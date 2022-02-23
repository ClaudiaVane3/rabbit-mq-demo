package vane.rabbit.mq.direct;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.*;
import vane.rabbit.mq.util.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestConsumer {

  public static final String HOST = "localhost";
  public static final String QUEUE_NAME = "direct_queue";
  public static final String CHARSET_NAME = "UTF-8";

  public static void main(String[] args) throws IOException, TimeoutException {
    // 为消费者随机取名
    String consumerName = "consumer - " + RandomUtil.randomString(5);
    // 判断服务器是否启动
    RabbitMQUtil.checkServer();
    // 创建连接工厂
    ConnectionFactory connFactory = new ConnectionFactory();
    // 设置 RabbitMQ 地址
    connFactory.setHost(HOST);
    // 创建新连接
    Connection conn = connFactory.newConnection();
    // 创建通道
    Channel channel = conn.createChannel();
    // 声明要关注的队列
    channel.queueDeclare(QUEUE_NAME, false, false, true, null);
    System.out.println(consumerName + " - 等待接受消息");
    // DefaultConsumer 实现了 Consumer 接口
    // 通过传入一个频道，告诉服务器需要哪个频道的信息，如果频道中有信息，就会执行回调函数 handleDelivery
    Consumer consumer =
        new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(
              String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
              throws IOException {
            String message = new String(body, CHARSET_NAME);
            System.out.println(consumerName + " 接收到消息" + message);
          }
        };
    // 自动回复队列应答 - RabbitMQ中的消息确认机制
    channel.basicConsume(QUEUE_NAME, true, consumer);
  }
}
