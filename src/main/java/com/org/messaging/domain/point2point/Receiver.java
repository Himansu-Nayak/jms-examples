package com.org.messaging.domain.point2point;

import java.util.Properties;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Receiver {
    private Context context = null;
    private QueueConnectionFactory factory = null;
    private QueueConnection connection = null;
    private Queue queue = null;
    private QueueSession session = null;
    private QueueReceiver receiver = null;

    public Receiver() {

    }

    public static void main(String[] args) {
        Receiver secondClient = new Receiver();
        secondClient.receiveObjectMessage();
    }

    public void receiveObjectMessage() {
        Properties initialProperties = new Properties();
        initialProperties.put(InitialContext.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        initialProperties.put(InitialContext.PROVIDER_URL, "tcp://localhost:3035");
        try {
            context = new InitialContext(initialProperties);
            factory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
            queue = (Queue) context.lookup("queue1");
            connection = factory.createQueueConnection();
            session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            receiver = session.createReceiver(queue);
            connection.start();
            javax.jms.Message message = receiver.receive();
            if (message instanceof ObjectMessage) {
                Object object = ((ObjectMessage) message).getObject();
                System.out.println(this.getClass().getName() + " has received a message : " + (Message) object);
            }

        } catch (NamingException e) {

            e.printStackTrace();
        } catch (JMSException e) {

            e.printStackTrace();
        }
        if (context != null) {
            try {
                context.close();
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
        }

    }

}
