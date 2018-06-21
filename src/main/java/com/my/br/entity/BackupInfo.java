package com.my.br.entity;

import java.util.Date;

import com.my.br.util.ProgressUtil;

public class BackupInfo {
	private int id;
	private int policyId;
	private int backupType;
	private Date startTime;
	private Date endTime;
	private String targetDir;
	private int isSuccessful;
	private long fileSize;
	private long dedupSize;
	private double dedupRate;
	private ProgressUtil progressUtil;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPolicyId() {
		return policyId;
	}

	public void setPolicyId(int policyId) {
		this.policyId = policyId;
	}

	public int getBackupType() {
		return backupType;
	}

	public void setBackupType(int backupType) {
		this.backupType = backupType;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

	public int getIsSuccessful() {
		return isSuccessful;
	}

	public void setIsSuccessful(int isSuccessful) {
		this.isSuccessful = isSuccessful;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public double getDedupRate() {
		return dedupRate;
	}

	public void setDedupRate(double dedupRate) {
		this.dedupRate = dedupRate;
	}

	public long getDedupSize() {
		return dedupSize;
	}

	public void setDedupSize(long dedupSize) {
		this.dedupSize = dedupSize;
	}

	public ProgressUtil getProgressUtil() {
		return progressUtil;
	}

	public void setProgressUtil(ProgressUtil progressUtil) {
		this.progressUtil = progressUtil;
	}
}
