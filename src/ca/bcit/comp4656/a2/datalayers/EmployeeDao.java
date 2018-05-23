package ca.bcit.comp4656.a2.datalayers;

import java.util.List;

import javax.ejb.Local;

import ca.bcit.comp4656.a2.datamodels.Employee;

@Local
public interface EmployeeDao {
	public List<Employee> selectAll();
	
	public Employee findEmployee(String s);
	
	public int addEmployee(Employee e);
	
	public int deleteEmployee(String s);
	
	public int updateEmployee(Employee e);
}
