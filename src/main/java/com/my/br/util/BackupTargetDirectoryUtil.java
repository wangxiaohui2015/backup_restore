package com.my.br.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.my.br.ui.MainUI;

public class BackupTargetDirectoryUtil {

	public static final String TARGET_CONF_FILENAME = "target.conf";
	public static final String TARGET_INDEX_KEY = "backup.metadata.index";

	public static void checkTargetDirectory(String targetDirectory)
			throws Exception {
		File file = new File(targetDirectory);
		if (file.exists() && file.isDirectory()) {
			File confFile = new File(targetDirectory + File.separator
					+ TARGET_CONF_FILENAME);
			if (!confFile.exists()) {
				confFile.createNewFile();
			}
			Properties properties = new Properties();
			properties.load(new FileInputStream(confFile));
			String index = properties.getProperty(TARGET_INDEX_KEY);
			if (index == null || "".equals(index)) {
				updateIndexKey(targetDirectory, "1000000000000000");
			} else if (index.length() != 16) {
				throw new Exception("target.conf is invalid.");
			}
		} else {
			throw new Exception("Target directory is invalid.");
		}
	}

	public static void updateIndexKey(String targetDirectory, String indexKey)
			throws Exception {
		File confFile = new File(targetDirectory + File.separator
				+ TARGET_CONF_FILENAME);
		if (!confFile.exists()) {
			throw new Exception("Target config file is invalid.");
		} else if (indexKey.length() != 16) {
			throw new Exception("The key in target.conf is invalid.");
		} else {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new FileInputStream(confFile);
				out = new FileOutputStream(confFile);
				Properties properties = new Properties();
				properties.load(in);
				properties.setProperty(TARGET_INDEX_KEY, indexKey);
				properties.store(out, "Update index key, date: " + new Date());
			} finally {
				IOUtils.closeOutputStream(out);
				IOUtils.closeInputStream(in);
			}
		}
	}

	public static synchronized List<String> generateMetaDataPaths(
			String targetDirectory, int count) throws Exception {
		List<String> results = new LinkedList<String>();
		File confFile = new File(targetDirectory + File.separator
				+ TARGET_CONF_FILENAME);
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(confFile);
			Properties properties = new Properties();
			properties.load(in);

			out = new FileOutputStream(confFile);

			String index = properties.getProperty(TARGET_INDEX_KEY);
			long indexLongVal = Long.parseLong(index);
			for (int i = 0; i < count; i++) {
				results.add(String.valueOf((indexLongVal + i)));
			}

			properties.setProperty(TARGET_INDEX_KEY,
					String.valueOf(indexLongVal + count));
			properties.store(out, "Update index key, date: " + new Date());
		} finally {
			IOUtils.closeOutputStream(out);
			IOUtils.closeInputStream(in);
		}
		return results;
	}

	public static void checkTargetLocation() {
		String targetLocation = PropertiesUtil.getInstance().getProperties(
				"backup.targetDir");
		if (!(null == targetLocation || "".equals(targetLocation))) {
			return;
		}

		targetLocation = MainUI.getRootDir() + File.separator + "backupdata";

		PropertiesUtil.getInstance().putProperties("backup.targetDir",
				targetLocation);
	}
}
