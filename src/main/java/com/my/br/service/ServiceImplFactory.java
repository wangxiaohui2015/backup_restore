package com.my.br.service;

public class ServiceImplFactory {
	private static IBackupService backupService = new BackupServiceImpl();
	private static IRestoreService restoreService = new RestoreServiceImpl();
	private static IPolicyService policyService = new PolicyServiceImpl();

	public static IBackupService getBackupService() {
		return backupService;
	}

	public static IRestoreService getRestoreService() {
		return restoreService;
	}

	public static IPolicyService getPolicyService() {
		return policyService;
	}
}
