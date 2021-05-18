package com.jun.activemq.service;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Scanner;

/**
 * @author jun
 * @date 2021年05月15日 8:35
 */
public class JmsProduceTopic {
    static final Scanner input = new Scanner(System.in);
    private static final String ACTIVEMQ_URL = "tcp://114.116.251.9:61616";
    private static final String TOPIC_NAME = "topic01";

    public static void main(String[] args) throws JMSException {
        //1.创建连接工厂，按照给定的URL，采用默认的用户名密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂,获得connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        //3.创建会话session
        //两个参数transacted=事务,acknowledgeMode=确认模式(签收)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.创建主题
        Topic topic = session.createTopic(TOPIC_NAME);
        //5.创建消息的生产者
        MessageProducer messageProducer = session.createProducer(topic);
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
        //设置持久化之后在启动连接
        connection.start();
        //6.通过使用消息生产者,生产三条消息,发送到MQ的队列里面
        for (; ; ) {
            TextMessage textMessage;
            System.out.println("\n请输入");
            String msgText = input.next();
            if ("exit".equals(msgText)) {
                break;
            }
            if (msgText.startsWith("auto:")) {
                int amount;
                try {
                    amount = Integer.parseInt(msgText.substring(5));
                } catch (NumberFormatException e) {
                    System.out.println("auto指令格式错误，正确的应该是：auto:number");
                    continue;
                }
                System.out.println("请稍等...");
                for (int i = 1; i <= amount; i++) {
                    //7.创建消息
                    textMessage = session.createTextMessage(i + "");//理解为一个字符串
                    //8.通过messageProducer发送给MQ队列
                    messageProducer.send(textMessage);
                }
                System.out.println(msgText + "指令执行完成");
            } else {
                //7.创建消息
                textMessage = session.createTextMessage(msgText);//理解为一个字符串
                //8.通过messageProducer发送给MQ队列
                messageProducer.send(textMessage);
            }
        }

        //9.关闭资源
        messageProducer.close();
        session.close();
        System.out.println("****TOPIC-消息发布到MQ队列完成");
        System.exit(-1);
    }
}
