package com.my.br.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.my.br.db.DBHelper;
import com.my.br.entity.BackupInfo;
import com.my.br.entity.Policy;

public class BackupInfoDaoImpl implements IBackupInfoDao {

	@Override
	public int addBackupInfo(BackupInfo backupInfo) throws Exception {
		try (Connection con = DBHelper.getInstance().getConnection();
				PreparedStatement pst = con
						.prepareStatement(
								"INSERT INTO backup_info(policy_id, backup_type, start_time, end_time, target_dir, is_successful, file_size, dedup_size, dedup_rate) VALUES(?,?,?,?,?,?,?,?,?);",
								Statement.RETURN_GENERATED_KEYS);) {
			pst.setInt(1, backupInfo.getPolicyId());
			pst.setInt(2, backupInfo.getBackupType());
			pst.setTimestamp(3, new Timestamp(backupInfo.getStartTime()
					.getTime()));
			pst.setTimestamp(4,
					new Timestamp(backupInfo.getEndTime().getTime()));
			pst.setString(5, backupInfo.getTargetDir());
			pst.setInt(6, backupInfo.getIsSuccessful());
			pst.setLong(7, backupInfo.getFileSize());
			pst.setLong(8, backupInfo.getDedupSize());
			pst.setDouble(9, backupInfo.getDedupRate());
			int result = pst.executeUpdate();
			if (result == 1) {
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					backupInfo.setId(id);
				}
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<BackupInfo> queryBackupInfos(Policy policy) throws Exception {
		List<BackupInfo> backupInfos = new ArrayList<BackupInfo>();
		try (Connection con = DBHelper.getInstance().getConnection();
				PreparedStatement pst = con
						.prepareStatement("SELECT * FROM backup_info WHERE policy_id = ? ORDER by start_time DESC;")) {
			pst.setInt(1, policy.getId());
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				BackupInfo backupInfo = new BackupInfo();
				backupInfo.setId(rs.getInt("id"));
				backupInfo.setPolicyId(rs.getInt("policy_id"));
				backupInfo.setBackupType(rs.getInt("backup_type"));
				backupInfo.setStartTime(rs.getTimestamp("start_time"));
				backupInfo.setEndTime(rs.getTimestamp("end_time"));
				backupInfo.setTargetDir(rs.getString("target_dir"));
				backupInfo.setIsSuccessful(rs.getInt("is_successful"));
				backupInfo.setFileSize(rs.getLong("file_size"));
				backupInfo.setDedupSize(rs.getLong("dedup_size"));
				backupInfo.setDedupRate(rs.getDouble("dedup_rate"));
				backupInfos.add(backupInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return backupInfos;
	}

	@Override
	public int updateBackupInfo(BackupInfo backupInfo) throws Exception {
		try (Connection con = DBHelper.getInstance().getConnection();
				PreparedStatement pst = con
						.prepareStatement("UPDATE backup_info SET end_time=?, is_successful=?, file_size=?, dedup_size=?, dedup_rate=? WHERE id=?;")) {
			pst.setTimestamp(1,
					new Timestamp(backupInfo.getEndTime().getTime()));
			pst.setInt(2, backupInfo.getIsSuccessful());
			pst.setLong(3, backupInfo.getFileSize());
			pst.setLong(4, backupInfo.getDedupSize());
			pst.setDouble(5, backupInfo.getDedupRate());
			pst.setInt(6, backupInfo.getId());
			int result = pst.executeUpdate();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}