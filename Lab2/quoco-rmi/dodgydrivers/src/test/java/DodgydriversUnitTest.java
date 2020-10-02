import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import core.ClientInfo;
import core.Constants;
import core.Quotation;
import core.QuotationService;
import dodgydrivers.DDQService;

import org.junit.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DodgydriversUnitTest {
  private Registry registry;

  @Before
  public void setup() {
    QuotationService ddqService = new DDQService();
    try {
      registry = LocateRegistry.createRegistry(1099);

      QuotationService quotationService = (QuotationService)
        UnicastRemoteObject.exportObject(ddqService, 0);

      registry.bind(Constants.DODGY_DRIVERS_SERVICE, quotationService);
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
  }

  @Test
  public void connectionTest() throws Exception {
    QuotationService service = (QuotationService)
      registry.lookup(Constants.DODGY_DRIVERS_SERVICE);
    assertNotNull(service);
  }

  @Test
  public void generateQuotationReturnsQuotationTest() throws Exception {
    ClientInfo client1 = new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9");
    DDQService service = new DDQService();
    Quotation quote = service.generateQuotation(client1);
    assertTrue(quote instanceof Quotation);
    assertNotNull(quote.company);
    assertNotNull(quote.reference);
    assertNotNull(quote.price);
  }
}
