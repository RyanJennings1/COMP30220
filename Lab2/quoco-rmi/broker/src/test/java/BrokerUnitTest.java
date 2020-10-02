import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import core.ClientInfo;
import core.Constants;
import core.Quotation;
import core.QuotationService;
import broker.LocalBrokerService;

import org.junit.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BrokerUnitTest {
  private Registry registry;

  @Before
  public void setup() {
    LocalBrokerService brokerService = new LocalBrokerService();
    try {
      registry = LocateRegistry.createRegistry(1099);

      QuotationService quotationService = (QuotationService)
        UnicastRemoteObject.exportObject(brokerService, 0);

      registry.bind(Constants.BROKER_SERVICE, quotationService);
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
  }

  @Test
  public void connectionTest() throws Exception {
    QuotationService service = (QuotationService)
      registry.lookup(Constants.BROKER_SERVICE);
    assertNotNull(service);
  }

  @Test
  public void getQuotationReturnsListOfQuotationsTest() throws Exception {
    ClientInfo client1 = new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4");
    LocalBrokerService service = new LocalBrokerService();
    Quotation quote = service.generateQuotation(client1);
    assertTrue(quote instanceof Quotation);
    assertNotNull(quote.company);
    assertNotNull(quote.reference);
    assertNotNull(quote.price);
  }

  @Test
  public void getQuotationReturnsEmptyListTest() throws Exception {
    /*
    ClientInfo client1 = new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4");
    AFQService service = new AFQService();
    Quotation quote = service.generateQuotation(client1);
    assertTrue(quote instanceof Quotation);
    assertNotNull(quote.company);
    assertNotNull(quote.reference);
    assertNotNull(quote.price);
    */
  }
}
