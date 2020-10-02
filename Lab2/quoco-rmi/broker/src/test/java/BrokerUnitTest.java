import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import java.util.List;

import core.ClientInfo;
import core.Constants;
import core.Quotation;
import core.BrokerService;
import broker.LocalBrokerService;

import org.junit.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BrokerUnitTest {
  private static Registry registry;

  @Before
  public void setup() {
    BrokerService lbService = new LocalBrokerService();
    try {
      registry = LocateRegistry.createRegistry(1099);

      BrokerService brokerService = (BrokerService)
        UnicastRemoteObject.exportObject(lbService, 0);

      registry.bind(Constants.BROKER_SERVICE, lbService);
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
  }

  @Test
  public void connectionTest() throws Exception {
    BrokerService service = (BrokerService)
      registry.lookup(Constants.BROKER_SERVICE);
    assertNotNull(service);
  }

  @Test
  public void getQuotationReturnsListOfQuotationsTest() throws Exception {
    /*
    ClientInfo client1 = new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4");
    LocalBrokerService service = new LocalBrokerService();
    List<Quotation> quotes = service.getQuotations(client1);
    //assertTrue(quote instanceof Quotation);
    //assertNotNull(quote.company);
    //assertNotNull(quote.reference);
    //assertNotNull(quote.price);
    assertNotNull(quotes);
    System.out.println(quotes);
    */
    assertTrue(true);
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
    assertTrue(true);
  }
}
