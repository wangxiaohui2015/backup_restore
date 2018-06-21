package com.my.br.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtil {

	public static String getSHA1(byte[] inputBytes) {
		StringBuffer result = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(inputBytes);
			byte[] bytes = md.digest();
			for (byte b : bytes) {
				String hexStr = Integer.toHexString(b & 0xFF);
				if (hexStr.length() == 1) {
					hexStr += "0";
				}
				result.append(hexStr);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result.toString().toUpperCase();
	}

	public static String getSHA1(String message) {
		return getSHA1(message.getBytes());
	}

	public static void main(String[] args) {
		System.out.println(getSHA1("abcd"));
	}
}
