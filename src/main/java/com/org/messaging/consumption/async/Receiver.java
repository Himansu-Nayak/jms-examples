package com.org.messaging.consumption.async;

import java.util.Properties;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Receiver implements MessageListener {
    Thread idleThread = null;
    private Context context = null;
    private TopicConnectionFactory factory = null;
    private TopicConnection connection = null;
    private TopicSession session = null;
    private Topic topic = null;
    private TopicSubscriber subscriber = null;

    public Receiver() {
    }

    public static void main(String... args) {
        Receiver client = new Receiver();
        client.subscribeMessage();
    }

    public void subscribeMessage() {
        Properties initialProperties = new Properties();
        initialProperties.put(InitialContext.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        initialProperties.put(InitialContext.PROVIDER_URL, "tcp://localhost:3035");
        try {
            context = new InitialContext(initialProperties);
            factory = (TopicConnectionFactory) context.lookup("ConnectionFactory");
            topic = (Topic) context.lookup("topic1");
            connection = factory.createTopicConnection();
            session = connection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
            subscriber = session.createSubscriber(topic);
            subscriber.setMessageListener(this);
            connection.start();
            Runnable idleRunnable = new Runnable() {
                public void run() {
                    while (true) {
                        // stay here idle..the program should not exit till the
                        // response receives..Thats why...
                    }
                }
            };
            idleThread = new Thread(idleRunnable, "idleThread");
            idleThread.start();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage text = (TextMessage) message;
            try {
                System.out.println("Message received is : " + text.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Going to exit...");
        System.exit(-1);
    }
}