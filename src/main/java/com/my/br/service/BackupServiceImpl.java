package com.my.br.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.my.br.dao.DaoImplFactory;
import com.my.br.entity.BackupDetails;
import com.my.br.entity.BackupInfo;
import com.my.br.entity.MetaData;
import com.my.br.entity.MetaDataCatalog;
import com.my.br.entity.Policy;
import com.my.br.util.BackupTargetDirectoryUtil;
import com.my.br.util.Constants;
import com.my.br.util.FileUtils;
import com.my.br.util.IOUtils;
import com.my.br.util.MessageDigestUtil;
import com.my.br.util.ProgressUtil;
import com.my.br.util.PropertiesUtil;

public class BackupServiceImpl implements IBackupService {

	@Override
	public BackupInfo startBackup(Policy policy) throws Exception {
		BackupInfo backupInfo = createBackupInfo(policy);
		BackupTargetDirectoryUtil.checkTargetDirectory(backupInfo
				.getTargetDir());
		backupInfo.setProgressUtil(new ProgressUtil(FileUtils
				.getDirSize(new File(policy.getSourceDir()))));
		File sourceDir = new File(policy.getSourceDir());
		if (sourceDir.exists() && sourceDir.isDirectory()) {
			resolveBackupDetails(sourceDir, backupInfo, null);
		}
		updateBackupInfoWhenFinish(backupInfo);
		return backupInfo;
	}

	private void resolveBackupDetails(File file, BackupInfo backupInfo,
			BackupDetails parentBackupDetails) throws Exception {

		/*
		 * System.out.println((file.isDirectory() ? "D: " : "F: ") +
		 * file.getAbsolutePath());
		 */
		if (file.isDirectory()) {
			BackupDetails backupDetails = new BackupDetails();
			backupDetails.setBackupInfoId(backupInfo.getId());
			backupDetails.setFileName(file.getName());
			backupDetails.setDir(true);
			backupDetails.setParentId(parentBackupDetails == null ? 0
					: parentBackupDetails.getId());
			backupDetails.setMetaDataCatalogRootId(0);
			DaoImplFactory.getBackupDetailsDao()
					.addBackupDetails(backupDetails);
			File[] files = file.listFiles();
			for (File f : files) {
				resolveBackupDetails(f, backupInfo, backupDetails);
			}
		} else {
			int parentCataLogId = resolveBackupCatalog(file, backupInfo);
			BackupDetails backupDetails = new BackupDetails();
			backupDetails.setBackupInfoId(backupInfo.getId());
			backupDetails.setFileName(file.getName());
			backupDetails.setDir(false);
			backupDetails.setParentId(parentBackupDetails.getId());
			backupDetails.setMetaDataCatalogRootId(parentCataLogId);
			DaoImplFactory.getBackupDetailsDao()
					.addBackupDetails(backupDetails);
		}
	}

	private int resolveBackupCatalog(File file, BackupInfo backupInfo)
			throws Exception {
		InputStream in = new FileInputStream(file);
		int length = -1;
		byte[] bytes = new byte[Constants.DATA_BLOCK_SIZE];

		int fileTotalLength = 0;

		List<byte[]> byteList = new ArrayList<byte[]>();

		int rootCatalogId = 0;
		int newParentCatalogId = 0;
		while ((length = in.read(bytes)) != -1) {
			fileTotalLength += length;
			byteList.add(Arrays.copyOf(bytes, length));

			if (byteList.size() == Constants.DATA_CATALOG_LENGTH) {
				newParentCatalogId = resolveBackupMetaDataAndCataLog(byteList,
						backupInfo, newParentCatalogId);
				if (rootCatalogId == 0) {
					rootCatalogId = newParentCatalogId;
				}
				byteList.clear();
			}
		}

		newParentCatalogId = resolveBackupMetaDataAndCataLog(byteList,
				backupInfo, newParentCatalogId);
		if (rootCatalogId == 0) {
			rootCatalogId = newParentCatalogId;
		}

		backupInfo.setFileSize(backupInfo.getFileSize() + fileTotalLength);
		return rootCatalogId;
	}

	private int resolveBackupMetaDataAndCataLog(List<byte[]> byteList,
			BackupInfo backupInfo, int parentCataLogId) throws Exception {

		List<Map<String, Object>> catalogInfo = new ArrayList<Map<String, Object>>();
		List<String> pathes = BackupTargetDirectoryUtil.generateMetaDataPaths(
				backupInfo.getTargetDir(), byteList.size());
		List<Integer> metaDataIds = new ArrayList<Integer>();

		for (byte[] byteData : byteList) {
			String hash = MessageDigestUtil.getSHA1(byteData);
			List<MetaData> metaDatas = DaoImplFactory.getMetaDataDao()
					.findMetaDataByHash(hash);
			int id = findMetaDataIdByHashAndFilePath(byteData, metaDatas,
					backupInfo);
			metaDataIds.add(id);
			if (id == 0) {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("HASH", hash);
				dataMap.put("PATH", pathes.remove(0));
				dataMap.put("DATA", byteData);
				catalogInfo.add(dataMap);
			} else {
				// System.out.println("Dedup, hash: " + hash);
				backupInfo.setDedupSize(backupInfo.getDedupSize()
						+ byteData.length);
			}
			if (backupInfo.getProgressUtil() != null) {
				backupInfo.getProgressUtil().showProgressByIncress(
						byteData.length);
			}
		}
		writeDataToFiles(catalogInfo, backupInfo);
		List<Integer> metaDataDBIds = insertMetaDataToDB(catalogInfo);
		for (int i = 0; i < metaDataIds.size(); i++) {
			if (metaDataIds.get(i) == 0) {
				metaDataIds.set(i, metaDataDBIds.remove(0));
			}
		}

		StringBuffer sb = new StringBuffer();
		for (Integer id : metaDataIds) {
			sb.append(id);
			sb.append(",");
		}

		// Insert Data into metadata_catalog
		MetaDataCatalog metaDataCatalog = new MetaDataCatalog();
		metaDataCatalog.setMetaDataIds(sb.toString());
		metaDataCatalog.setParentId(parentCataLogId);
		DaoImplFactory.getMetaDataCatalog().addMetaDataCatalog(metaDataCatalog);

		return metaDataCatalog.getId();
	}

	private List<Integer> insertMetaDataToDB(
			List<Map<String, Object>> catalogInfo) throws Exception {
		List<MetaData> metaDatas = new ArrayList<MetaData>();
		for (Map<String, Object> catalog : catalogInfo) {
			String hash = (String) catalog.get("HASH");
			String path = (String) catalog.get("PATH");
			MetaData metaData = new MetaData();
			metaData.setDigest(hash);
			metaData.setFilePath(path);
			metaDatas.add(metaData);
		}

		List<Integer> ids = DaoImplFactory.getMetaDataDao().addMetaDatas(
				metaDatas);
		return ids;
	}

	private void writeDataToFiles(List<Map<String, Object>> catalogInfo,
			BackupInfo backupInfo) throws Exception {
		for (Map<String, Object> catalog : catalogInfo) {
			String path = (String) catalog.get("PATH");

			String folderPath = backupInfo.getTargetDir() + File.separator
					+ path.substring(0, 4) + File.separator
					+ path.substring(4, 8) + File.separator
					+ path.substring(8, 12);
			new File(folderPath).mkdirs();
			File newFile = new File(folderPath + File.separator
					+ path.substring(12, 16) + ".data");
			newFile.createNewFile();

			byte[] data = (byte[]) catalog.get("DATA");
			OutputStream out = new FileOutputStream(newFile);
			out.write(data);
			IOUtils.closeOutputStream(out);
		}
	}

	private int findMetaDataIdByHashAndFilePath(byte[] bytes,
			List<MetaData> metaDatas, BackupInfo backupInfo) throws Exception {
		for (MetaData metaData : metaDatas) {

			return metaData.getId();

			// String metaDataPath = metaData.getFilePath();
			// String filePath = backupInfo.getTargetDir() + File.separator
			// + metaDataPath.substring(0, 4) + File.separator
			// + metaDataPath.substring(4, 8) + File.separator
			// + metaDataPath.substring(8, 12) + File.separator
			// + metaDataPath.substring(12, 16) + ".data";
			// FileInputStream in = new FileInputStream(filePath);
			// try {
			// int index = 0;
			// while (true) {
			// int data = in.read();
			// if (index == bytes.length + 1) {
			// break;
			// }
			// if (data == -1 && index != bytes.length) {
			// break;
			// }
			// if (data == -1 && index == bytes.length) {
			// return metaData.getId();
			// }
			// if ((byte) data != bytes[index]) {
			// break;
			// }
			// index++;
			// }
			// } finally {
			// IOUtils.closeInputStream(in);
			// }
		}
		return 0;
	}

	private BackupInfo createBackupInfo(Policy policy) throws Exception {
		BackupInfo backupInfo = new BackupInfo();
		backupInfo.setPolicyId(policy.getId());
		backupInfo.setBackupType(1);
		backupInfo.setStartTime(new Date());
		backupInfo.setEndTime(new Date());
		backupInfo.setTargetDir(PropertiesUtil.getInstance().getProperties(
				"backup.targetDir"));
		backupInfo.setIsSuccessful(Constants.BACKUP_RESULT_FAILED);
		DaoImplFactory.getBackupInfoDao().addBackupInfo(backupInfo);
		return backupInfo;
	}

	private void updateBackupInfoWhenFinish(BackupInfo backupInfo)
			throws Exception {
		backupInfo.setEndTime(new Date());
		backupInfo.setIsSuccessful(Constants.BACKUP_RESULT_SUCCESS);
		if (backupInfo.getFileSize() == 0) {
			backupInfo.setDedupRate(0);
		} else {
			backupInfo.setDedupRate(backupInfo.getDedupSize() * 1.0
					/ backupInfo.getFileSize());
		}
		DaoImplFactory.getBackupInfoDao().updateBackupInfo(backupInfo);
	}

	@Override
	public List<BackupInfo> queryBackupInfos(Policy policy) throws Exception {
		return DaoImplFactory.getBackupInfoDao().queryBackupInfos(policy);
	}
}
