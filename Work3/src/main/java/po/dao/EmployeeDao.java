package po.dao;

import po.Employee;
import java.util.List;

public interface EmployeeDao {
 List<Employee> getAllEmployees() throws Exception;
 void addEmployee(Employee employee) throws Exception;
 void updateEmployee(Employee employee) throws Exception;
 void deleteEmployee(String employeeid) throws Exception;
 Employee findById(String employeeid) throws Exception;
}

