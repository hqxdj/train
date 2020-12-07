package com.macro.mall.tiny.component;

import com.alibaba.fastjson.JSON;
import com.macro.mall.tiny.common.constant.RabbitMqConstant;
import com.macro.mall.tiny.dto.OrderParam;
import com.macro.mall.tiny.dto.QueueEnum;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 取消订单消息的发出者
 *
 * @author macro
 * @date 2018/9/14
 */
@Component
public class CancelOrderSender {
    private static Logger LOGGER =LoggerFactory.getLogger(CancelOrderSender.class);
    @Resource
    private AmqpTemplate amqpTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(Long orderId,final long delayTimes){
//        //给延迟队列发送消息
//        amqpTemplate.convertAndSend(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(), QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey(), orderId, new MessagePostProcessor() {
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                //给消息设置延迟毫秒值
//                message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
//                return message;
//            }
//        });
//        LOGGER.info("send delay message orderId:{}",orderId);
    }

    public void sendMessage() {
//        String message = "hello rabbitmq";
//        amqpTemplate.convertAndSend(QueueEnum.QUEUE_ORDER_CANCEL.getExchange(),QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey(),message);
//        rabbitTemplate.convertAndSend(QueueEnum.QUEUE_ORDER.getExchange(),QueueEnum.QUEUE_ORDER.getRouteKey(),message);
    }

    public void sendCreateOrderMessage(OrderParam orderMast){
        //该参数可以传,可以不传,不传时,correlationData的id值默认是null,消息发送成功后,在RabbitMqConfig类的rabbitTemplate类的confirm方法会接收到该值
        CorrelationData correlationData=new CorrelationData();
        correlationData.setId(String.valueOf(orderMast.getCouponId()));
        String msg = JSON.toJSONString(orderMast);
        //convertAndSend该方法有非常多的重构方法,找到适合自己的业务方法就行了,这里我用的是其中一个,发送时指定exchange和route值,这样就会发到对应的队列去了
        rabbitTemplate.convertAndSend(RabbitMqConstant.SHOP_ORDER_CREATE_EXCHANGE,RabbitMqConstant.SHOP_ORDER_CREATE_ROUTE,msg,correlationData);


    }

    @XxlJob("createOrderJob")
    public ReturnT<String> sendCreateOrderMessageByTopic(String orderMast){
        //该参数可以传,可以不传,不传时,correlationData的id值默认是null,消息发送成功后,在RabbitMqConfig类的rabbitTemplate类的confirm方法会接收到该值
        CorrelationData correlationData=new CorrelationData();
        //convertAndSend该方法有非常多的重构方法,找到适合自己的业务方法就行了,这里我用的是其中一个,发送时指定exchange和route值,这样就会发到对应的队列去了
        rabbitTemplate.convertAndSend(RabbitMqConstant.SHOP_ORDER_CREATE_TOPIC_EXCHANGE,RabbitMqConstant.SHOP_ORDER_CREATE_TOPIC_TOUTE,orderMast,correlationData);

        return ReturnT.SUCCESS;
    }

    //在消息发送类(ShopMessagePublisher)新增
    //发送延时消息
    public void sendCreateOrderDelayMessage(OrderParam orderMast){
        //该参数可以传,可以不传,不传时,correlationData的id值默认是null,消息发送成功后,在RabbitMqConfig类的rabbitTemplate类的confirm方法会接收到该值
        CorrelationData correlationData=new CorrelationData();
        correlationData.setId(String.valueOf(orderMast.getCouponId()));
        String msg = JSON.toJSONString(orderMast);
        // convertAndSend(String exchange, String routingKey, Object message, MessagePostProcessor messagePostProcessor, @Nullable CorrelationData correlationData)
        rabbitTemplate.convertAndSend(RabbitMqConstant.SHOP_ORDER_CREATE_DELAY_EXCHANGE, RabbitMqConstant.SHOP_ORDER_CREATE_DELAY_ROUTE, msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                MessageProperties messageProperties = message.getMessageProperties();
                messageProperties.setExpiration("10000");//单位是毫秒
                return message;
            }
        }, correlationData);

    }


}
