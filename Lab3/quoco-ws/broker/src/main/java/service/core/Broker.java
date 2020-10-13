package service.core;

import java.util.concurrent.Executors;
import java.util.LinkedList;
import java.util.List;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import service.core.ClientInfo;
import service.core.Quotation;

/**
 * Implementation of the broker service that uses JWS
 * 
 * @author Rem
 *
 */
@WebService
@SOAPBinding(style=SOAPBinding.Style.DOCUMENT, use=SOAPBinding.Use.LITERAL)
public class Broker {
  private static LinkedList<String> urls = new LinkedList<String>();

  public static void main(String[] args) {
    try {
      Endpoint endpoint = Endpoint.create(new Broker());
      HttpServer server = HttpServer.create(new InetSocketAddress(9000), 5);
      server.setExecutor(Executors.newFixedThreadPool(5));
      HttpContext context = server.createContext("/broker");
      endpoint.publish(context);
      server.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @WebMethod
	public LinkedList<Quotation> getQuotations(ClientInfo info, List<String> urls) throws MalformedURLException {
		LinkedList<Quotation> quotations = new LinkedList<Quotation>();
		
		for (String url: urls) {
      URL wsdlUrl = new URL(url);
      QName serviceName = new QName("http://core.service/", "QuoterService");
      Service service = Service.create(wsdlUrl, serviceName);
      QName portName = new QName("http://core.service/", "QuoterPort");

      QuoterService qservice = service.getPort(portName, QuoterService.class);
      quotations.add(qservice.generateQuotation(info));
		}

		return quotations;
	}
}
