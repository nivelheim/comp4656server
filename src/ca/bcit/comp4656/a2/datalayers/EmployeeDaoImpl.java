package ca.bcit.comp4656.a2.datalayers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ca.bcit.comp4656.a2.datamodels.Employee;

public class EmployeeDaoImpl implements EmployeeDao {
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	
	public EmployeeDaoImpl() {
		this.entityManagerFactory = Persistence.createEntityManagerFactory("EmployeePU");
		this.entityManager = entityManagerFactory.createEntityManager();
	}

	@Override
	public List<Employee> selectAll() {
		List<Employee> emps = new ArrayList<Employee>();
		try {
			emps = entityManager.createQuery("From Employee", Employee.class).getResultList();
			
		} catch (Exception e) {
			System.out.println("Error occured");
		}
		
		return emps;
	}

	@Override
	public Employee findEmployee(String s) {
		try {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Employee> getEmployeeQuery = cb.createQuery(Employee.class);
			Root<Employee> emp = getEmployeeQuery.from(Employee.class);
			getEmployeeQuery.where(cb.equal(emp.get("employeeId"), s));
			TypedQuery<Employee> query = entityManager.createQuery(getEmployeeQuery);
			return query.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			return new Employee();
		}	
	}

	@Override
	public int addEmployee(Employee e) {
		int status = 0;
		if (this.findEmployee(e.getEmployeeId()).getEmployeeId() == null) {
			try {
				entityManager.getTransaction().begin();
				Employee emp = new Employee();
				emp = e;
				entityManager.persist(emp);
				entityManager.getTransaction().commit();
				
				status = 1;
				return status;
			} catch (Exception ex) {
				ex.printStackTrace();
				return -1;
			}
		}
		else {
			return 0;
		}
	}

	@Override
	public int deleteEmployee(String s) {
		int status = 0;
		try {
			entityManager.getTransaction().begin();
			Employee emp = new Employee();
			emp = findEmployee(s);
			if (emp.getFirstName() == null) {
				throw new NoResultException();
			}
			entityManager.remove(emp);
			entityManager.getTransaction().commit();
			
			status = 1;
			System.out.println("-------------DELETE SUCCESSFUL-------------");
			return status;
		} catch (NoResultException ex) {
			ex.printStackTrace();
			status = -1;
			System.out.println("-------------DELETE UNSUCCESSFUL-------------");
			return status;
		}
	}

	@Override
	public int updateEmployee(Employee e) {
		// TODO Auto-generated method stub
		return 0;
	}

}
