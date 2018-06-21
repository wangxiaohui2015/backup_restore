package com.my.br.entity;

public class MetaDataCatalog {
	private int id;
	private String metaDataIds;
	private int parentId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMetaDataIds() {
		return metaDataIds;
	}

	public void setMetaDataIds(String metaDataIds) {
		this.metaDataIds = metaDataIds;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
}
