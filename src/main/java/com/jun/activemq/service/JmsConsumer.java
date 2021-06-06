package com.jun.activemq.service;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author jun
 * @date 2021年05月13日 13:45
 */
public class JmsConsumer {
    private static final String ACTIVEMQ_URL = "nio://114.116.251.9:61608";
    private static final String QUEUE_NAME = "nio-queue";

    public static void main(String[] args) throws JMSException {
        //1.创建连接工厂，按照给定的URL，采用默认的用户名密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        //2.通过连接工厂,获得connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3.创建会话session
        //两个参数transacted=事务,acknowledgeMode=确认模式(签收)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.创建目的地(具体是队列queue还是主题topic)
        Queue queue = session.createQueue(QUEUE_NAME);

        //5.创建消息的消费者,指定消费哪一个队列里面的消息
        MessageConsumer messageConsumer = session.createConsumer(queue);

        //循环获取
        while (true) {
            //6.通过消费者调用方法获取队列里面的消息(发送的消息是什么类型,接收的时候就强转成什么类型)
            TextMessage textMessage = (TextMessage) messageConsumer.receive();
            if (textMessage != null) {
                System.out.println("****消费者接收到TextMessage的消息:  " + textMessage.getText());
                System.out.println("****消费者接收到TextMessage请求头的消息:  " + textMessage.getStringProperty("c01"));
            } else {
                break;
            }
//            MapMessage receive = (MapMessage) messageConsumer.receive();
//            if (receive != null) {
//                System.out.println("****消费者接收到MapMessage的消息:  " + receive.getString("k1"));
//            } else {
//                break;
//            }
        }

        //7.关闭资源
        messageConsumer.close();
        session.close();
        connection.close();
    }
}
