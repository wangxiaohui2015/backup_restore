package com.my.br.test;

import com.my.br.entity.Policy;
import com.my.br.service.ServiceImplFactory;

public class TestBackup {
	public static void main(String[] args) {
		testBackup();
	}

	private static void testBackup() {
		Policy policy = new Policy();
		policy.setId(3);
		policy.setPolicyName("WPS");
		// policy.setSourceDir("D:\\Eclipse\\workspace\\myeclipse10_7\\BackRestore\\tmp\\source");
		policy.setSourceDir("E:\\software\\WPS");
		policy.setMemo("WPS");
		try {
			ServiceImplFactory.getBackupService().startBackup(policy);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
