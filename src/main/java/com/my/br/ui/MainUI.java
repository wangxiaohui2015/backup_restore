package com.my.br.ui;

import java.util.Scanner;
import java.util.logging.Level;

import com.mchange.v2.log.ModifyLogLevel;
import com.my.br.util.BackupTargetDirectoryUtil;

public class MainUI {

	private static Scanner scanner = new Scanner(System.in);

	private static String rootDir = "";

	public static void main(String[] args) {
		init(args);
		showMainPage();
	}

	private static void init(String[] args) {
		ModifyLogLevel.modifyInfoLevel(Level.ALL);
		if (args.length < 1) {
			prtln("The arguments is incorrect, please specify root dir.");
			System.exit(-1);
		}
		rootDir = args[0];
		BackupTargetDirectoryUtil.checkTargetLocation();
	}

	private static void showMainPage() {
		while (true) {
			prtln("");
			prtln("********** WELCOME TO BACKUP/RESTORE SYSTEM **********");
			prtln("[1] Policy  Management");
			prtln("[2] Backup & Restore Management");
			prtln("[0] Exit");
			int option = readOptions(0, 2);

			switch (option) {
			case 1:
				showPolicyManagementMainPage();
				break;
			case 2:
				showBackupAndRestoreManagementMainPage();
				break;
			case 0:
				prtln("Bye-bye.");
				System.exit(0);
			default:
				break;
			}
		}
	}

	private static void showPolicyManagementMainPage() {
		PolicyUI.getInstance().showPolicyUI();
	}

	private static void showBackupAndRestoreManagementMainPage() {
		BackupAndRestoreUI.getInstance().showBackupUI();
	}

	public static int readOptions(int minNum, int maxNum) {
		prt("Enter a number, range: [" + minNum + "," + maxNum + "]: ");
		while (true) {
			String str = scanner.nextLine();
			try {
				int intNum = Integer.parseInt(str);
				if (intNum >= minNum && intNum <= maxNum) {
					return intNum;
				} else {
					prt("Invalid input, enter again: ");
				}
			} catch (NumberFormatException e) {
				prt("Invalid input, enter again: ");
			}
		}
	}

	public static void enterAnything() {
		prtln("");
		prtln("Enter anything for continue...");
		scanner.next();
	}

	public static String enterString(String message, int minLen, int maxLen) {
		prt(message);
		while (true) {
			String str = scanner.nextLine();
			if (str.length() >= minLen && str.length() <= maxLen) {
				return str;
			} else {
				prt("Invalid input, enter again: ");
			}
		}
	}

	public static int enterNumber(String message, int minNum, int maxNum) {
		prt(message);
		while (true) {
			String str = scanner.nextLine();
			try {
				int intNum = Integer.parseInt(str);
				if (intNum >= minNum && intNum <= maxNum) {
					return intNum;
				} else {
					prt("Invalid input, enter again: ");
				}
			} catch (NumberFormatException e) {
				prt("Invalid input, enter again: ");
			}
		}
	}

	public static String getRootDir() {
		return rootDir;
	}

	public static void prt(String str) {
		System.out.print(str);
	}

	public static void prt(int str) {
		System.out.print(str);
	}

	public static void prtln(String str) {
		System.out.println(str);
	}

	public static void prtln(int str) {
		System.out.println(str);
	}
}
