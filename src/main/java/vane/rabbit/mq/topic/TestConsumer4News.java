package vane.rabbit.mq.topic;

import com.rabbitmq.client.*;
import vane.rabbit.mq.util.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static vane.rabbit.mq.topic.TestRabbitMQInfo.*;

public class TestConsumer4News {

  public static void main(String[] args) throws IOException, TimeoutException {
    // 当前消费者名称
    String name = "consumer-news";
    // 判断服务器是否启动
    RabbitMQUtil.checkServer();
    // 创建连接工厂
    ConnectionFactory connFactory = new ConnectionFactory();
    // 设置RabbitMQ地址
    connFactory.setHost(HOST);
    // 创建一个新的连接
    Connection conn = connFactory.newConnection();
    // 创建一个通道
    Channel channel = conn.createChannel();
    // 交换机声明（交换机名称；交换机类型）
    channel.exchangeDeclare(EXCHANGE_NAME, TYPE);
    // 获取一个临时队列
    String queueName = channel.queueDeclare().getQueue();
    // 接受 news 信息
    channel.queueBind(queueName, EXCHANGE_NAME, S2_NEWS);
    System.out.println(name + "等待接受消息");
    // DefaultConsumer类实现了Consumer接口，通过传入一个频道，告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
    Consumer consumer =
        new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(
              String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
              throws IOException {
            String message = new String(body, CHARSET_NAME);
            System.out.println(name + " 接收到消息 '" + message + "'");
          }
        };
    // 自动回复队列应答 - RabbitMQ 中的消息确认机制
    channel.basicConsume(queueName, true, consumer);
  }
}
