package com.jun.activemq.service;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author jun
 * @date 2021年05月15日 8:34
 */
public class JmsConsumerTopic {
    private static final String ACTIVEMQ_URL = "tcp://114.116.251.9:61616";
    private static final String TOPIC_NAME = "topic01";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("我是一号消费者");
        //1.创建连接工厂，按照给定的URL，采用默认的用户名密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        //2.通过连接工厂,获得connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.setClientID("张三");

        //3.创建会话session
        //两个参数transacted=事务,acknowledgeMode=确认模式(签收)
//        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

        //4.创建目的地(具体是队列queue还是主题topic)
        Topic topic = session.createTopic(TOPIC_NAME);
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "我是张三");

        connection.start();

        //5.创建消息的消费者,指定消费哪一个队列里面的消息
        Message receive = topicSubscriber.receive();

        while (null != receive) {
            TextMessage textMessage = (TextMessage) receive;
            System.out.println("接收到持久化的topic" + textMessage.getText());
            receive = topicSubscriber.receive(1000L);
        }
//        开启事务时必须commit不然不会提交到队列
//        保证事务的一致性可以使用session.rollback();进行回滚
        session.commit();
        session.close();
        connection.close();

        /**
         * 一定要先运行一次消费者,类似于像MQ注册,我订阅了这个主题
         * 然后再运行主题生产者
         * 无论消费着是否在线,都会接收到,在线的立即接收到,不在线的等下次上线把没接收到的接收

         也就是：先启动消费者（A,B），然后启动生产者生产消息。消费者消费之后，关闭消费者B，再生产消息。此时B是离线状态的，A在线，所以A立刻消费了消息。此时启动消费者B，B也可以消费之前的消息。
         */
    }
}
