package vane.rabbit.mq.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestProducer {

  public static final String HOST = "localhost";
  public static final String EXCHANGE_NAME = "fanout_exchange";
  public static final String EXCHANGE_TYPE = "fanout";
  public static final String CHARSET_NAME = "UTF-8";

  public static void main(String[] args) throws IOException, TimeoutException {
    RabbitMQUtil.checkServer();
    // 创建连接工厂
    ConnectionFactory connFactory = new ConnectionFactory();
    // 设置 RabbitMQ 相关信息
    connFactory.setHost(HOST);
    // 创建新的连接
    Connection conn = connFactory.newConnection();
    // 创建通道
    Channel channel = conn.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
    for (int i = 0; i < 100; i++) {
      String message = "direct 消息 " + i;
      // 发送消息到队列中
      channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(CHARSET_NAME));
      System.out.println("发送消息 : " + message);
    }
    // 关闭通道和连接
    channel.close();
    conn.close();
  }
}
