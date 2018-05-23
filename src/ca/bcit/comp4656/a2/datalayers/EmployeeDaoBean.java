package ca.bcit.comp4656.a2.datalayers;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ca.bcit.comp4656.a2.datamodels.Employee;

@Stateless(name="EmployeeDaoBean", mappedName = "EmployeeDaoEJB")
public class EmployeeDaoBean implements EmployeeDao {
	@PersistenceContext
	EntityManager entityManager;
	
	public EmployeeDaoBean() {
		
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
				//entityManager.getTransaction().begin();
				Employee emp = new Employee();
				emp = e;
				entityManager.persist(emp);
				//entityManager.getTransaction().commit();
				
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
			//entityManager.getTransaction().begin();
			Employee emp = new Employee();
			emp = findEmployee(s);
			if (emp.getFirstName() == null) {
				throw new NoResultException();
			}
			entityManager.remove(emp);
			//entityManager.getTransaction().commit();
			
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
		int status = 0;
		try {
			this.findEmployee(e.getEmployeeId());
			Query qr = entityManager.createQuery("UPDATE Employee e SET e.firstName = :fn, e.lastName = :ln WHERE e.employeeId = :id");
			qr.setParameter("fn", e.getFirstName());
			qr.setParameter("ln", e.getLastName());
			qr.setParameter("id", e.getEmployeeId());
			qr.executeUpdate();
			status = 1;
		} catch (NoResultException ex) {
			ex.printStackTrace();
			status = -1;
		}
		return status;	
	}

}
