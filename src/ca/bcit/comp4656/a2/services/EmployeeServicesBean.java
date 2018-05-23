package ca.bcit.comp4656.a2.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ca.bcit.comp4656.a2.datalayers.EmployeeDao;
import ca.bcit.comp4656.a2.datamodels.Employee;

@Stateless(name="EmployeeServicesBean", mappedName = "EmployeServicesEJB")
public class EmployeeServicesBean implements EmployeeServices {
	@EJB(beanName = "EmployeeDaoBean")
	EmployeeDao empDao;
	
	public EmployeeServicesBean() {
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
		return empDao.updateEmployee(emp);
	}

}
