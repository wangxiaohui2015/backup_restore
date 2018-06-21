package com.my.br.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.my.br.db.DBHelper;
import com.my.br.entity.BackupDetails;

public class BackupDetailsDaoImpl implements IBackupDetailsDao {

	@Override
	public int addBackupDetails(BackupDetails backupDetails) throws Exception {
		try (Connection con = DBHelper.getInstance().getConnection();
				PreparedStatement pst = con
						.prepareStatement(
								"INSERT INTO backup_details(backup_info_id, file_name, is_dir, parent_id, meta_data_catalog_root_id) VALUES(?,?,?,?,?);",
								Statement.RETURN_GENERATED_KEYS);) {
			pst.setInt(1, backupDetails.getBackupInfoId());
			pst.setString(2, backupDetails.getFileName());
			pst.setInt(3, backupDetails.isDir() ? 1 : 0);
			pst.setInt(4, backupDetails.getParentId());
			pst.setInt(5, backupDetails.getMetaDataCatalogRootId());
			int result = pst.executeUpdate();
			if (result == 1) {
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					backupDetails.setId(id);
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
	public List<BackupDetails> findBackupDetail(int backupInfoId, int parentId)
			throws Exception {
		List<BackupDetails> results = new ArrayList<BackupDetails>();

		try (Connection con = DBHelper.getInstance().getConnection();
				PreparedStatement pst = con
						.prepareStatement("SELECT * FROM backup_details WHERE backup_info_id=? AND parent_id=?;")) {
			pst.setInt(1, backupInfoId);
			pst.setInt(2, parentId);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				BackupDetails backupDetails = new BackupDetails();
				backupDetails.setId(rs.getInt(1));
				backupDetails.setBackupInfoId(rs.getInt(2));
				backupDetails.setFileName(rs.getString(3));
				backupDetails.setDir(rs.getInt(4) == 1 ? true : false);
				backupDetails.setParentId(rs.getInt(5));
				backupDetails.setMetaDataCatalogRootId(rs.getInt(6));
				results.add(backupDetails);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
}
