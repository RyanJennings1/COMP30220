package service.core;

import java.util.List;

public class ClientApplication {
  private int applicationNumber;
  private ClientInfo info;
  private List<Quotation> quotations;
  private Boolean hasError = false;
  private String errorCode = null;

  public ClientApplication() {}

  public ClientApplication(int applicationNumber, ClientInfo info, List<Quotation> quotations) {
    this.applicationNumber = applicationNumber;
    this.info = info;
    this.quotations = quotations;
  }

  public int getApplicationNumber() {
    return this.applicationNumber;
  }

  public void setApplicationNumber(int applicationNumber) {
    this.applicationNumber = applicationNumber;
  }

  public ClientInfo getInfo() {
    return this.info;
  }

  public void setInfo(ClientInfo info) {
    this.info = info;
  }

  public List<Quotation> getQuotations() {
    return this.quotations;
  }

  public void setQuotations(List<Quotation> quotations) {
    this.quotations = quotations;
  }

  public Boolean getHasError() {
    return this.hasError;
  }

  public void setHasError(Boolean hasError) {
    this.hasError = hasError;
  }

  public String getErrorCode() {
    return this.errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }
}
