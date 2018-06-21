package com.my.br.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.my.br.util.IOUtils;

public class FileSpliter {

	private static FileSpliter instance = new FileSpliter();

	private FileSpliter() {

	}

	public static FileSpliter getInstance() {
		return instance;
	}

	public List<String> splitFile(String sourceFile, String targetDir) {
		return splitFile(sourceFile, targetDir, 1024);
	}

	public List<String> splitFile(String sourceFile, String targetDir,
			int byteSize) {
		List<String> results = new LinkedList<String>();
		File sFile = new File(sourceFile);
		if (!sFile.isFile()) {
			return null;
		}

		FileOutputStream out = null;
		FileInputStream in = null;
		File outFile = null;
		try {
			in = new FileInputStream(sourceFile);
			byte[] bytes = new byte[byteSize];
			int length = -1;
			String subFileName = "";
			for (int i = 0; (length = in.read(bytes)) != -1; i++) {
				while (true) {
					subFileName = sFile.getName() + "_" + i;
					outFile = new File(targetDir + File.separator + subFileName);
					if (!outFile.exists()) {
						break;
					} else {
						i++;
					}
				}
				out = new FileOutputStream(outFile);
				out.write(bytes, 0, length);
				out.flush();
				IOUtils.closeOutputStream(out);
				results.add(subFileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeOutputStream(out);
			IOUtils.closeInputStream(in);
		}

		return results;
	}

	private static void testSplitFile() {
		List<String> results = FileSpliter
				.getInstance()
				.splitFile(
						"D:\\Eclipse\\workspace\\myeclipse10_7\\BackRestore\\src\\com\\my\\br\\FileSpliter.java",
						"D:\\Eclipse\\workspace\\myeclipse10_7\\BackRestore\\tmp",
						128);
		System.out.println(Arrays.toString(results.toArray()));
	}

	private static void testSplitBigFile() {
		List<String> results = FileSpliter
				.getInstance()
				.splitFile(
						"E:\\software\\OS\\ubuntu-14.04-desktop-i386.iso",
						"D:\\Eclipse\\workspace\\myeclipse10_7\\BackRestore\\tmp\\bigFile",
						1024 * 10);
		// System.out.println(Arrays.toString(results.toArray()));
		System.out.println("Finished.");
	}

	public static void main(String[] args) {
		// testSplitFile();
		testSplitBigFile();
	}
}
