package com.my.br.dao;

import java.util.List;

import com.my.br.entity.BackupDetails;

public interface IBackupDetailsDao {
	int addBackupDetails(BackupDetails backupDetails) throws Exception;

	List<BackupDetails> findBackupDetail(int backupInfoId, int parentId)
			throws Exception;
}
