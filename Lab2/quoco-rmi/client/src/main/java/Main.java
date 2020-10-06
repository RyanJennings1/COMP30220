package client;

import java.text.NumberFormat;

import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

import core.BrokerService;
import core.ClientInfo;
import core.Quotation;
import core.QuotationService;
import core.Constants;

import broker.LocalBrokerService;
import auldfellas.AFQService;
import dodgydrivers.DDQService;
import girlpower.GPQService;

public class Main {
  public static Registry registry;

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
    try {
      String host = "localhost";
      if (args.length == 0) {
        registry = LocateRegistry.createRegistry(1099);
      } else {
        registry = LocateRegistry.getRegistry(host, 1099);
      }

      // Create the services and bind them to the registry.
      QuotationService afqService = new AFQService();
      QuotationService ddqService = new DDQService();
      QuotationService gpqService = new GPQService();
      BrokerService lbService = new LocalBrokerService();

      QuotationService auldService = (QuotationService)
        UnicastRemoteObject.exportObject(afqService, 0);
      QuotationService dodgyService = (QuotationService)
        UnicastRemoteObject.exportObject(ddqService, 0);
      QuotationService girlService = (QuotationService)
        UnicastRemoteObject.exportObject(gpqService, 0);
      BrokerService brService = (BrokerService)
        UnicastRemoteObject.exportObject(lbService, 0);

      registry.bind(Constants.AULD_FELLAS_SERVICE, afqService);
      registry.bind(Constants.DODGY_DRIVERS_SERVICE, ddqService);
      registry.bind(Constants.GIRL_POWER_SERVICE, gpqService);
      registry.bind(Constants.BROKER_SERVICE, lbService);

      BrokerService brokerService = (BrokerService)registry.lookup(Constants.BROKER_SERVICE);

      // Create the broker and run the test data
      for (ClientInfo info: clients) {
        displayProfile(info);

        // Retrieve quotations from the broker and display them...
        for(Quotation quotation: brokerService.getQuotations(info)) {
          displayQuotation(quotation);
        }

        // Print a couple of lines between each client
        System.out.println("\n");
      }
    } catch (Exception e) {
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
