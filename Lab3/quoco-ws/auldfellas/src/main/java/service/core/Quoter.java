package service.core;

import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Endpoint;

import service.core.AbstractQuotationService;
import service.core.ClientInfo;
import service.core.Quotation;

/**
 * Implementation of the AuldFellas insurance quotation service.
 * 
 * @author Rem
 *
 */
@WebService
@SOAPBinding(style=SOAPBinding.Style.DOCUMENT, use=SOAPBinding.Use.LITERAL)
public class Quoter extends AbstractQuotationService {
	// All references are to be prefixed with an AF (e.g. AF001000)
	public static final String PREFIX = "AF";
	public static final String COMPANY = "Auld Fellas Ltd.";

  public static void main(String[] args) {
    String host = "localhost";
    if (args.length > 0) {
      host = args[0];
    }
    Endpoint.publish("http://0.0.0.0:9001/quotation", new Quoter());
    jmdnsAdvertise(host);
  }
	
  /**
   * Quote generation:
   * 30% discount for being male
   * 2% discount per year over 60
   * 20% discount for less than 3 penalty points
   * 50% penalty (i.e. reduction in discount) for more than 60 penalty points 
   */
  @WebMethod
    public Quotation generateQuotation(ClientInfo info) {
    // Create an initial quotation between 600 and 1200
    double price = generatePrice(600, 600);

    // Automatic 30% discount for being male
    int discount = (info.gender == ClientInfo.MALE) ? 30:0;

    // Automatic 2% discount per year over 60...
    discount += (info.age > 60) ? (2*(info.age-60)) : 0;

    // Add a points discount
    discount += getPointsDiscount(info);

    // Generate the quotation and send it back
    return new Quotation(COMPANY, generateReference(PREFIX), (price * (100-discount)) / 100);
  }

  private int getPointsDiscount(ClientInfo info) {
    if (info.points < 3) return 20;
    if (info.points <= 6) return 0;
    return -50;
  }

  private static void jmdnsAdvertise(String host) {
    try {
      String config = "path=http://" + host + ":9001/quotation?wsdl";
      JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

      // Register a service
      ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.",
                                                   "ws-service",
                                                   1234,
                                                   config);
      jmdns.registerService(serviceInfo);

      // Wait a bit
      Thread.sleep(100000);

      // Unregister all services
      jmdns.unregisterAllServices();
    } catch (Exception e) {
      System.out.println("Problem Advertising Service: " + e.getMessage());
      e.printStackTrace();
    }
  }

}
