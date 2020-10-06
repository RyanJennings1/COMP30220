package broker;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import java.util.LinkedList;
import java.util.List;

import core.BrokerService;
import core.ClientInfo;
import core.Quotation;
import core.QuotationService;

/**
 * Implementation of the broker service that uses Java RMI
 * 
 * @author Rem
 *
 */
public class LocalBrokerService implements BrokerService {
  public List<Quotation> getQuotations(ClientInfo info) {
    List<Quotation> quotations = new LinkedList<Quotation>();
    try {
      Registry registry = LocateRegistry.getRegistry();

      for (String name : registry.list()) {
        if (name.startsWith("qs-")) {
          QuotationService service = (QuotationService)registry.lookup(name);
          quotations.add(service.generateQuotation(info));
        }
      }

    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
    return quotations;
  }
}
