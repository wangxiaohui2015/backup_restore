package com.my.br.dao;

import java.util.List;

import com.my.br.entity.MetaData;

public interface IMetaDataDao {
	List<MetaData> findMetaDataByHash(String hash) throws Exception;

	List<Integer> addMetaDatas(List<MetaData> metaDatas) throws Exception;

	MetaData findMetaDataById(int id) throws Exception;
}
