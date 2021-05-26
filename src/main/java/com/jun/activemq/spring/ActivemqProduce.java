package com.jun.activemq.spring;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

/**
 * @author jun
 * @date 2021年05月25日 19:46
 */
@Service
public class ActivemqProduce {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        ActivemqProduce activemqProduce = (ActivemqProduce) ctx.getBean("activemqProduce");
        activemqProduce.jmsTemplate.send(session -> {
            TextMessage textMessage = session.createTextMessage("***spring和ActiveMq整合的case");
            return textMessage;
        });
        System.out.println("send task over");
    }
}
