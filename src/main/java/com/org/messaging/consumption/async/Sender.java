package com.org.messaging.consumption.async;

import java.util.Properties;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Sender {
    private Context context = null;
    private TopicConnectionFactory factory = null;
    private TopicConnection connection = null;
    private TopicSession session = null;
    private Topic topic = null;
    private TopicPublisher publisher = null;

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
            factory = (TopicConnectionFactory) context.lookup("ConnectionFactory");
            topic = (Topic) context.lookup("topic1");
            connection = factory.createTopicConnection();
            session = connection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
            publisher = session.createPublisher(topic);
            TextMessage message = session.createTextMessage();
            message.setText("Hello ...This is a sample message");
            connection.start();
            publisher.publish(message);
            System.out.println("Message has successfully sent...");

        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
        }
        if (context != null) {
            try {
                context.close();
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void closeContext() {

        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
        }
        if (context != null) {
            try {
                context.close();
            } catch (NamingException ex) {
                ex.printStackTrace();
            }
        }
    }
}
