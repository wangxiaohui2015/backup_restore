package com.my.br.util;

import java.io.File;

public class FileUtils {
	public static long getDirSize(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				long size = 0;
				for (File f : children) {
					size += getDirSize(f);
				}
				return size;
			} else {
				return file.length();
			}
		} else {
			return 0;
		}
	}
}
