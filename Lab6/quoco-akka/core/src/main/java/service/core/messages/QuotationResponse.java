package service.messages;

import service.core.Quotation;

public class QuotationResponse {
  private int id;
  private Quotation quotation;

  public QuotationResponse(int id, Quotation quotation) {
    this.id = id;
    this.quotation = quotation;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Quotation getQuotation() {
    return this.quotation;
  }

  public void setQuotation(Quotation quotation) {
    this.quotation = quotation;
  }
}
