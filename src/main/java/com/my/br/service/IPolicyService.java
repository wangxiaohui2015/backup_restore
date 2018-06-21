package com.my.br.service;

import java.util.List;

import com.my.br.entity.Policy;

public interface IPolicyService {
	List<Policy> queryAllPolicies() throws Exception;

	void addPolicy(Policy policy) throws Exception;

	Policy findPolicyById(int policyId) throws Exception;
}
