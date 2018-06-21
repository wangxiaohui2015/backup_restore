package com.my.br.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.my.br.db.DBHelper;

public class TestBatch {
	public static void main(String[] args) {
		try {
			testBatchInsert();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testBatchInsert() throws Exception {
		Connection con = DBHelper.getInstance().getConnection();
		PreparedStatement pst = con
				.prepareStatement(
						"INSERT INTO policy(policy_name, source_dir, memo) VALUES(?,?,?);",
						Statement.RETURN_GENERATED_KEYS);
		con.setAutoCommit(false);

		for (int i = 0; i < 10000; i++) {
			pst.setString(1, "policyName_" + i);
			pst.setString(2, "path_" + i);
			pst.setString(3, "memo_" + i);
			pst.addBatch();
		}

		int[] result = pst.executeBatch();
		con.commit();

		if (result.length > 0) {
			ResultSet rs = pst.getGeneratedKeys();
			while (rs.next()) {
				int id = rs.getInt(1);
				System.out.println(id);
			}
			rs.close();
		}
	}
}
