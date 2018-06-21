package com.my.br.ui;

import java.util.List;

import com.my.br.entity.Policy;
import com.my.br.service.ServiceImplFactory;

public class PolicyUI {
	private static PolicyUI instance = new PolicyUI();

	private PolicyUI() {

	}

	public static PolicyUI getInstance() {
		return instance;
	}

	public void showPolicyUI() {
		while (true) {
			MainUI.prtln("");
			MainUI.prtln("********** Policy Management **********");
			MainUI.prtln("[1] Query All Policies");
			MainUI.prtln("[2] Create Policy");
			MainUI.prtln("[0] Return");
			int option = MainUI.readOptions(0, 2);
			switch (option) {
			case 1:
				queryAllPolicies();
				break;
			case 2:
				createPolicy();
				break;
			case 0:
				return;
			default:
				queryAllPolicies();
				break;
			}

			// Main.enterAnything();
		}
	}

	private void createPolicy() {
		String policyName = MainUI.enterString("Policy name: ", 1, 128);
		String sourceDir = MainUI.enterString("Source directory: ", 1, 2048);
		String memo = MainUI.enterString("Memo: ", 0, 2048);

		Policy policy = new Policy();
		policy.setPolicyName(policyName);
		policy.setSourceDir(sourceDir);
		policy.setMemo(memo);

		try {
			ServiceImplFactory.getPolicyService().addPolicy(policy);
			MainUI.prtln("Succeed.");
		} catch (Exception e) {
			MainUI.prtln("Failed to create policy, error: " + e.getMessage());
		}
	}

	private void queryAllPolicies() {
		try {
			List<Policy> policies = ServiceImplFactory.getPolicyService()
					.queryAllPolicies();

			ConsoleTable table = new ConsoleTable(4, true);
			table.appendRow();
			table.appendColum("ID").appendColum("POLICY NAME")
					.appendColum("SOURCE DIR").appendColum("MEMO");
			for (Policy policy : policies) {
				table.appendRow();
				table.appendColum(policy.getId())
						.appendColum(policy.getPolicyName())
						.appendColum(policy.getSourceDir())
						.appendColum(policy.getMemo());
			}
			MainUI.prtln(table.toString());
		} catch (Exception e) {
			MainUI.prtln("Failed to query policy, error: " + e.getMessage());
		}
	}
}
