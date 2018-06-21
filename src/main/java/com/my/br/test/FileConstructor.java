package com.my.br.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.my.br.util.IOUtils;

public class FileConstructor {

	private static FileConstructor instance = new FileConstructor();

	private FileConstructor() {

	}

	public static FileConstructor getInstance() {
		return instance;
	}

	public void constructFile(List<String> subSourceFiles, String destFile) {
		FileOutputStream out = null;
		try {
			File dFile = new File(destFile);
			if (dFile.exists()) {
				dFile.delete();
			}
			dFile.createNewFile();
			out = new FileOutputStream(destFile);
			for (String source : subSourceFiles) {
				FileInputStream in = new FileInputStream(source);
				byte[] bytes = new byte[1024];
				int length = -1;
				while ((length = in.read(bytes)) != -1) {
					out.write(bytes, 0, length);
				}
				IOUtils.closeInputStream(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeOutputStream(out);
		}
	}

	private static void testConstructFile() {
		List<String> subFileNames = new ArrayList<String>();
		for (int i = 0; i <= 15; i++) {
			String subFileName = "D:\\Eclipse\\workspace\\myeclipse10_7\\BackRestore\\tmp"
					+ "\\FileSpliter.java_" + i;
			subFileNames.add(subFileName);
		}
		FileConstructor
				.getInstance()
				.constructFile(subFileNames,
						"D:\\Eclipse\\workspace\\myeclipse10_7\\BackRestore\\tmp\\FileSpliter.java");
	}

	private static void testConstructBigFile() {
		List<String> subFileNames = new ArrayList<String>();
		for (int i = 0; i <= 99327; i++) {
			String subFileName = "D:\\Eclipse\\workspace\\myeclipse10_7\\BackRestore\\tmp\\bigFile"
					+ "\\ubuntu-14.04-desktop-i386.iso_" + i;
			subFileNames.add(subFileName);
		}
		FileConstructor
				.getInstance()
				.constructFile(
						subFileNames,
						"D:\\Eclipse\\workspace\\myeclipse10_7\\BackRestore\\tmp\\bigFile\\ubuntu-14.04-desktop-i386.iso");
		System.out.println("Finished.");
	}

	public static void main(String[] args) {
		// testConstructFile();
		testConstructBigFile();
	}
}
