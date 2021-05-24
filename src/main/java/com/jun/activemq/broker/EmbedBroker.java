package com.jun.activemq.broker;

import org.apache.activemq.broker.BrokerService;

/**
 * @author jun
 * @date 2021年05月24日 9:58
 */
public class EmbedBroker {
    public static void main(String[] args) throws Exception {
        //ActiveMQ也支持在vm中通信基于嵌入的broker
        BrokerService brokerService = new BrokerService();
        brokerService.setPopulateJMSXUserID(true);
        //绑定地址，等下通过那个地址来连接此broker
        brokerService.addConnector("tcp://127.0.0.1:61616");
        brokerService.start();
    }
}
