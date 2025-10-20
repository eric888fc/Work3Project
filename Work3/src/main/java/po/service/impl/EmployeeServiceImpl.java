package po.service.impl;

import java.util.List;

import po.Employee;
import po.dao.EmployeeDao;
import po.dao.impl.EmployeeDaoImpl;
import po.service.EmployeeService;

public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeDao employeeDao = new EmployeeDaoImpl();

    @Override
    public List<Employee> getAllEmployees() throws Exception {
        return employeeDao.getAllEmployees();
    }

    @Override
    public void addEmployee(Employee employee) throws Exception {
        employeeDao.addEmployee(employee);
    }

    @Override
    public void updateEmployee(Employee employee) throws Exception {
        employeeDao.updateEmployee(employee);
    }

    @Override
    public void deleteEmployee(String employeeid) throws Exception {
        employeeDao.deleteEmployee(employeeid);
    }

    @Override
    public Employee getEmployeeById(String employeeid) throws Exception {
        return employeeDao.findById(employeeid);
    }
}
