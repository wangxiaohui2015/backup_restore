package com.my.br.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.my.br.db.DBHelper;
import com.my.br.entity.Policy;

public class PolicyDaoImpl implements IPolicyDao {

	@Override
	public int addPolicy(Policy policy) throws Exception {
		try (Connection con = DBHelper.getInstance().getConnection();
				PreparedStatement pst = con
						.prepareStatement(
								"INSERT INTO policy(policy_name, source_dir, memo) VALUES(?,?,?);",
								Statement.RETURN_GENERATED_KEYS);) {
			pst.setString(1, policy.getPolicyName());
			pst.setString(2, policy.getSourceDir());
			pst.setString(3, policy.getMemo());
			int result = pst.executeUpdate();
			if (result == 1) {
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					policy.setId(id);
					rs.close();
				}
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<Policy> queryAllPolicies() throws Exception {
		List<Policy> policies = new ArrayList<Policy>();
		try (Connection con = DBHelper.getInstance().getConnection();
				PreparedStatement pst = con
						.prepareStatement("SELECT * FROM policy;")) {
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Policy policy = new Policy();
				policy.setId(rs.getInt(1));
				policy.setPolicyName(rs.getString(2));
				policy.setSourceDir(rs.getString(3));
				policy.setMemo(rs.getString(4));
				policies.add(policy);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return policies;
	}

	@Override
	public Policy findPolicyById(int policyId) throws Exception {
		try (Connection con = DBHelper.getInstance().getConnection();
				PreparedStatement pst = con
						.prepareStatement("SELECT * FROM policy where id=?;")) {
			pst.setInt(1, policyId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				Policy policy = new Policy();
				policy.setId(rs.getInt(1));
				policy.setPolicyName(rs.getString(2));
				policy.setSourceDir(rs.getString(3));
				policy.setMemo(rs.getString(4));
				return policy;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
