package ca.bcit.comp4656.a2.datamodels;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@Entity
@Table(name="A00918606_Employee")
public class Employee implements Serializable {
	private static final long serialVersionUID = 3656612758888659006L;
	@Id
	@NotNull
	private String employeeId;
	@Column
	@NotNull
	private String firstName;
	@Column
	@NotNull
	private String lastName;
	@Column
	private Date dob;

	public Employee() {
			
	}
	
	public Employee(String id, String firstName, String lastName, Date dob) {
		this.employeeId = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
	}
	
	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

}
