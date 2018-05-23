package ca.bcit.comp4656.a2.services;

import java.util.List;
import ca.bcit.comp4656.a2.datalayers.EmployeeDaoImpl;
import ca.bcit.comp4656.a2.datamodels.Employee;

public class EmployeeServicesImpl implements EmployeeServices {
	private EmployeeDaoImpl empDao;
	
	public EmployeeServicesImpl() {
		empDao = new EmployeeDaoImpl();
	}

	@Override
	public List<Employee> getEmployees() {
		return empDao.selectAll();
	}

	@Override
	public Employee findEmployee(String id) {
		return empDao.findEmployee(id);
	}

	@Override
	public int addEmployee(Employee emp) {
		return empDao.addEmployee(emp);
	}

	@Override
	public int deleteEmployee(String id) {
		return empDao.deleteEmployee(id);
	}

	@Override
	public int updateEmployee(Employee emp) {
		// TODO Auto-generated method stub
		return 0;
	}

}
