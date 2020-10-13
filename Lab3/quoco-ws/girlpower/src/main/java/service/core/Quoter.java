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
 * Implementation of the Girl Power insurance quotation service.
 * 
 * @author Rem
 *
 */
@WebService
@SOAPBinding(style=SOAPBinding.Style.DOCUMENT, use=SOAPBinding.Use.LITERAL)
public class Quoter extends AbstractQuotationService {
  // All references are to be prefixed with an DD (e.g. DD001000)
  public static final String PREFIX = "GP";
  public static final String COMPANY = "Girl Power Inc.";

  public static void main(String[] args) {
    /*
    try {
      Endpoint endpoint = Endpoint.create(new Quoter());
      HttpServer server = HttpServer.create(new InetSocketAddress(9002), 5);
      server.setExecutor(Executors.newFixedThreadPool(5));
      HttpContext context = server.createContext("/quotation");
      endpoint.publish(context);
      server.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
    */
    String host = "localhost";
    if (args.length > 0) {
      host = args[0];
    }
    Endpoint.publish("http://0.0.0.0:9002/quotation", new Quoter());
    jmdnsAdvertise(host);
  }
	
  /**
   * Quote generation:
   * 50% discount for being female
   * 20% discount for no penalty points
   * 15% discount for < 3 penalty points
   * no discount for 3-5 penalty points
   * 100% penalty for > 5 penalty points
   * 5% discount per year no claims
   */
  //@Override
  @WebMethod
  public Quotation generateQuotation(ClientInfo info) {
    // Create an initial quotation between 600 and 1000
    double price = generatePrice(600, 400);

    // Automatic 50% discount for being female
    int discount = (info.gender == ClientInfo.FEMALE) ? 50:0;

    // Add a points discount
    discount += getPointsDiscount(info);

    // Add a no claims discount
    discount += getNoClaimsDiscount(info);

    // Generate the quotation and send it back
    return new Quotation(COMPANY, generateReference(PREFIX), (price * (100-discount)) / 100);
  }

  private int getNoClaimsDiscount(ClientInfo info) {
    return 5*info.noClaims;
  }

  private int getPointsDiscount(ClientInfo info) {
    if (info.points == 0) return 20;
    if (info.points < 3) return 15;
    if (info.points < 6) return 0;
    return -100;
  }

  private static void jmdnsAdvertise(String host) {
    try {
      String config = "path=http://" + host + ":9002/quotation?wsdl";
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
