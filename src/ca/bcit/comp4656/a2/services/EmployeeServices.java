package ca.bcit.comp4656.a2.services;

import java.util.List;

import javax.ejb.Local;

import ca.bcit.comp4656.a2.datamodels.Employee;

@Local
public interface EmployeeServices {
	public List<Employee> getEmployees();
	
	public Employee findEmployee(String id);
	
	public int addEmployee(Employee emp);
	
	public int deleteEmployee(String id);
	
	public int updateEmployee(Employee emp);

}
