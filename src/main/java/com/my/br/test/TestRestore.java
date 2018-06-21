package com.my.br.test;

import com.my.br.entity.BackupInfo;
import com.my.br.service.IRestoreService;
import com.my.br.service.ServiceImplFactory;

public class TestRestore {
	public static void main(String[] args) {
		try {
			testRestore();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testRestore() throws Exception {
		BackupInfo backupInfo = new BackupInfo();
		backupInfo.setId(5);
		backupInfo
				.setTargetDir("D:\\Eclipse\\workspace\\myeclipse10_7\\BackRestore\\tmp\\target");

		IRestoreService restoreService = ServiceImplFactory.getRestoreService();
		restoreService
				.startRecovery(backupInfo,
						"D:\\Eclipse\\workspace\\myeclipse10_7\\BackRestore\\tmp\\recovery");
	}
}
