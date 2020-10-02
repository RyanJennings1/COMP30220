import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import core.ClientInfo;
import core.Constants;
import core.Quotation;
import core.QuotationService;
import auldfellas.AFQService;

import org.junit.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AuldfellasUnitTest {
  private Registry registry;

  @Before
  public void setup() {
    QuotationService afqService = new AFQService();
    try {
      registry = LocateRegistry.createRegistry(1099);

      QuotationService quotationService = (QuotationService)
        UnicastRemoteObject.exportObject(afqService, 0);

      registry.bind(Constants.AULD_FELLAS_SERVICE, quotationService);
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
  }

  @Test
  public void connectionTest() throws Exception {
    QuotationService service = (QuotationService)
      registry.lookup(Constants.AULD_FELLAS_SERVICE);
    assertNotNull(service);
  }

  @Test
  public void generateQuotationReturnsQuotationTest() throws Exception {
    ClientInfo client1 = new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4");
    AFQService service = new AFQService();
    Quotation quote = service.generateQuotation(client1);
    assertTrue(quote instanceof Quotation);
    assertNotNull(quote.company);
    assertNotNull(quote.reference);
    assertNotNull(quote.price);
  }
}
