package com.my.br.service;

import java.io.File;
import java.util.List;

import com.my.br.dao.DaoImplFactory;
import com.my.br.entity.Policy;

public class PolicyServiceImpl implements IPolicyService {

	@Override
	public List<Policy> queryAllPolicies() throws Exception {
		return DaoImplFactory.getPolicyDao().queryAllPolicies();
	}

	@Override
	public void addPolicy(Policy policy) throws Exception {
		File file = new File(policy.getSourceDir());
		if (!file.exists() || file.isFile()) {
			throw new Exception("Source directory is incorrect.");
		} else {
			DaoImplFactory.getPolicyDao().addPolicy(policy);
		}
	}

	@Override
	public Policy findPolicyById(int policyId) throws Exception {
		return DaoImplFactory.getPolicyDao().findPolicyById(policyId);
	}
}
