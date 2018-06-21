package com.my.br.service;

import com.my.br.entity.BackupInfo;

public interface IRestoreService {
	void startRecovery(BackupInfo backupInfo, String recoveryDir)
			throws Exception;
}
