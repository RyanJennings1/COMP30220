package service.core;

import java.io.Serializable;

/**
 * Class to store the quotations returned by the quotation services
 * 
 * @author Rem
 *
 */
public class Quotation implements Serializable {
  public Quotation(String company, String reference, double price) {
    this.company = company;
    this.reference = reference;
    this.price = price;
  }

  public Quotation() {}

  public String getCompany() {
    return this.company;
  }

  public void setCompany(String comp) {
    this.company = comp;
  }

  public String getReference() {
    return this.reference;
  }

  public void setReference(String ref) {
    this.reference = ref;
  }

  public Double getPrice() {
    return this.price;
  }

  public void setPrice(double pri) {
    this.price = pri;
  }

  private String company;
  private String reference;
  private double price;
}
