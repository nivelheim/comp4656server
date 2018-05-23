package ca.bcit.comp4656.a2.jms;

import java.util.Properties;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConnectionUtility {

	private String JBOSS_INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	private String JBOSS_URL_PKG_PREFIXES = "org.jboss.naming:org.jnp.interfaces";
	//private String JBOSS_URL_PKG_PREFIXES = "org.jboss.ejb.client.naming";
	private String PROVIDER_URL = "http-remoting://localhost:8080";
	private String USER_NAME = "comp4656";
	private String PASSWORD = "java";

	private Destination dest;
	private Session session;
	private QueueConnection queueConnection;
	private InitialContext context;

	public QueueConnection getQueueConnection() {
		return queueConnection;
	}

	public ConnectionUtility(String dst) throws NamingException, JMSException {

		/*
		 * 
		 * Object tmp = iniCtx.lookup("jms/RemoteConnectionFactory");
		 * QueueConnectionFactory qcf = (QueueConnectionFactory) tmp; conn =
		 * qcf.createQueueConnection("javaStudent", "java" ); que = (Queue)
		 * iniCtx.lookup("jms/queue/test"); session =
		 * conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE );
		 * QueueReceiver queueReceiver = session.createReceiver(que);
		 * queueReceiver.setMessageListener( this ); conn.start();
		 * 
		 */

		Properties props = new Properties();
		props.setProperty(Context.INITIAL_CONTEXT_FACTORY, JBOSS_INITIAL_CONTEXT_FACTORY);
		props.setProperty(Context.URL_PKG_PREFIXES, JBOSS_URL_PKG_PREFIXES);
		props.setProperty(Context.PROVIDER_URL, PROVIDER_URL);
		props.setProperty(Context.SECURITY_PRINCIPAL, USER_NAME);
		props.setProperty(Context.SECURITY_CREDENTIALS, PASSWORD);
		this.context = new InitialContext(props);

		
		QueueConnectionFactory queueFactory = (QueueConnectionFactory) context.lookup("jms/RemoteConnectionFactory");
		queueConnection = queueFactory.createQueueConnection(USER_NAME, PASSWORD);
		dest = (Queue) context.lookup(dst);
		session = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);

	}

	public void start() throws JMSException {
		if (queueConnection != null) {
			queueConnection.start();
		}
	}

	public Session getSession() {
		return session;
	}

	public Destination getDestination() {
		return dest;
	}

	public void disconnect() throws JMSException {
		if (queueConnection != null) {
			queueConnection.stop();
		}
		if (session != null) {
			session.close();
		}
		if (queueConnection != null) {
			queueConnection.close();
		}
	}

}
