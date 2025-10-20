package po.dao.impl;

import po.Admin;
import po.dao.AdminDao;
import util.DBUtil;
import java.sql.*;

import exception.DataNotFoundException;

public class AdminDaoImpl implements AdminDao {
	
	private final Connection conn = DBUtil.getConnection();
 @Override
 public Admin findByAccount(String account) throws DataNotFoundException{
     String sql = "SELECT * FROM admin WHERE account = ?";
     try (PreparedStatement ps = conn.prepareStatement(sql)) {
         ps.setString(1, account);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
             return new Admin(
                     rs.getString("adminid"),
                     rs.getString("account"),
                     rs.getString("password")
             );
         }
     } catch (SQLException e) {
		// TODO Auto-generated catch block
    	 throw new DataNotFoundException("資料庫查詢錯誤：" + e.getMessage());
	}
     return null;
 }
}
