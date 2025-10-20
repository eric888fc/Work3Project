package po.dao.impl;
import po.Employee;
import po.dao.EmployeeDao;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {
	Connection conn = DBUtil.getConnection();
    @Override
    public List<Employee> getAllEmployees() throws Exception {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Employee(
                        rs.getString("employeeid"),
                        rs.getString("name"),
                        rs.getString("image"),
                        rs.getString("area")
                ));
            }
        }
        return list;
    }

    @Override
    public void addEmployee(Employee employee) throws Exception {
        String sql = "INSERT INTO employee (employeeid, name, image, area) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, employee.getEmployeeid());
            ps.setString(2, employee.getName());
            ps.setString(3, employee.getImage());
            ps.setString(4, employee.getArea());
            ps.executeUpdate();
        }
    }

    @Override
    public void updateEmployee(Employee employee) throws Exception {
        String sql = "UPDATE employee SET name=?, image=?, area=? WHERE employeeid=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getImage());
            ps.setString(3, employee.getArea());
            ps.setString(4, employee.getEmployeeid());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteEmployee(String employeeid) throws Exception {
        String sql = "DELETE FROM employee WHERE employeeid=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, employeeid);
            ps.executeUpdate();
        }
    }

    @Override
    public Employee findById(String employeeid) throws Exception {
        String sql = "SELECT * FROM employee WHERE employeeid=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, employeeid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getString("employeeid"),
                            rs.getString("name"),
                            rs.getString("image"),
                            rs.getString("area")
                    );
                }
            }
        }
        return null;
    }
}
