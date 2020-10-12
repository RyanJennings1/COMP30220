package client;

import java.text.NumberFormat;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuoterService;

//import service.auldfellas.AFQService;
//import service.dodgydrivers.DDQService;
//import service.girlpower.GPQService;

public class Client {

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
      int port = 9001;

      // More Advanced flag-based configuration
      for (int i = 0; i < args.length; i++) {
        if (args[i].equals("-h") || args[i].equals("--host")) {
          host = args[++i];
        } else if (args[i].equals("-p") || args[i].equals("--port")) {
          port = Integer.parseInt(args[++i]);
        } else if (args[i].equals("--help")) {
          System.out.println("Command line options:");
          System.out.println("\t[-h|--host] = Host name to use");
          System.out.println("\t[-p|--port] = Port number to use");
        }
      }

      URL wsdlUrl = new URL("http://" + host + ":" + port + "/quotation?wsdl");

      QName serviceName = new QName("http://core.service/", "QuoterService");

      Service service = Service.create(wsdlUrl, serviceName);

      QName portName = new QName("http://core.service/", "QuoterPort");
      QuoterService quotationService = service.getPort(portName, QuoterService.class);

      for (ClientInfo info : clients) {
        displayProfile(info);

        Quotation quotation = quotationService.generateQuotation(info);
        displayQuotation(quotation);

        System.out.println("\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
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
