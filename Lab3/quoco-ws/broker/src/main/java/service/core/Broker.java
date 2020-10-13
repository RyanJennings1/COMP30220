package service.core;

import java.util.concurrent.Executors;
import java.util.LinkedList;
import java.util.List;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.net.URL;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

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
public class Broker implements ServiceListener {
  private static LinkedList<String> urls = new LinkedList<String>();

  public static void main(String[] args) {
    try {
      Endpoint.publish("http://0.0.0.0:9000/broker", new Broker());
      // Create a JmDNS instance
      JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
      // Add a service listener
      jmdns.addServiceListener("_http._tcp.local.", new Broker());
      // Wait a bit
      Thread.sleep(30000);
    } catch (UnknownHostException e) {
      System.out.println(e.getMessage());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    } catch (InterruptedException e) {
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

  @Override
  public void serviceAdded(ServiceEvent event) {
    System.out.println("Service added: " + event.getInfo());
  }

  @Override
  public void serviceRemoved(ServiceEvent event) {
    System.out.println("Service removed: " + event.getInfo());
  }

  @Override
  public void serviceResolved(ServiceEvent event) {
    System.out.println("Service resolved: " + event.getInfo());
    String path = event.getInfo().getPropertyString("path");
    if (path != null) {
      try {
        urls.add(path);
      } catch (Exception e) {
        System.out.println("Problem with service: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }
}
