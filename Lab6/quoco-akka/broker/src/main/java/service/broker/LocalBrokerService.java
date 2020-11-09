package service.broker;

import java.util.LinkedList;
import java.util.List;

import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuotationService;

/**
 * 
 * @author Rem
 *
 */
public class LocalBrokerService {
	public List<Quotation> getQuotations(ClientInfo info) {
		List<Quotation> quotations = new LinkedList<Quotation>();
		
    /*
		for (String name : ServiceRegistry.list()) {
			if (name.startsWith("qs-")) {
				QuotationService service = ServiceRegistry.lookup(name, QuotationService.class);
				quotations.add(service.generateQuotation(info));
			}
		}
    */

		return quotations;
	}
}
