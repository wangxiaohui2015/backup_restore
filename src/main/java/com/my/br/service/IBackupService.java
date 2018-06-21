package com.my.br.service;

import java.util.List;

import com.my.br.entity.BackupInfo;
import com.my.br.entity.Policy;

public interface IBackupService {
	BackupInfo startBackup(Policy policy) throws Exception;

	List<BackupInfo> queryBackupInfos(Policy policy) throws Exception;
}
