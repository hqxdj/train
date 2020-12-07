package com.macro.mall.tiny.config;

import com.google.common.collect.Maps;
import com.macro.mall.tiny.common.constant.RabbitMqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 消息队列配置
 *
 * @author macro
 * @date 2018/9/14
 */
@Configuration
@Slf4j
public class RabbitMqConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    /**
     * 单一消费者
     *
     * @return
     */

    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        factory.setBatchSize(1);
        return factory;
    }


    /**
     * 多个消费者
     *
     * @return
     */
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.configure(factory, connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(20);
        factory.setMaxConcurrentConsumers(20);
        factory.setPrefetchCount(20);
        return factory;
    }

    /**
     * 模板的初始化配置
     *
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, success, cause) -> {
            if (success) {
                log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, success, cause);
            }

        });
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> log.warn("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message));
        return rabbitTemplate;
    }


    //消息的创建设计三个步骤:队列的创建,交换机创建(direct类型,topic类型,fanout类型),队列和交换机的通过路由键的绑定


    //--------- 下单消息配置
    //队列
    @Bean
    public Queue shopOrderCreateQueue() {
        return new Queue(RabbitMqConstant.SHOP_ORDER_CREATE_QUEUE, true);
    }

    //Direct交换机(一对一关系,一个direct交换机只能绑定一个队列,当有2个相同消费者时,如项目部署2台机,只有一个消费者能消费,)
    @Bean
    DirectExchange shopOrderCreateExchange() {
        return new DirectExchange(RabbitMqConstant.SHOP_ORDER_CREATE_EXCHANGE);
    }

    //绑定
    @Bean
    Binding bindShopOrderCreateQueue() {
        return BindingBuilder.bind(shopOrderCreateQueue()).to(shopOrderCreateExchange()).with(RabbitMqConstant.SHOP_ORDER_CREATE_ROUTE);
    }

    //在RabbitMqConfig新增topic队列的基本信息
//-------------------------下单TOPIC消息的创建

    //创建TOPIC交换机
    @Bean
    TopicExchange shopOrderCreateTopicExchange() {
        return new TopicExchange(RabbitMqConstant.SHOP_ORDER_CREATE_TOPIC_EXCHANGE);
    }

    //---------------------------//队列1使用自己的route和交换机绑定
    //创建队列1
    @Bean
    public Queue shopOrderCreateQueueOne() {
        return new Queue(RabbitMqConstant.SHOP_ORDER_CREATE_TOPIC_QUEUE_ONE, true);
    }

    //绑定
    @Bean
    Binding bindShopOrderCreateQueueOne() {
        return BindingBuilder.bind(shopOrderCreateQueueOne()).to(shopOrderCreateTopicExchange()).with(RabbitMqConstant.SHOP_ORDER_CREATE_TOPIC_ROUTE_ONE);
    }

    //---------------------------//队列2用自己的route和交换机绑定

    //创建队列2
    @Bean
    public Queue shopOrderCreateQueueTWO() {
        return new Queue(RabbitMqConstant.SHOP_ORDER_CREATE_TOPIC_QUEUE_TWO, true);
    }

    //绑定
    @Bean
    Binding bindShopOrderCreateQueueTWO() {
        return BindingBuilder.bind(shopOrderCreateQueueTWO()).to(shopOrderCreateTopicExchange()).with(RabbitMqConstant.SHOP_ORDER_CREATE_TOPIC_ROUTE_TWO);
    }

//在RabbitMqConfig加上
    //----------------------- 延时队列的配置

    //延时队列
    @Bean
    public Queue shopOrderCreateDelayQueue() {
        Map<String, Object> argsMap= Maps.newHashMap();
        //真正的交换机
        argsMap.put("x-dead-letter-exchange",RabbitMqConstant.SHOP_ORDER_CREATE_REAL_EXCHANGE);
        //真正的路由键
        argsMap.put("x-dead-letter-routing-key",RabbitMqConstant.SHOP_ORDER_CREATE_REAL_ROUTE);
        return new Queue(RabbitMqConstant.SHOP_ORDER_CREATE_DELAY_QUEUE,true,false,false,argsMap);

    }
    //延时交换机
    @Bean
    DirectExchange shopOrderCreateDelayExchange() {
        return new DirectExchange(RabbitMqConstant.SHOP_ORDER_CREATE_DELAY_EXCHANGE);
    }

    //延时队列绑定延时交换机
    @Bean
    Binding bindShopOrderCreateDelayQueue() {
        return BindingBuilder.bind(shopOrderCreateDelayQueue()).to(shopOrderCreateDelayExchange()).with(RabbitMqConstant.SHOP_ORDER_CREATE_DELAY_ROUTE);
    }


    //真正的队列配置-------------------------------------


    //真正的队列
    @Bean
    public Queue shopOrderCreateRealQueue() {

        return new Queue(RabbitMqConstant.SHOP_ORDER_CREATE_REAL_QUEUE,true);

    }
    //真正的交换机
    @Bean
    DirectExchange shopOrderCreateRealExchange() {
        return new DirectExchange(RabbitMqConstant.SHOP_ORDER_CREATE_REAL_EXCHANGE);
    }

    //绑定真正的交换机
    @Bean
    Binding bindShopOrderCreateRealQueue() {
        return BindingBuilder.bind(shopOrderCreateRealQueue()).to(shopOrderCreateRealExchange()).with(RabbitMqConstant.SHOP_ORDER_CREATE_REAL_ROUTE);
    }

}


