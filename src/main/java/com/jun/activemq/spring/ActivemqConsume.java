package com.jun.activemq.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * @author jun
 * @date 2021年05月25日 19:46
 */
@Service
public class ActivemqConsume {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        ActivemqConsume consume = (ActivemqConsume) ctx.getBean("activemqConsume");

        String message = (String) consume.jmsTemplate.receiveAndConvert();

        System.out.println("消费者收到的消息：" + message);
    }
}
