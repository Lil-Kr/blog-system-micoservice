package org.cy.micoservice.blog.framework.rocketmq.starter.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/8/3
 * @Description: rocketmq生产者配置
 */
public class ProducerConfig {

  @Autowired
  private RocketMQProducerProperties rocketMQProducerProperties;

  @Autowired
  private MQProducer mqProducer;

  @Bean
  public MQProducer mqProducer() {
    ThreadPoolExecutor asyncThreadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, 100, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000), new ThreadFactory() {
      @Override
      public Thread newThread(Runnable r) {
        return new Thread(r, "rocketmq-async-thread-" + new Random().ints().toString());
      }
    });
    DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
    defaultMQProducer.setNamesrvAddr(rocketMQProducerProperties.getNameserver());
    defaultMQProducer.setProducerGroup(rocketMQProducerProperties.getGroup());
    defaultMQProducer.setRetryTimesWhenSendFailed(rocketMQProducerProperties.getRetryTimes());
    defaultMQProducer.setRetryTimesWhenSendAsyncFailed(rocketMQProducerProperties.getRetryTimes());
    defaultMQProducer.setRetryAnotherBrokerWhenNotStoreOK(true);
    defaultMQProducer.setAsyncSenderExecutor(asyncThreadPool);
    try {
      defaultMQProducer.start();
      System.out.println("=============== mq生产者启动成功, namesrv is " + rocketMQProducerProperties.getNameserver() + " ==================");
    } catch (MQClientException e) {
      throw new RuntimeException(e);
    }
    return defaultMQProducer;
  }

  @Bean
  public RocketMQProducerClient rocketMQProducerClient(MQProducer mqProducer) {
    return new RocketMQProducerClient(mqProducer);
  }
}
