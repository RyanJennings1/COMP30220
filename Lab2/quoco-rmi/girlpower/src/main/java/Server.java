import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

import girlpower.GPQService;
import core.ClientInfo;
import core.Quotation;
import core.QuotationService;
import core.Constants;

public class Server {
  public static void main(String[] args) {
    QuotationService gpqService = new GPQService();
    try {
      // Connect to the RMI Registry - creating the registry will be the 
      // responsibility of the broker.
      String host = "localhost";
      Registry registry = null;
      if (args.length == 0) {
       registry = LocateRegistry.createRegistry(1099);
      } else {
       registry = LocateRegistry.getRegistry(host, 1099);
      }
      // Create the Remote Object
      QuotationService quotationService = (QuotationService)
      UnicastRemoteObject.exportObject(gpqService, 0);
      // Register the object with the RMI Registry
      registry.bind(Constants.GIRL_POWER_SERVICE, quotationService);

      System.out.println("STOPPING SERVER SHUTDOWN");
      while (true) {Thread.sleep(1000); }
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
  }
}
