package com.my.br.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.my.br.dao.DaoImplFactory;
import com.my.br.entity.BackupDetails;
import com.my.br.entity.BackupInfo;
import com.my.br.entity.MetaData;
import com.my.br.entity.MetaDataCatalog;
import com.my.br.util.Constants;
import com.my.br.util.IOUtils;

public class RestoreServiceImpl implements IRestoreService {

	@Override
	public void startRecovery(BackupInfo backupInfo, String recoveryDir)
			throws Exception {
		resolveBackupDetails(backupInfo, "", 0, recoveryDir);
	}

	private void resolveBackupDetails(BackupInfo backupInfo, String relatedDir,
			int parentBackupDetailsId, String recoveryDir) throws Exception {
		List<BackupDetails> backupDetailsList = DaoImplFactory
				.getBackupDetailsDao().findBackupDetail(backupInfo.getId(),
						parentBackupDetailsId);
		for (BackupDetails backupDetails : backupDetailsList) {
			if (backupDetails.isDir()) {
				// String recoverySourceDirName = File.separator + relatedDir
				// + backupDetails.getFileName();
				// System.out.println("D: " + recoverySourceDirName);

				String recoveryDestDirName = recoveryDir + File.separator
						+ relatedDir + backupDetails.getFileName();
				new File(recoveryDestDirName).mkdirs();

				resolveBackupDetails(backupInfo,
						relatedDir + backupDetails.getFileName()
								+ File.separator, backupDetails.getId(),
						recoveryDir);
			} else {
				// String recoverySourceFileName = File.separator + relatedDir
				// + backupDetails.getFileName();
				// System.out.println("F: " + recoverySourceFileName);

				String recoveryDestFileName = recoveryDir + File.separator
						+ relatedDir + backupDetails.getFileName();
				File newFile = new File(recoveryDestFileName);
				if (newFile.exists()) {
					newFile.delete();
				}
				newFile.createNewFile();
				resolveMetaDataCatalog(backupInfo,
						backupDetails.getMetaDataCatalogRootId(), 0,
						recoveryDestFileName);
			}
		}
	}

	private void resolveMetaDataCatalog(BackupInfo backupInfo,
			int metaDataCatalogId, int metaDataCatalogParentId,
			String destFileName) throws Exception {
		MetaDataCatalog metaDataCatalog = null;

		if (metaDataCatalogParentId == 0) {
			metaDataCatalog = DaoImplFactory.getMetaDataCatalog()
					.findMetaDataCatalogById(metaDataCatalogId);
		} else if (metaDataCatalogId == 0) {
			metaDataCatalog = DaoImplFactory.getMetaDataCatalog()
					.findMetaDataCatalogByParentId(metaDataCatalogParentId);
		}

		if (null != metaDataCatalog) {

			resolveMetaData(backupInfo, destFileName, metaDataCatalog);

			resolveMetaDataCatalog(backupInfo, 0, metaDataCatalog.getId(),
					destFileName);
		}
	}

	private void resolveMetaData(BackupInfo backupInfo, String destFileName,
			MetaDataCatalog metaDataCatalog) throws FileNotFoundException,
			Exception, IOException {
		OutputStream out = new FileOutputStream(destFileName, true);

		String metaDataIds = metaDataCatalog.getMetaDataIds();
		String[] metaDataIdsArray = metaDataIds.split(",");
		for (String metaDataId : metaDataIdsArray) {
			if ("".equals(metaDataId)) {
				continue;
			}
			MetaData metaData = DaoImplFactory.getMetaDataDao()
					.findMetaDataById(Integer.parseInt(metaDataId));
			String metaDataFilePath = metaData.getFilePath();
			String sourceFilePath = backupInfo.getTargetDir() + File.separator
					+ metaDataFilePath.substring(0, 4) + File.separator
					+ metaDataFilePath.substring(4, 8) + File.separator
					+ metaDataFilePath.substring(8, 12) + File.separator
					+ metaDataFilePath.substring(12, 16) + ".data";
			InputStream in = new FileInputStream(sourceFilePath);
			int length = -1;
			byte[] bytes = new byte[Constants.DATA_BLOCK_SIZE];
			while ((length = in.read(bytes)) != -1) {
				out.write(bytes, 0, length);

				// Show progress
				if (backupInfo.getProgressUtil() != null) {
					backupInfo.getProgressUtil().showProgressByIncress(length);
				}
			}
			IOUtils.closeInputStream(in);
		}

		IOUtils.closeOutputStream(out);
	}
}
