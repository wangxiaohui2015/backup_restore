package com.my.br.dao;

import java.util.List;

import com.my.br.entity.Policy;

public interface IPolicyDao {
	int addPolicy(Policy policy) throws Exception;

	List<Policy> queryAllPolicies() throws Exception;

	Policy findPolicyById(int policyId) throws Exception;
}
