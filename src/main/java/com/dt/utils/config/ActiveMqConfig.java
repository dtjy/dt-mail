package com.dt.utils.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.Topic;

/**
 * @Author jiangyao
 * @Date 2020/4/17 22:32
 **/
@Configuration
public class ActiveMqConfig {

    public static final String SIMPLE_STOCK_MQ_TOPIC = "SIMPLE_STOCK";

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("admin", "admin", "tcp://123.57.53.11:61616");
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setPubSubDomain(true);
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }
//    @Bean
//    public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ActiveMQConnectionFactory connectionFactory) {
//        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
//        bean.setConnectionFactory(connectionFactory);
//        return bean;
//    }

    @Bean
    public JmsMessagingTemplate jmsMessagingTemplate(ActiveMQConnectionFactory connectionFactory){
        return new JmsMessagingTemplate(connectionFactory);
    }

    @Bean(name = "simpleStockTopic")
    public Topic topic(){
        return new ActiveMQTopic(SIMPLE_STOCK_MQ_TOPIC);
    }


}
