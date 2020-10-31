package service.broker;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import service.core.ClientApplication;
import service.core.ClientInfo;
import service.core.Quotation;

/**
 * Implementation of the broker service that uses REST.
 * 
 * @author Rem
 *
 */
@RestController
public class LocalBrokerService {
  private HashMap<Integer, ClientApplication> applications = new HashMap<Integer, ClientApplication>();
  private static String[] services = {
    "http://localhost:8080/quotations",
    "http://localhost:8081/quotations",
    "http://localhost:8082/quotations"
  };
  private static int applicationNumber = 0;

  @RequestMapping(value="/applications", method=RequestMethod.POST)
  public ClientApplication getQuotations(@RequestBody ClientInfo info) {
    List<Quotation> quotations = new LinkedList<Quotation>();
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<ClientInfo> request = new HttpEntity<>(info);

    for (String service: services) {
      Quotation quote = restTemplate.postForObject(service, request, Quotation.class);
      quotations.add(quote);
    }

    ClientApplication app = new ClientApplication(applicationNumber,
                                                  info,
                                                  quotations);
    applications.put(applicationNumber++, app);
    return app;
  }

  @RequestMapping(value="/applications/{application-number}", method=RequestMethod.GET)
  public ClientApplication getApplication(@PathVariable("application-number") String applicationNumber) {
    Integer appNumber = Integer.parseInt(applicationNumber);
    if (applications.containsKey(appNumber)) {
      return applications.get(appNumber);
    } else {
      // Safer from an enterprise coding perspective to return response
      // with error messages than throwing NoSuchQuotationException here
      // and stop the service
      ClientApplication notFound = new ClientApplication();
      notFound.setHasError(true);
      notFound.setErrorCode("404");
      return notFound;
    }
  }

  @RequestMapping(value="/applications", method=RequestMethod.GET)
  public LinkedList<ClientApplication> getAllApplications() {
    LinkedList<ClientApplication> apps = new LinkedList<ClientApplication>();
    for (ClientApplication ca: applications.values()) {
      apps.add(ca);
    }
    return apps;
  }
}
