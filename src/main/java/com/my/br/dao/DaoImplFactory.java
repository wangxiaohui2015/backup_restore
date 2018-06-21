package com.my.br.dao;

public class DaoImplFactory {
	private static IPolicyDao policyDao = new PolicyDaoImpl();
	private static IBackupInfoDao backupDao = new BackupInfoDaoImpl();
	private static IBackupDetailsDao backupDetailsDao = new BackupDetailsDaoImpl();
	private static IMetaDataDao metaDataDao = new MetaDataDaoImpl();
	private static IMetaDataCatalogDao metaDataCatalog = new MetaDataCatalogDaoImpl();

	public static IPolicyDao getPolicyDao() {
		return policyDao;
	}

	public static IBackupInfoDao getBackupInfoDao() {
		return backupDao;
	}

	public static IBackupDetailsDao getBackupDetailsDao() {
		return backupDetailsDao;
	}

	public static IMetaDataDao getMetaDataDao() {
		return metaDataDao;
	}

	public static IMetaDataCatalogDao getMetaDataCatalog() {
		return metaDataCatalog;
	}
}
