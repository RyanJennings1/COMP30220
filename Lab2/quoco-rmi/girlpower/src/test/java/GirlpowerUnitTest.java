import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import core.ClientInfo;
import core.Constants;
import core.Quotation;
import core.QuotationService;
import girlpower.GPQService;

import org.junit.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GirlpowerUnitTest {
  private Registry registry;

  @Before
  public void setup() {
    QuotationService gpqService = new GPQService();
    try {
      registry = LocateRegistry.createRegistry(1099);

      QuotationService quotationService = (QuotationService)
        UnicastRemoteObject.exportObject(gpqService, 0);

      registry.bind(Constants.GIRL_POWER_SERVICE, quotationService);
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
  }

  @Test
  public void connectionTest() throws Exception {
    QuotationService service = (QuotationService)
      registry.lookup(Constants.GIRL_POWER_SERVICE);
    assertNotNull(service);
  }

  @Test
  public void generateQuotationReturnsQuotationTest() throws Exception {
    ClientInfo client1 = new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 16, 10, 0, "HMA304/9");
    GPQService service = new GPQService();
    Quotation quote = service.generateQuotation(client1);
    assertTrue(quote instanceof Quotation);
    assertNotNull(quote.company);
    assertNotNull(quote.reference);
    assertNotNull(quote.price);
  }
}
