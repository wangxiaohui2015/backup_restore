package com.my.br.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.my.br.db.DBHelper;
import com.my.br.entity.MetaDataCatalog;

public class MetaDataCatalogDaoImpl implements IMetaDataCatalogDao {

	@Override
	public int addMetaDataCatalog(MetaDataCatalog metaDataCatalog)
			throws Exception {
		try (Connection con = DBHelper.getInstance().getConnection();
				PreparedStatement pst = con
						.prepareStatement(
								"INSERT INTO meta_data_catalog(meta_data_ids, parent_id) VALUES(?,?);",
								Statement.RETURN_GENERATED_KEYS);) {
			pst.setString(1, metaDataCatalog.getMetaDataIds());
			pst.setInt(2, metaDataCatalog.getParentId());
			int result = pst.executeUpdate();
			if (result == 1) {
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					metaDataCatalog.setId(id);
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
	public MetaDataCatalog findMetaDataCatalogById(int id) throws Exception {
		try (Connection con = DBHelper.getInstance().getConnection();
				PreparedStatement pst = con
						.prepareStatement("SELECT * FROM meta_data_catalog WHERE id=?");) {
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				MetaDataCatalog metaDataCatalog = new MetaDataCatalog();
				metaDataCatalog.setId(rs.getInt(1));
				metaDataCatalog.setMetaDataIds(rs.getString(2));
				metaDataCatalog.setParentId(rs.getInt(3));

				rs.close();
				return metaDataCatalog;
			}
		}
		return null;
	}

	@Override
	public MetaDataCatalog findMetaDataCatalogByParentId(int parentId)
			throws Exception {
		try (Connection con = DBHelper.getInstance().getConnection();
				PreparedStatement pst = con
						.prepareStatement("SELECT * FROM meta_data_catalog WHERE parent_id=?");) {
			pst.setInt(1, parentId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				MetaDataCatalog metaDataCatalog = new MetaDataCatalog();
				metaDataCatalog.setId(rs.getInt(1));
				metaDataCatalog.setMetaDataIds(rs.getString(2));
				metaDataCatalog.setParentId(rs.getInt(3));

				rs.close();
				return metaDataCatalog;
			}
		}
		return null;
	}
}
