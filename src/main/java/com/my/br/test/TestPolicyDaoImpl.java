package com.my.br.test;

import com.my.br.dao.DaoImplFactory;
import com.my.br.dao.IPolicyDao;
import com.my.br.entity.Policy;

public class TestPolicyDaoImpl {
	public static void main(String[] args) {
		IPolicyDao dao = DaoImplFactory.getPolicyDao();
		Policy policy = new Policy();
		policy.setPolicyName("test_policy");
		policy.setSourceDir("E:\\software\\WPS");
		policy.setMemo("This is my test policy.");
		try {
			int result = dao.addPolicy(policy);
			System.out.println("Result: " + result + ", ID: " + policy.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}