package vane.rabbit.mq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import vane.rabbit.mq.util.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static vane.rabbit.mq.topic.TestRabbitMQInfo.*;

public class TestProducer {

  public static void main(String[] args) throws IOException, TimeoutException {
    RabbitMQUtil.checkServer();
    // 创建连接工厂
    ConnectionFactory connFactory = new ConnectionFactory();
    // 设置RabbitMQ相关信息
    connFactory.setHost(HOST);
    Connection conn = connFactory.newConnection();
    Channel channel = conn.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, TYPE);
    String[] routingKeys =
        new String[] {"CHINA.NEWS", "CHINA.WEATHER", "EUROPE.NEWS", "EUROPE.WEATHER"};
    String[] messages = new String[] {"中国新闻", "中国天气", "欧洲新闻", "欧洲天气"};
    for (int i = 0; i < routingKeys.length; i++) {
      String routingKey = routingKeys[i];
      String message = messages[i];
      channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
      System.out.printf("发送消息到路由：%s, 内容是: %s%n ", routingKey, message);
    }
    // 关闭通道和连接
    channel.close();
    conn.close();
  }
}
