package po.dao.impl;

import po.Member;
import po.dao.MemberDao;
import util.DBUtil;
import exception.DataNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDaoImpl implements MemberDao {

    private final Connection conn = DBUtil.getConnection();

    @Override
    public void register(Member member) throws Exception {
        String sql = "INSERT INTO member(memberid, name, gmail, password, address, balance) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, member.getMemberId());
            ps.setString(2, member.getName());
            ps.setString(3, member.getGmail());
            ps.setString(4, member.getPassword());
            ps.setString(5, member.getAddress());
            ps.setInt(6, member.getBalance());
            ps.executeUpdate();
        }
    }

    @Override
    public Member login(String gmail, String password) throws DataNotFoundException {
        String sql = "SELECT * FROM member WHERE gmail=? AND password=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gmail);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Member m = new Member();
                m.setMemberId(rs.getString("memberid"));
                m.setName(rs.getString("name"));
                m.setGmail(rs.getString("gmail"));
                m.setPassword(rs.getString("password"));
                m.setAddress(rs.getString("address"));
                m.setBalance(rs.getInt("balance"));
                return m;
            }
        } catch (SQLException e) {
            throw new DataNotFoundException("資料庫查詢錯誤：" + e.getMessage());
        }
        throw new DataNotFoundException("帳號或密碼錯誤");
    }

    @Override
    public List<Member> findAll() throws Exception {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM member";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Member m = new Member();
                m.setMemberId(rs.getString("memberid"));
                m.setName(rs.getString("name"));
                list.add(m);
            }
        }
        return list;
    }
    
    @Override
    public String getLastMemberId() throws Exception {
        String sql = "SELECT memberid FROM member ORDER BY memberid DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("memberid");
            }
        }
        return null; // 若沒有資料
    }


    @Override
    public Member findByGmail(String gmail) throws DataNotFoundException {
        String sql = "SELECT * FROM member WHERE gmail=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gmail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Member m = new Member();
                m.setMemberId(rs.getString("memberid"));
                m.setName(rs.getString("name"));
                m.setGmail(rs.getString("gmail"));
                m.setAddress(rs.getString("address"));
                m.setBalance(rs.getInt("balance"));
                return m;
            }
        } catch (SQLException e) {
            throw new DataNotFoundException("資料庫查詢錯誤：" + e.getMessage());
        }
        throw new DataNotFoundException("找不到此 Gmail");
    }
    @Override
    public void updateMember(Member member) throws Exception {
        String sql = "UPDATE member SET name=?, gmail=?, password=?, address=?, balance=? WHERE memberid=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, member.getName());
            ps.setString(2, member.getGmail());
            ps.setString(3, member.getPassword());
            ps.setString(4, member.getAddress());
            ps.setInt(5, member.getBalance());
            ps.setString(6, member.getMemberId());
            ps.executeUpdate();
        }
    }
    
    @Override
    public Member getMemberById(String memberId) throws Exception {
        String sql = "SELECT memberid, name, gmail, balance FROM member WHERE memberid = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Member m = new Member();
                    m.setMemberId(rs.getString("memberid"));
                    m.setName(rs.getString("name"));
                    m.setGmail(rs.getString("gmail"));
                    m.setBalance(rs.getInt("balance"));
                    return m;
                } else {
                    return null; // 找不到會員
                }
            }
        }
    }
}
