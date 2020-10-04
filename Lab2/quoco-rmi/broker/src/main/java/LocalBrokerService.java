package broker;

import java.rmi.registry.*;

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
        //System.out.println("Before name: " + name);
        if (name.startsWith("qs-")) {
          QuotationService service = (QuotationService)registry.lookup(name);
          //System.out.println("Retrieved name: ");
          //System.out.println(name);
          quotations.add(service.generateQuotation(info));
        }
      }

    } catch (Exception e) {
      System.out.println("Trouble: " + e);
      //e.printStackTrace();
    }
		return quotations;
	}
}
