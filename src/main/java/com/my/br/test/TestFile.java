package com.my.br.test;

import java.io.File;
import java.io.IOException;

public class TestFile {
	public static void main(String[] args) {
		testCreateFile();
	}

	private static void testCreateFile() {
		File f = new File("D:\\a\\b\\c.data");
		try {
			f.mkdirs();
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
