package com.my.br.entity;

public class BackupDetails {
	private int id;
	private int backupInfoId;
	private String fileName;
	private boolean isDir;
	private int parentId;
	private int metaDataCatalogRootId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBackupInfoId() {
		return backupInfoId;
	}

	public void setBackupInfoId(int backupInfoId) {
		this.backupInfoId = backupInfoId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isDir() {
		return isDir;
	}

	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getMetaDataCatalogRootId() {
		return metaDataCatalogRootId;
	}

	public void setMetaDataCatalogRootId(int metaDataCatalogRootId) {
		this.metaDataCatalogRootId = metaDataCatalogRootId;
	}
}
