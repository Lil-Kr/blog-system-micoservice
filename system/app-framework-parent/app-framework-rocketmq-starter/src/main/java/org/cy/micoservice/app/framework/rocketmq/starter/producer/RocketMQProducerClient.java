package org.cy.micoservice.app.framework.rocketmq.starter.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.exception.RequestTimeoutException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.Collection;
import java.util.List;

/**
 * @Author: Lil-K
 * @Date: Created at 2025/8/3
 * @Description: rocketmq 生产者 客户端包装类
 */
public class RocketMQProducerClient  {

  private MQProducer mqProducer;

  public RocketMQProducerClient(MQProducer mqProducer) {
    this.mqProducer = mqProducer;
  }

  public List<MessageQueue> fetchPublishMessageQueues(String topic) throws MQClientException {
    return mqProducer.fetchPublishMessageQueues(topic);
  }

  public SendResult send(Message message) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return mqProducer.send(message);
  }

  public SendResult send(Message msg, long timeout) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return mqProducer.send(msg, timeout);
  }

  public void send(Message msg, SendCallback sendCallback) throws MQClientException, RemotingException, InterruptedException {
    mqProducer.send(msg, sendCallback);
  }

  public void send(Message msg, SendCallback sendCallback, long timeout) throws MQClientException, RemotingException, InterruptedException {
    mqProducer.send(msg,sendCallback,timeout);
  }

  public void sendOneway(Message msg) throws MQClientException, RemotingException, InterruptedException {
    mqProducer.sendOneway(msg);
  }

  public SendResult send(Message msg, MessageQueue mq) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return mqProducer.send(msg, mq);
  }

  public SendResult send(Message msg, MessageQueue mq, long timeout) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return mqProducer.send(msg,mq,timeout);
  }

  public void send(Message msg, MessageQueue mq, SendCallback sendCallback) throws MQClientException, RemotingException, InterruptedException {
    mqProducer.send(msg,mq,sendCallback);
  }

  public void send(Message msg, MessageQueue mq, SendCallback sendCallback, long timeout) throws MQClientException, RemotingException, InterruptedException {
    mqProducer.send(msg,mq,sendCallback,timeout);
  }

  public void sendOneway(Message msg, MessageQueue mq) throws MQClientException, RemotingException, InterruptedException {
    mqProducer.sendOneway(msg,mq);
  }

  public SendResult send(Message msg, MessageQueueSelector selector, Object arg) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return mqProducer.send(msg, selector, arg);
  }

  public SendResult send(Message msg, MessageQueueSelector selector, Object arg, long timeout) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return mqProducer.send(msg, selector, arg, timeout);
  }

  public void send(Message msg, MessageQueueSelector selector, Object arg, SendCallback sendCallback) throws MQClientException, RemotingException, InterruptedException {
    mqProducer.send(msg, selector, arg, sendCallback);
  }

  public void send(Message msg, MessageQueueSelector selector, Object arg, SendCallback sendCallback, long timeout) throws MQClientException, RemotingException, InterruptedException {
    mqProducer.send(msg,selector,arg,sendCallback,timeout);
  }

  public void sendOneway(Message msg, MessageQueueSelector selector, Object arg) throws MQClientException, RemotingException, InterruptedException {
    mqProducer.sendOneway(msg,selector,arg);
  }

  public TransactionSendResult sendMessageInTransaction(Message msg, LocalTransactionExecuter tranExecuter, Object arg) throws MQClientException {
    return mqProducer.sendMessageInTransaction(msg,tranExecuter,arg);
  }

  public TransactionSendResult sendMessageInTransaction(Message msg, Object arg) throws MQClientException {
    return mqProducer.sendMessageInTransaction(msg,arg);
  }

  public SendResult send(Collection<Message> msgs) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return mqProducer.send(msgs);
  }

  public SendResult send(Collection<Message> msgs, long timeout) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return mqProducer.send(msgs,timeout);
  }

  public SendResult send(Collection<Message> msgs, MessageQueue mq) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return mqProducer.send(msgs,mq);
  }

  public SendResult send(Collection<Message> msgs, MessageQueue mq, long timeout) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return mqProducer.send(msgs,mq,timeout);
  }

  public void send(Collection<Message> msgs, SendCallback sendCallback) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    mqProducer.send(msgs,sendCallback);
  }

  public void send(Collection<Message> msgs, SendCallback sendCallback, long timeout) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    mqProducer.send(msgs,sendCallback,timeout);
  }

  public void send(Collection<Message> msgs, MessageQueue mq, SendCallback sendCallback) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    mqProducer.send(msgs,mq,sendCallback);
  }

  public void send(Collection<Message> msgs, MessageQueue mq, SendCallback sendCallback, long timeout) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    mqProducer.send(msgs,mq,sendCallback,timeout);
  }

  public Message request(Message msg, long timeout) throws RequestTimeoutException, MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return mqProducer.request(msg,timeout);
  }

  public void request(Message msg, RequestCallback requestCallback, long timeout) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
    mqProducer.request(msg,requestCallback,timeout);
  }

  public Message request(Message msg, MessageQueueSelector selector, Object arg, long timeout) throws RequestTimeoutException, MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return mqProducer.request(msg,selector,arg,timeout);
  }

  public void request(Message msg, MessageQueueSelector selector, Object arg, RequestCallback requestCallback, long timeout) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
    mqProducer.request(msg,selector,arg,requestCallback,timeout);
  }

  public Message request(Message msg, MessageQueue mq, long timeout) throws RequestTimeoutException, MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return mqProducer.request(msg,mq,timeout);
  }

  public void request(Message msg, MessageQueue mq, RequestCallback requestCallback, long timeout) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    mqProducer.request(msg,mq,requestCallback,timeout);
  }

}
