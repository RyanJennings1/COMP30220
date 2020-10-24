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

import service.core.ClientApplicationMessage;
import service.core.ClientInfo;
import service.core.Quotation;
import service.message.QuotationResponseMessage;
import service.message.QuotationRequestMessage;

/**
 * JMS Broker
 * - Handle incoming QuotationRequestMessages
 * - Return ClientApplicationMessage(ClientInfo, Quotations)
 * - Consumer thread runs in a different thread to producer thread
 * - 2 consumers needed:
 *   - handle incoming QuotationRequestMessage from client
       - (create REQUESTS queue)
       - new thread should be created for each request
       - publish request on APPLICATIONS topic
       - sleep for fixed time
       - grab quotations, package as ClientApplicationMessage
       - send to client (who listens on RESPONSES channel)
 *   - handle responses from quotation services
 */
public class Broker {
  private static HashMap<Long, ClientInfo> cache = new HashMap<Long, ClientInfo>();
  private static Connection connection;
  private static List<Quotation> quotes;

  public static void main(String[] args) {
    String host = "localhost";
    if (args.length > 0) {
      host = args[0];
    }

    ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");
    try {
      //Connection connection = factory.createConnection();
      connection = factory.createConnection();
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

  /*
   * Listen for requests on the REQUESTS queue.
   * Send the message to the APPLICATIONS topic
   * where it will be picked up the quotation services
   * who send it onto QUOTATIONS.
   */
  public static class ConsumerThread implements Runnable {
    public void run() {
      try {
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue("REQUESTS");
        Topic topic = session.createTopic("APPLICATIONS");
        MessageProducer producer = session.createProducer(topic);
        MessageConsumer consumer = session.createConsumer(queue);

        connection.start();

        while (true) {
          Message message = consumer.receive();
          if (message instanceof ObjectMessage) {
            Object content = ((ObjectMessage)message).getObject();
            if (content instanceof QuotationRequestMessage) {
              QuotationRequestMessage request = (QuotationRequestMessage)content;
              System.out.printf("Request Id: %d    Request Name: %s\n", request.id, request.info.name);
              producer.send(message);
              cache.put(request.id, request.info);
              message.acknowledge();
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

  /*
   * Listen for quotations on the QUOTATIONS queue.
   * Send responses to the RESPONSE topic
   */
  public static class ProducerThread implements Runnable {
    public void run() {
      try {
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue("QUOTATIONS");
        Topic topic = session.createTopic("RESPONSES");
        MessageProducer producer = session.createProducer(topic);
        MessageConsumer consumer = session.createConsumer(queue);
        MessageProducer responseToClient = session.createProducer(queue);
        connection.start();

        while (true) {
          quotes = new ArrayList<Quotation>();
          Quotation rquote = null;
          Message message = consumer.receive();

          HashMap<Long, List<Quotation>> quoteMap = new HashMap<Long, List<Quotation>>();

          /*
          Thread updateThread = new Thread(new Runnable() {
            public void run() {
              try {
                if (message instanceof ObjectMessage) {
                  Object content = ((ObjectMessage)message).getObject();
                  if (content instanceof QuotationResponseMessage) {
                    QuotationResponseMessage response = (QuotationResponseMessage)content;
                    quotes.add(response.quotation);
                    System.out.println("======================");
                    System.out.println("response id inside thread: ");
                    System.out.println(response.id);
                    System.out.println("======================");
                    //responseId = response.id;
                    message.acknowledge();
                  }
                } else {
                  System.out.println("Unknown message type: " + message.getClass().getCanonicalName());
                }
              } catch (JMSException e) {
                System.out.println("Trouble: " + e);
              }
            }
          });
          updateThread.start();
          //*/

          //* handled in new thread
          long responseId = -1;
          long seed = 0;
          //while (seed != 3) {
            if (message instanceof ObjectMessage) {
              Object content = ((ObjectMessage)message).getObject();
              if (content instanceof QuotationResponseMessage) {
                QuotationResponseMessage response = (QuotationResponseMessage)content;
                System.out.printf("Response Id: %d    Response Company: %s\n", response.id, response.quotation.company);
                //
                //if (quoteMap.containsKey(response.id))
                //
                if ((responseId == -1) || (responseId == response.id)) {
                  quotes.add(response.quotation);
                  responseId = response.id;
                  rquote = response.quotation;
                  message.acknowledge();
                  //seed++;
                } else {
                  //responseToClient.send(message);
                }
              }
            } else {
              System.out.println("Unknown message type: " + message.getClass().getCanonicalName());
            }
          //}
          // end of new thread */
          /*
          try {
            Thread.sleep(5000);
          } catch (InterruptedException e) {
            System.out.println("Trouble : " + e);
          }
          */
          //System.out.println("Quotes size: ");
          //System.out.println(quotes.size());
          //Message bundle = session.createObjectMessage(new ClientApplicationMessage(responseId, cache.get(responseId), quotes));
          Message bundle = session.createObjectMessage(new ClientApplicationMessage(responseId, cache.get(responseId), rquote));
          producer.send(bundle);
        }
      } catch (JMSException e) {
        System.out.println("Trouble: " + e);
      }
    }
  }
}
