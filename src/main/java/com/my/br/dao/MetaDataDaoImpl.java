package com.my.br.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.my.br.db.DBHelper;
import com.my.br.entity.MetaData;

public class MetaDataDaoImpl implements IMetaDataDao {

	@Override
	public List<MetaData> findMetaDataByHash(String hash) throws Exception {
		List<MetaData> metaDatas = new ArrayList<MetaData>();
		try (Connection con = DBHelper.getInstance().getConnection();
				PreparedStatement pst = con
						.prepareStatement("SELECT * FROM meta_data WHERE digest=?;")) {
			pst.setString(1, hash);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				MetaData metaData = new MetaData();
				metaData.setId(rs.getInt(1));
				metaData.setDigest(rs.getString(2));
				metaData.setFilePath(rs.getString(3));
				metaDatas.add(metaData);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return metaDatas;
	}

	@Override
	public MetaData findMetaDataById(int id) throws Exception {
		try (Connection con = DBHelper.getInstance().getConnection();
				PreparedStatement pst = con
						.prepareStatement("SELECT * FROM meta_data WHERE id=?;")) {
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				MetaData metaData = new MetaData();
				metaData.setId(rs.getInt(1));
				metaData.setDigest(rs.getString(2));
				metaData.setFilePath(rs.getString(3));
				rs.close();
				return metaData;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Integer> addMetaDatas(List<MetaData> metaDatas)
			throws Exception {

		List<Integer> results = new ArrayList<Integer>();

		Connection con = DBHelper.getInstance().getConnection();
		PreparedStatement pst = con.prepareStatement(
				"INSERT INTO meta_data(digest, file_path) VALUES(?,?);",
				Statement.RETURN_GENERATED_KEYS);
		con.setAutoCommit(false);

		for (MetaData metaData : metaDatas) {
			pst.setString(1, metaData.getDigest());
			pst.setString(2, metaData.getFilePath());
			pst.addBatch();
		}

		pst.executeBatch();
		con.commit();

		ResultSet rs = pst.getGeneratedKeys();
		while (rs.next()) {
			results.add(rs.getInt(1));
		}
		rs.close();
		pst.close();
		con.close();

		return results;
	}
}
