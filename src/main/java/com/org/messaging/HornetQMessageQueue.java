package com.org.messaging;

import java.util.HashMap;
import java.util.Map;

import javax.jms.*;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.core.config.impl.FileConfiguration;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;
import org.hornetq.integration.transports.netty.NettyConnectorFactory;
import org.hornetq.integration.transports.netty.TransportConstants;
import org.hornetq.jms.server.JMSServerManager;
import org.hornetq.jms.server.impl.JMSServerManagerImpl;

public class HornetQMessageQueue {
    private static JMSServerManager jmsServerManager = null;

    static void startServer() {
        try {
            FileConfiguration configuration = new FileConfiguration();
            configuration.setConfigurationUrl("hornetq-configuration.xml");
            configuration.start();

            HornetQServer server = HornetQServers.newHornetQServer(configuration);
            jmsServerManager = new JMSServerManagerImpl(server, "hornetq-jms.xml");
            // if you want to use JNDI, simple inject a context here or don't
            // call this method and make sure the JNDI parameters are set.
            jmsServerManager.setContext(null);
            jmsServerManager.start();
            System.out.println("Server started !!");
        } catch (Exception e) {
            System.out.println("Damn it !!");
            e.printStackTrace();
        }
    }

    static void stopServer() throws Exception {
        if (jmsServerManager != null) {
            jmsServerManager.stop();
            System.out.println("Server stop !!");

        }
    }

    static Connection createConnection() throws JMSException {
        // Instantiate the TransportConfiguration object which
        // contains the knowledge of what transport to use,
        // The server port etc.

        Map<String, Object> connectionParams = new HashMap<String, Object>();
        connectionParams.put(TransportConstants.PORT_PROP_NAME, 5445);

        TransportConfiguration transportConfiguration = new TransportConfiguration(
                NettyConnectorFactory.class.getName(), connectionParams);

        // Directly instantiate the JMS ConnectionFactory object
        // using that TransportConfiguration
        ConnectionFactory cf = HornetQJMSClient.createConnectionFactory(transportConfiguration);

        // Create a JMS Connection
        return cf.createConnection();
    }

    static Queue createQueue(String queueName) {
        return HornetQJMSClient.createQueue("testQueue");
    }

    static void sendMessage(Queue queue, String message) throws JMSException {
        Connection connection = null;
        try {
            connection = createConnection();

            // Start the Connection
            connection.start();

            // Create a JMS Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create a JMS Message Producer
            MessageProducer producer = session.createProducer(queue);

            // Create a Text Message
            TextMessage textMessage = session.createTextMessage(message);

            System.out.println("Sent message: " + textMessage.getText());

            // Send the Message
            producer.send(textMessage);

        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    static void receiveMessage(Queue queue) throws JMSException {

        Connection connection = null;
        try {
            connection = createConnection();

            // Start the Connection
            connection.start();

            // Create a JMS Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create a JMS Message Consumer
            MessageConsumer messageConsumer = session.createConsumer(queue);

            TextMessage messageReceived = (TextMessage) messageConsumer.receive(5000);
            System.out.println("Received message: " + messageReceived.getText());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void template() throws JMSException {
        // use configuration queue
        Queue queue = createQueue("testQueue");
        sendMessage(queue, "Hello World!!");
        receiveMessage(queue);

        // use core queue
        // TODO: not working
        Queue coreQueue = createQueue("jms.queue.testCoreQueue");
        sendMessage(coreQueue, "Hi!!");
        receiveMessage(coreQueue);
    }

    public static void main(String... strings) throws Exception {
        startServer();
        template();
        stopServer();
    }

}