package com.macro.mall.tiny.component;

import com.alibaba.fastjson.JSON;
import com.macro.mall.tiny.common.constant.RabbitMqConstant;
import com.macro.mall.tiny.dto.OrderParam;
import com.macro.mall.tiny.service.OmsPortalOrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 取消订单消息的处理者
 * Created by macro on 2018/9/14.
 */
@Component
@Slf4j
//@RabbitListener(queues = "mall.order.cancel")
public class CancelOrderReceiver {
    private static Logger LOGGER = LoggerFactory.getLogger(CancelOrderReceiver.class);
    @Autowired
    private OmsPortalOrderService portalOrderService;

    @RabbitHandler
    public void handle(Long orderId) {
        LOGGER.info("receive delay message orderId:{}", orderId);
        portalOrderService.cancelOrder(orderId);
    }

    //    @RabbitListener(queues = "mall.order.cancel")
    public void testHandle(String msg) {

        log.info("rabbitmq msg {}", msg);
    }

//    @RabbitListener(queues = RabbitMqConstant.SHOP_ORDER_CREATE_QUEUE)
    public void consumerMessage(String msg, Channel channel, Message message) {
        log.info("rabbitmq msg {}", "开始消费");
        OrderParam orderParam = JSON.parseObject(msg, OrderParam.class);

        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }


    }

    //消息的消费方新增
    //消费者1
    @RabbitListener(queues = RabbitMqConstant.SHOP_ORDER_CREATE_TOPIC_QUEUE_ONE)
    public void createOrderMesaageComsumerOne(String msg, Channel channel, Message message) {
        try {
            //消息可以通过msg获取也可以通过message对象的body值获取
            System.out.println("我是消费者1");
            OrderParam shopOrderMast = JSON.parseObject(msg, OrderParam.class);


            /**
             * 因为我在application.yml那里配置了消息手工确认也就是传说中的ack,所以消息消费后必须发送确认给mq
             * 很多人不理解ack(消息消费确认),以为这个确认是告诉消息发送者的,这个是错的,这个ack是告诉mq服务器,
             * 消息已经被我消费了,你可以删除它了
             * 如果没有发送basicAck的后果是:每次重启服务,你都会接收到该消息
             * 如果你不想用确认机制,就去掉application.yml的acknowledge-mode: manual配置,该配置默认
             * 是自动确认auto,去掉后,下面的channel.basicAck就不用写了
             *
             */
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                //出现异常,告诉mq抛弃该消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }

    //消费者2
    @RabbitListener(queues = RabbitMqConstant.SHOP_ORDER_CREATE_TOPIC_QUEUE_TWO)
    public void createOrderMesaageComsumerTWO(String msg, Channel channel, Message message) {
        try {
            //消息可以通过msg获取也可以通过message对象的body值获取
            System.out.println("我是消费者2");
            OrderParam shopOrderMast = JSON.parseObject(msg, OrderParam.class);


            /**
             * 因为我在application.yml那里配置了消息手工确认也就是传说中的ack,所以消息消费后必须发送确认给mq
             * 很多人不理解ack(消息消费确认),以为这个确认是告诉消息发送者的,这个是错的,这个ack是告诉mq服务器,
             * 消息已经被我消费了,你可以删除它了
             * 如果没有发送basicAck的后果是:每次重启服务,你都会接收到该消息
             * 如果你不想用确认机制,就去掉application.yml的acknowledge-mode: manual配置,该配置默认
             * 是自动确认auto,去掉后,下面的channel.basicAck就不用写了
             *
             */
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            try {
                //出现异常,告诉mq抛弃该消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }

}
