package ca.bcit.comp4656.a2.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.naming.NamingException;

import ca.bcit.comp4656.a2.datamodels.Employee;


public class MessageListner implements MessageListener {

	public MessageListner() {
		ConnectionUtility response;
		try {
			response = new ConnectionUtility("jms/queue/labRequest");
			QueueSession session = (QueueSession) response.getSession();
			QueueReceiver receiver = session.createReceiver((Queue) response.getDestination());
			receiver.setMessageListener(this);
			response.start();
		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("START");
		new MessageListner();
		for (int i = 0; i < 100; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onMessage(Message message) {
		try {
			if (message != null && message instanceof ObjectMessage) {
				ObjectMessage om = (ObjectMessage) message;
				Employee emp = new Employee();
				emp = (Employee) om.getObject();
				System.out.println("Received Object = " + emp.getFirstName() + " " + emp.getLastName());
			} else {
				System.err.println("Listner client timed out!");
			}
		} catch (JMSException j) {
			j.printStackTrace();
		}

	}

}
