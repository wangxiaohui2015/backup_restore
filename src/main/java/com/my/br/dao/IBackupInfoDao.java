package com.my.br.dao;

import java.util.List;

import com.my.br.entity.BackupInfo;
import com.my.br.entity.Policy;

public interface IBackupInfoDao {
	int addBackupInfo(BackupInfo backupInfo) throws Exception;

	List<BackupInfo> queryBackupInfos(Policy policy) throws Exception;

	int updateBackupInfo(BackupInfo backupInfo) throws Exception;
}
