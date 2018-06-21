package com.my.br.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.my.br.ui.MainUI;

public class PropertiesUtil {
	private static Properties properties = new Properties();

	private static final PropertiesUtil instance = new PropertiesUtil();

	private String confFilePath = "";

	private PropertiesUtil() {

		confFilePath = MainUI.getRootDir() + File.separator + "conf"
				+ File.separator + "conf.properties";

		try {
			InputStream in = new FileInputStream(confFilePath);
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static PropertiesUtil getInstance() {
		return instance;
	}

	public String getProperties(String key) {
		return properties.getProperty(key);
	}

	public void putProperties(String key, String value) {
		try {
			properties.put(key, value);
			OutputStream out = new FileOutputStream(confFilePath);
			properties.store(out, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String userName = PropertiesUtil.getInstance().getProperties(
				"backup.targetlocation");
		System.out.println(userName);

		PropertiesUtil.getInstance().putProperties("backup.targetlocation",
				"D://");
	}
}
