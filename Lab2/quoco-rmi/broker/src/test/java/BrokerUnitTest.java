import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import java.util.List;

import core.ClientInfo;
import core.Constants;
import core.Quotation;
import core.QuotationService;
import core.BrokerService;
import broker.LocalBrokerService;
import auldfellas.AFQService;
import dodgydrivers.DDQService;
import girlpower.GPQService;

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
    QuotationService afqService = new AFQService();
    QuotationService ddqService = new DDQService();
    QuotationService gpqService = new GPQService();

    QuotationService auldService = (QuotationService)
      UnicastRemoteObject.exportObject(afqService, 0);
    QuotationService dodgyService = (QuotationService)
      UnicastRemoteObject.exportObject(ddqService, 0);
    QuotationService girlService = (QuotationService)
      UnicastRemoteObject.exportObject(gpqService, 0);


    registry.bind(Constants.AULD_FELLAS_SERVICE, afqService);
    registry.bind(Constants.DODGY_DRIVERS_SERVICE, ddqService);
    registry.bind(Constants.GIRL_POWER_SERVICE, gpqService);
    for (String name: registry.list()) {
      System.out.println("Name: " + name);
    }

    ClientInfo client1 = new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4");
    //LocalBrokerService service = new LocalBrokerService();
    BrokerService service = (BrokerService)
      registry.lookup(Constants.BROKER_SERVICE);
    List<Quotation> quotes = service.getQuotations(client1);
    //assertTrue(quotes.size() > 0);
    //assertTrue(!quotes.isEmpty());
    assertNotNull(quotes);
    System.out.println("1-----------------------");
    for (Quotation quo: quotes) {
      System.out.println(quo.company);
    }
  }

  @Test
  public void getQuotationReturnsEmptyListTest() throws Exception {
    ClientInfo client1 = new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4");
    LocalBrokerService service = new LocalBrokerService();
    List<Quotation> quotes = service.getQuotations(client1);
    System.out.println("2-----------------------");
    for (Quotation quo: quotes) {
      System.out.println("2>>>");
      System.out.println(quo.company);
    }
    assertTrue(quotes.isEmpty());
  }
}
