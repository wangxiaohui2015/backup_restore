package com.my.br.dao;

import com.my.br.entity.MetaDataCatalog;

public interface IMetaDataCatalogDao {
	int addMetaDataCatalog(MetaDataCatalog metaDataCatalog) throws Exception;

	MetaDataCatalog findMetaDataCatalogById(int id) throws Exception;

	MetaDataCatalog findMetaDataCatalogByParentId(int parentId)
			throws Exception;
}
