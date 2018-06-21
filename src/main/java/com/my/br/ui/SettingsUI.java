package com.my.br.ui;

import java.util.List;

import com.my.br.entity.Policy;
import com.my.br.service.ServiceImplFactory;

public class SettingsUI {
	private static SettingsUI instance = new SettingsUI();

	private SettingsUI() {

	}

	public static SettingsUI getInstance() {
		return instance;
	}

	public void showSettingsUI() {
		while (true) {
			MainUI.prtln("");
			MainUI.prtln("********** Settings Management **********");
			MainUI.prtln("[1] Query All Policies");
			MainUI.prtln("[2] Create Policy");
			MainUI.prtln("[0] Return");
			int option = MainUI.readOptions(0, 2);
			switch (option) {
			case 1:
				break;
			case 2:
				break;
			case 0:
				return;
			default:
				break;
			}
			// Main.enterAnything();
		}
	}
}
