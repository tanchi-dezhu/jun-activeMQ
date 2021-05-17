package com.jun.activemq.service;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author jun
 * @date 2021年05月16日 11:05
 */
public class JmsProduceTest {

    private static final String ACTIVEMQ_URL = "tcp://114.116.251.9:61616";
    private static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        MessageProducer messageProducer = session.createProducer(queue);
        for (int i = 0; i < 3; i++) {
            TextMessage textMessage = session.createTextMessage(String.valueOf(i));
            textMessage.setStringProperty("c01","vip");
            messageProducer.send(textMessage);

            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("k1", "mapMessage的值" + i);
            messageProducer.send(mapMessage);
        }

        messageProducer.close();
        session.close();
        System.out.println("****消息发布到MQ队列完成");
        System.exit(-1);
    }
}
