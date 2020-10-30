package service.girlpower;

import java.util.HashMap;
import java.util.Map;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import service.core.AbstractQuotationService;
import service.core.ClientInfo;
import service.core.NoSuchQuotationException;
import service.core.Quotation;

/**
 * Implementation of the Girl Power insurance quotation service.
 * 
 * @author Rem
 *
 */
@RestController
public class GPQService extends AbstractQuotationService {
  // All references are to be prefixed with an DD (e.g. DD001000)
  public static final String PREFIX = "GP";
  public static final String COMPANY = "Girl Power Inc.";

  private Map<String, Quotation> quotations = new HashMap<>();

  @RequestMapping(value="/quotations", method=RequestMethod.POST)
  public ResponseEntity<Quotation> createQuotation(@RequestBody ClientInfo info) throws URISyntaxException {
    Quotation quotation = generateQuotation(info);
    quotations.put(quotation.getReference(), quotation);

    String path = ServletUriComponentsBuilder.fromCurrentContextPath().
    build().toUriString() + "/quotations/" + quotation.getReference();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(new URI(path));
    return new ResponseEntity<>(quotation, headers, HttpStatus.CREATED);
  }

  @RequestMapping(value="/quotations/{reference}", method=RequestMethod.GET)
  public Quotation getResource(@PathVariable("reference") String reference) {
    Quotation quotation = quotations.get(reference);
    if (quotation == null) throw new NoSuchQuotationException();
    return quotation;
  }

  /**
   * Quote generation:
   * 50% discount for being female
   * 20% discount for no penalty points
   * 15% discount for < 3 penalty points
   * no discount for 3-5 penalty points
   * 100% penalty for > 5 penalty points
   * 5% discount per year no claims
   */
  public Quotation generateQuotation(ClientInfo info) {
    // Create an initial quotation between 600 and 1000
    double price = generatePrice(600, 400);

    // Automatic 50% discount for being female
    int discount = (info.getGender() == ClientInfo.FEMALE) ? 50:0;

    // Add a points discount
    discount += getPointsDiscount(info);

    // Add a no claims discount
    discount += getNoClaimsDiscount(info);

    // Generate the quotation and send it back
    return new Quotation(COMPANY, generateReference(PREFIX), (price * (100-discount)) / 100);
  }

  private int getNoClaimsDiscount(ClientInfo info) {
    return 5*info.getNoClaims();
  }

  private int getPointsDiscount(ClientInfo info) {
    if (info.getPoints() == 0) return 20;
    if (info.getPoints() < 3) return 15;
    if (info.getPoints() < 6) return 0;
    return -100;

  }

}
