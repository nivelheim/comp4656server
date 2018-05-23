package ca.bcit.comp4656.a2.webservices;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.NamingException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ca.bcit.comp4656.a2.datamodels.Employee;
import ca.bcit.comp4656.a2.datamodels.ResponseCode;
import ca.bcit.comp4656.a2.jms.ConnectionUtility;
import ca.bcit.comp4656.a2.services.EmployeeServices;

@Path("/employee/")
@ManagedBean
public class EmployeeController {
	@Context
	private UriInfo uriInfo;
	
	@EJB(beanName = "EmployeeServicesBean")
	EmployeeServices empServ;

	private Properties prop;
	private Pattern pattern1;
	private Pattern pattern2;
	private Matcher matcher;
	
	private ConnectionUtility reqUtil;
	private QueueSender sender;
	
	private final String ID_INPUT_PATTERN = "[A][0][0-9]{7}";
	private final String DATE_INPUT_PATTERN = "([0-9]{4})\\/(0[1-9]|1[012])\\/(0[1-9]|[12][0-9]|3[01])";
	
	public EmployeeController() {
		prop= new Properties();
		try {
			prop.load(EmployeeController.class.getResourceAsStream("/msg.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		pattern1 = Pattern.compile(ID_INPUT_PATTERN);
		pattern2 = Pattern.compile(DATE_INPUT_PATTERN);
		
	}
	
	@GET
	@Path("/employees")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Employee> getEmployees() {
		List<Employee> emps = new ArrayList<Employee>();
		emps = empServ.getEmployees();
		
		return emps;
	}
	
	//For future purpose of utilizing returned Employee object
	@GET
	@Path("/get/{empId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Employee getEmployee(@PathParam("empId") String id) {	
		Employee emp = new Employee();
		emp = empServ.findEmployee(id);
		return emp;
	}
	
	
	@GET
	@Path("/find/{empId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseCode findEmployee(@PathParam("empId") String id) {
		
			
		
		
		ResponseCode rc = new ResponseCode(); 
		Employee emp = new Employee();
		
		matcher = pattern1.matcher(id);
		
		if (matcher.matches()) {
			emp = empServ.findEmployee(id);
			
			if (emp.getEmployeeId() != null) {
				try {
					reqUtil = new ConnectionUtility("jms/queue/labRequest");
					Queue requestQueue = (Queue) reqUtil.getDestination();
					sender = ((QueueSession) reqUtil.getSession()).createSender(requestQueue);
					sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
					ObjectMessage message = reqUtil.getSession().createObjectMessage();
					message.setObject(emp);
					sender.send(message);
					System.out.println("Message Sent.");
				} catch (NamingException e) {
					e.printStackTrace();
				} catch (JMSException e) {
					e.printStackTrace();
				}
				
				rc.setResponseCode(000);
				rc.setDescription(prop.getProperty("findSuccess"));
				rc.setNote(emp.getFirstName() + " " + emp.getLastName());
				return rc;
			}
			else {
				rc.setResponseCode(801);
				rc.setDescription(prop.getProperty("findError"));
				return rc;
			}
		}
		else {
			rc.setResponseCode(801);
			rc.setDescription(prop.getProperty("findInvalid"));
			return rc;
		}		
	}
	
	//For empty param from /find call
	@GET
	@Path("/find/")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseCode findEmployee() {
		ResponseCode rc = new ResponseCode(); 
		
		rc.setResponseCode(801);
		rc.setDescription(prop.getProperty("findInvalid"));
		return rc;
	}
	
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseCode addEmployee(Employee emp) {
		ResponseCode rc = new ResponseCode();
	
		if (objectValidator(emp) == 1) {
			int result = empServ.addEmployee(emp);
			if (result == 1) {
				rc.setResponseCode(200);
				rc.setDescription(prop.getProperty("insertSuccess"));
				return rc;
			}
			
			else if (result == -1){
				rc.setResponseCode(901);
				rc.setDescription(prop.getProperty("insertError"));
				return rc;
			}
			
			else {
				rc.setResponseCode(902);
				rc.setDescription(prop.getProperty("insertDuplicate"));
				return rc;
			}
		}
		
		else {
			rc.setResponseCode(903);
			rc.setDescription(prop.getProperty("insertInvalid"));
			return rc;
		}
	}
	
	@DELETE
	@Path("/delete/{empId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseCode deleteEmployee(@PathParam("empId") String id) {
		ResponseCode rc = new ResponseCode(); 
		
		matcher = pattern1.matcher(id);
		
		if (matcher.matches()) {
			int result = empServ.deleteEmployee(id);
			if (result == 1) {
				rc.setResponseCode(001);
				rc.setDescription(prop.getProperty("delSuccess"));
				return rc;
			}
			else {
				rc.setResponseCode(902);
				rc.setDescription(prop.getProperty("delError"));
				return rc;
			}
		}
		else {
			rc.setResponseCode(801);
			rc.setDescription(prop.getProperty("delInvalid"));
			return rc;
		}
	}
	
	//For empty param from /delete call
	@DELETE
	@Path("/delete/")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseCode deleteEmployee() {
		ResponseCode rc = new ResponseCode(); 
		
		rc.setResponseCode(801);
		rc.setDescription(prop.getProperty("delInvalid"));
		return rc;
	}
	
	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseCode updateEmployee(Employee emp) {
		ResponseCode rc = new ResponseCode(); 
		
		int result = empServ.updateEmployee(emp);
		
		if (result == 1) {
			rc.setResponseCode(200);
			rc.setDescription(prop.getProperty("updSuccess"));
			return rc;
		}
		
		else {
			rc.setResponseCode(902);
			rc.setDescription(prop.getProperty("updError"));
			return rc;
		}
	}
	
	public int objectValidator(Employee emp) {
		int result = 0;
		
		if (emp.getDob() != null) {
			matcher = pattern1.matcher(emp.getEmployeeId());
			if  (matcher.matches()) {
				DateFormat fmt = new SimpleDateFormat("yyyy/mm/dd", Locale.ENGLISH);
				String date = fmt.format(emp.getDob());
				matcher = pattern2.matcher(date);
				if (matcher.matches()) {
					return 1;
				}
				else {
					return -1;
				}
			}
			
			else {
				return -1;
			}
		}
		
		else {
			matcher = pattern1.matcher(emp.getEmployeeId());
			if  (matcher.matches()) {
				result = 1;
			}
			else { 
				result = -1;
			}
			
			return result;
		}
		
	
	}
	

}
