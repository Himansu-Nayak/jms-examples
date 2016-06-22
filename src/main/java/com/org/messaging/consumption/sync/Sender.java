package com.org.messaging.consumption.sync;

import java.util.Properties;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Sender {
    Context context = null;
    ConnectionFactory factory = null;
    Connection connection = null;
    Destination destination = null;
    Session session = null;
    MessageProducer producer = null;

    public Sender() {

    }

    public static void main(String... args) {
        Sender firstClient = new Sender();
        firstClient.sendMessage();
    }

    public void sendMessage() {
        Properties initialProperties = new Properties();
        initialProperties.put(InitialContext.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        initialProperties.put(InitialContext.PROVIDER_URL, "tcp://localhost:3035");

        try {

            context = new InitialContext(initialProperties);
            factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            destination = (Destination) context.lookup("queue1");
            connection = factory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(destination);
            connection.start();
            TextMessage message = session.createTextMessage();
            message.setText("Hello ...This is a sample message");
            producer.send(message);
            System.out.println("Sent: " + message.getText());

        } catch (JMSException ex) {
            ex.printStackTrace();
        } catch (NamingException ex) {
            ex.printStackTrace();
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
