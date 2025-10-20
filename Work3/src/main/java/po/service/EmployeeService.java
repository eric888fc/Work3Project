package po.service;

import java.util.List;

import po.Employee;

public interface EmployeeService {
    List<Employee> getAllEmployees() throws Exception;
    void addEmployee(Employee employee) throws Exception;
    void updateEmployee(Employee employee) throws Exception;
    void deleteEmployee(String employeeid) throws Exception;
    Employee getEmployeeById(String employeeid) throws Exception;
}
