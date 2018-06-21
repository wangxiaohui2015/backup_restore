package com.my.br.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.my.br.entity.BackupInfo;
import com.my.br.entity.Policy;
import com.my.br.service.IRestoreService;
import com.my.br.service.ServiceImplFactory;
import com.my.br.util.DateFormat;
import com.my.br.util.NumberFormatUtil;
import com.my.br.util.ProgressUtil;

public class BackupAndRestoreUI {
	private static BackupAndRestoreUI instance = new BackupAndRestoreUI();

	private BackupAndRestoreUI() {

	}

	public static BackupAndRestoreUI getInstance() {
		return instance;
	}

	public void showBackupUI() {
		while (true) {
			MainUI.prtln("");
			MainUI.prtln("********** Backup Management **********");
			MainUI.prtln("[1] Show Backup Points and Execute Restore");
			MainUI.prtln("[2] Execute Backup");
			MainUI.prtln("[0] Return");
			int option = MainUI.readOptions(0, 2);
			switch (option) {
			case 1:
				showBackupPointsAndExecuteRestore();
				break;
			case 2:
				executeBackup();
				break;
			case 0:
				return;
			default:
				break;
			}
		}
	}

	private void executeBackup() {
		try {
			Policy policy = null;
			while (true) {
				int policyId = MainUI.enterNumber("Policy ID: ", 1,
						Integer.MAX_VALUE);
				policy = ServiceImplFactory.getPolicyService().findPolicyById(
						policyId);
				if (null == policy) {
					MainUI.prtln("Cannot find the policy, ID: " + policyId);
					String message = MainUI.enterString(
							"Continue? [y]:enter another policy ID.", 1, 128);
					if (message.equalsIgnoreCase("y")) {
						continue;
					} else {
						return;
					}
				} else {

					// Print policy
					MainUI.prtln("Policy Information: ");
					ConsoleTable table = new ConsoleTable(4, true);
					table.appendRow();
					table.appendColum("ID").appendColum("POLICY NAME")
							.appendColum("SOURCE DIR").appendColum("MEMO");
					table.appendRow();
					table.appendColum(policy.getId())
							.appendColum(policy.getPolicyName())
							.appendColum(policy.getSourceDir())
							.appendColum(policy.getMemo());
					MainUI.prtln(table.toString());

					String message = MainUI
							.enterString(
									"Confirm the policy information above, continue? [y]:backup right now, [e]:enter another policy ID.",
									1, 128);
					if (message.equalsIgnoreCase("y")) {
						break;
					} else if (message.equalsIgnoreCase("e")) {
						continue;
					} else {
						return;
					}
				}
			}
			MainUI.prtln("Executing backup...");
			BackupInfo backupInfo = ServiceImplFactory.getBackupService()
					.startBackup(policy);
			MainUI.prtln("Succeed.");
			List<BackupInfo> backupInfos = new ArrayList<BackupInfo>();
			backupInfos.add(backupInfo);
			showBackupInfos(policy, backupInfos);
		} catch (Exception e) {
			MainUI.prtln("Failed to execute backup operation, error: "
					+ e.getMessage());
		}
	}

	private void showBackupPointsAndExecuteRestore() {
		try {
			Policy policy = null;
			while (true) {
				int policyId = MainUI.enterNumber("Policy ID: ", 1,
						Integer.MAX_VALUE);
				policy = ServiceImplFactory.getPolicyService().findPolicyById(
						policyId);
				if (null == policy) {
					MainUI.prtln("Cannot find the policy, ID: " + policyId);
					String message = MainUI.enterString(
							"Continue? [y]:enter another policy ID.", 1, 128);
					if (message.equalsIgnoreCase("y")) {
						continue;
					} else {
						return;
					}
				} else {
					break;
				}
			}
			List<BackupInfo> backupInfos = ServiceImplFactory
					.getBackupService().queryBackupInfos(policy);
			showBackupInfos(policy, backupInfos);

			BackupInfo backupInfo = null;
			while (true) {
				int backupPointId = MainUI
						.enterNumber(
								"Enter backup points id to start restore, enter 0 to return: ",
								0, Integer.MAX_VALUE);
				if (backupPointId == 0) {
					return;
				}

				backupInfo = findBackupInfoById(backupInfos, backupPointId);
				if (null == backupInfo) {
					String message = MainUI
							.enterString(
									"The backup point ID is invalid, enter another id? [y]: Enter another ID.",
									1, 128);
					if ("y".equalsIgnoreCase(message)) {
						continue;
					} else {
						return;
					}
				} else if (backupInfo.getIsSuccessful() == 0) {
					String message = MainUI
							.enterString(
									"Cannot restore to a failed point, enter another id? [y]: Enter another ID.",
									1, 128);
					if ("y".equalsIgnoreCase(message)) {
						continue;
					} else {
						return;
					}
				} else {
					break;
				}
			}

			String recoveryDir = "";
			while (true) {
				recoveryDir = MainUI.enterString("Enter a target location: ",
						1, 1024);
				if (!new File(recoveryDir).isDirectory()) {
					String invalidTargetMessage = MainUI
							.enterString(
									"The target location is invalid, enter again? [y]: Enter another location.",
									1, 128);
					if ("y".equalsIgnoreCase(invalidTargetMessage)) {
						continue;
					} else {
						return;
					}
				} else {
					break;
				}
			}

			String message = MainUI.enterString(
					"Are you sure to restore, backup point ID: "
							+ backupInfo.getId() + ", [y]: Restore.", 1, 128);
			if ("y".equalsIgnoreCase(message)) {
				IRestoreService restoreService = ServiceImplFactory
						.getRestoreService();
				MainUI.prtln("Recovering...");
				ProgressUtil progressUtil = new ProgressUtil(
						backupInfo.getFileSize());
				backupInfo.setProgressUtil(progressUtil);
				restoreService.startRecovery(backupInfo, recoveryDir);
				MainUI.prtln("Succeed.");
			} else {
				return;
			}

		} catch (Exception e) {
			MainUI.prtln("Failed to show backup points and recovery, error: "
					+ e.getMessage());
		}
	}

	private void showBackupInfos(Policy policy, List<BackupInfo> backupInfos) {
		ConsoleTable table = new ConsoleTable(9, true);
		table.appendRow();
		table.appendColum("ID").appendColum("POLICY NAME")
				.appendColum("RESULT").appendColum("START TIME")
				.appendColum("END TIME").appendColum("ELAPSED")
				.appendColum("FILE SIZE(KB)").appendColum("SPEED(KB/s)")
				.appendColum("DEDUP PERCENT");
		for (BackupInfo backupInfo : backupInfos) {
			table.appendRow();
			table.appendColum(backupInfo.getId())
					.appendColum(policy.getPolicyName())
					.appendColum(
							backupInfo.getIsSuccessful() == 0 ? "FAILED"
									: "SUCCESS")
					.appendColum(
							DateFormat.dateToString(backupInfo.getStartTime()))
					.appendColum(
							DateFormat.dateToString(backupInfo.getEndTime()))
					.appendColum(
							DateFormat.calcElapsed(backupInfo.getStartTime(),
									backupInfo.getEndTime()))
					.appendColum(backupInfo.getFileSize() / 1024)
					.appendColum(
							(backupInfo.getEndTime().getTime() - backupInfo
									.getStartTime().getTime()) == 0 ? "0"
									: (backupInfo.getFileSize() / 1024)
											/ ((backupInfo.getEndTime()
													.getTime() - backupInfo
													.getStartTime().getTime()) / 1000))
					.appendColum(
							NumberFormatUtil.formatDoubleNumber(backupInfo
									.getDedupRate() * 100) + "%");
		}
		MainUI.prtln(table.toString());
	}

	private BackupInfo findBackupInfoById(List<BackupInfo> backupInfos,
			int backupInfoId) {
		for (BackupInfo backupInfo : backupInfos) {
			if (backupInfo.getId() == backupInfoId) {
				return backupInfo;
			}
		}
		return null;
	}
}
