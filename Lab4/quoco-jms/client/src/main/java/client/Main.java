package client;

import java.text.NumberFormat;
import java.util.HashMap;

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

//import service.auldfellas.AFQService;
//import service.broker.LocalBrokerService;
//import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
//import service.core.Constants;
//import service.dodgydrivers.DDQService;
//import service.girlpower.GPQService;

import service.message.QuotationResponseMessage;
import service.message.QuotationRequestMessage;

public class Main {
  public static int SEED_ID = 0;
  public static HashMap<Long, ClientInfo> cache = new HashMap<Long, ClientInfo>();

  /**
   * This is the starting point for the application. Here, we must
   * get a reference to the Broker Service and then invoke the
   * getQuotations() method on that service.
   * 
   * Finally, you should print out all quotations returned
   * by the service.
   * 
   * @param args
   */
  public static void main(String[] args) {

    String host = "localhost";
    if (args.length > 0) {
      host = args[0];
    }

    ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");

    try {
      Connection connection = factory.createConnection();
      connection.setClientID("client");
      Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

      Queue queue = session.createQueue("QUOTATIONS");
      Topic topic = session.createTopic("APPLICATIONS");
      MessageProducer producer = session.createProducer(topic);
      MessageConsumer consumer = session.createConsumer(queue);

      connection.start();

      for (ClientInfo clientInfo: clients) {
        QuotationRequestMessage quotationRequest = new QuotationRequestMessage(SEED_ID++, clientInfo);
        Message request = session.createObjectMessage(quotationRequest);
        cache.put(quotationRequest.id, quotationRequest.info);
        producer.send(request);
      }

      while(true) {
        Message message = consumer.receive();
        if (message instanceof ObjectMessage) {
          Object content = ((ObjectMessage) message).getObject();
          if (content instanceof ClientApplicationMessage) {
            ClientApplicationMessage response = (ClientApplicationMessage)content;
            ClientInfo info = cache.get(response.clientId);
            displayProfile(info);
            //cache.remove(response.clientId);
            for (Quotation quote: response.quotations) {
              displayQuotation(quote);
            }
            System.out.println("\n");
          }
          message.acknowledge();
        } else {
          System.out.println("Unknown message type: " + message.getClass().getCanonicalName());
        }
      }
    } catch (JMSException e) {
      System.out.println("Trouble: " + e);
    }
  }

  /**
   * Display the client info nicely.
   * 
   * @param info
   */
  public static void displayProfile(ClientInfo info) {
    System.out.println("|=================================================================================================================|");
    System.out.println("|                                     |                                     |                                     |");
    System.out.println(
      "| Name: " + String.format("%1$-29s", info.name) + 
      " | Gender: " + String.format("%1$-27s", (info.gender==ClientInfo.MALE?"Male":"Female")) +
      " | Age: " + String.format("%1$-30s", info.age)+" |");
    System.out.println(
      "| License Number: " + String.format("%1$-19s", info.licenseNumber) + 
      " | No Claims: " + String.format("%1$-24s", info.noClaims+" years") +
      " | Penalty Points: " + String.format("%1$-19s", info.points)+" |");
    System.out.println("|                                     |                                     |                                     |");
    System.out.println("|=================================================================================================================|");
  }

  /**
   * Display a quotation nicely - note that the assumption is that the quotation will follow
   * immediately after the profile (so the top of the quotation box is missing).
   * 
   * @param quotation
   */
  public static void displayQuotation(Quotation quotation) {
    System.out.println(
      "| Company: " + String.format("%1$-26s", quotation.company) + 
      " | Reference: " + String.format("%1$-24s", quotation.reference) +
      " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
    System.out.println("|=================================================================================================================|");
  }

  /**
   * Test Data
   */
  public static final ClientInfo[] clients = {
    new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1"),
    new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4"),
    new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 16, 10, 0, "HMA304/9"),
    new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3"),
    new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 4, 7, "QUN987/4"),
    new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")		
  };
}
