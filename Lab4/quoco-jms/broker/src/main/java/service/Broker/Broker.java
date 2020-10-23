package service.Broker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import service.core.ClientInfo;
import service.core.Quotation;
import service.message.QuotationResponseMessage;
import service.message.QuotationRequestMessage;

public class Broker {
  //public static int SEED_ID = 0;
  public static HashMap<Long, ClientInfo> cache = new HashMap<Long, ClientInfo>();

  public static void main(String[] args) {
    String host = "localhost";
    if (args.length > 0) {
      host = args[0];
    }

    ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");
    try {
      Connection connection = factory.createConnection();
      connection.setClientID("broker");

      ConsumerThread consumerThread = new ConsumerThread();
      ProducerThread producerThread = new ProducerThread();
      Thread conThread = new Thread(consumerThread);
      Thread proThread = new Thread(producerThread);
      conThread.start();
      proThread.start();
    } catch (JMSException e) {
      System.out.println("Troube: " + e);
    }
  }

  public class ConsumerThread implements Runnable {
    public void run() {
      try {
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue("QUOTATIONS");
        Topic topic = session.createTopic("RESPONSE");
        MessageProducer producer = session.createProducer(topic);
        MessageConsumer consumer = session.createConsumer(queue);
        //MessageProducer rebacker = session.createProducer(queue);
        connection.start();
        while (true) {
          //
          List<Quotation> quotations = new ArrayList<Quotation>();
          //
          Message message = consumer.receive();
          if (message instanceof ObjectMessage) {
            Object content = ((ObjectMessage)message).getObject();
            if (content instanceof QuotationResponseMessage) {
              QuotationResponseMessage response = (QuotationResponseMessage)content;
              //message.acknowledge;
            }
          } else {
            System.out.println("Unknown message type: " + message.getClass().getCanonicalName());
          }
          //
          Thread.sleep(2000);
          Message application = session.createObjectMessage(new ClientApplicationMessage(Id, cache.get(Id), quotations));
          producer.send(application);
        }
      } catch (JMSException e) {
        System.out.println("Trouble: " + e);
      }
    }
  }

  public class ProducerThread implements Runnable {
    public void run() {
      try {
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue("REQUEST");
        Topic topic = session.createTopic("APPLICATIONS");
        MessageProducer producer = session.createProducer(topic);
        //MessageConsumer consumer = session.createConsumer(queue);
        connection.start();

        while (true) {
          Message message;

          if (message instanceof ObjectMessage) {
            Object content = ((ObjectMessage)message).getObject();
            if (content instanceof QuotationRequestMessage) {
              QuotationRequestMessage request = (QuotationRequestMessage)content;
              producer.send(message);
              cache.put(request.id, request.info);
            }
          } else {
            System.out.println("Unknown message type: " + message.getClass().getCanonicalName());
          }
        }
      } catch (JMSException e) {
        System.out.println("Trouble: " + e);
      }
    }
  }
}
