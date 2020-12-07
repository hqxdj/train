package com.macro.mall.tiny.common.constant;

/**
 * @author think
 */
public class RabbitMqConstant {
    //下单发送消息 队列名,交换机名,路由键的配置
    public final static String SHOP_ORDER_CREATE_EXCHANGE = "order.create.exchange";
    public final static String SHOP_ORDER_CREATE_ROUTE = "order.create.route";
    public final static String SHOP_ORDER_CREATE_QUEUE = "order.create.queue";

    //RabbitMqConstant新增topic的配置信息
    //下单topic消息:路由键的名字 星号* 代表多个字符,#号代表一个字符
    //topic交换机,发送消息时,发送到指定shop.order.create.topic.exchange和shop.order.create.topic.route中
    public final static String SHOP_ORDER_CREATE_TOPIC_EXCHANGE = "shop.order.create.topic.exchange";
    public final static String SHOP_ORDER_CREATE_TOPIC_TOUTE = "shop.order.create.topic.route";


    //队列1,通过shop.order.create.topic.*与交换机绑定
    public final static String SHOP_ORDER_CREATE_TOPIC_ROUTE_ONE = "shop.order.create.topic.*";
    public final static String SHOP_ORDER_CREATE_TOPIC_QUEUE_ONE = "shop.order.create.topic.queue.one";


    //队列2 通过shop.order.create.topic.*与交换机绑定shop.order.create.topic.#
    public final static String SHOP_ORDER_CREATE_TOPIC_ROUTE_TWO = "shop.order.create.topic.#";
    public final static String SHOP_ORDER_CREATE_TOPIC_QUEUE_TWO = "shop.order.create.topic.queue.two";


    //在RabbitMqConstant类添加如下内容
    //延时队列,消息先发到延时队列中,到时间后,再发送到真正的队列

    public final static String SHOP_ORDER_CREATE_DELAY_EXCHANGE="shop.order.create.delay.exchange";
    public final static String SHOP_ORDER_CREATE_DELAY_ROUTE="shop.order.create.delay.route";
    public final static String SHOP_ORDER_CREATE_DELAY_QUEUE="shop.order.create.delay.queue";

    //真正的队列

    public final static String SHOP_ORDER_CREATE_REAL_EXCHANGE="shop.order.create.real.exchange";
    public final static String SHOP_ORDER_CREATE_REAL_ROUTE="shop.order.create.real.route";
    public final static String SHOP_ORDER_CREATE_REAL_QUEUE="shop.order.create.real.queue";
}
