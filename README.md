# jms-examples
JMS applications begin by looking up a ConnectionFactory instance from JNDI (the Java Naming and Directory Interface), and then using this to create Connection and then Session instances.

The following are JMS elements:

JMS provider
An implementation of the JMS interface for a Message Oriented Middleware (MOM). Providers are implemented as either a Java JMS implementation or an adapter to a non-Java MOM.
JMS client
An application or process that produces and/or receives messages.
JMS producer/publisher
A JMS client that creates and sends messages.
JMS consumer/subscriber
A JMS client that receives messages.
JMS message
An object that contains the data being transferred between JMS clients.
JMS queue
A staging area that contains messages that have been sent and are waiting to be read (by only one consumer). Contrary to what the name queue suggests, messages don't have to be received in the order in which they were sent. A JMS queue only guarantees that each message is processed only once.
JMS topic
A distribution mechanism for publishing messages that are delivered to multiple subscribers.

Few JMS Provider
OpenJMS
ActiveMQ
HornetQ
RabbitMQ