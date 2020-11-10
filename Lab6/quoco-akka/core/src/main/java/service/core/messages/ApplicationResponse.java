package service.messages;

import java.util.List;

import service.core.ClientInfo;
import service.core.Quotation;

public class ApplicationResponse implements MySerializable {
  private ClientInfo clientInfo;
  private List<Quotation> quotations;

  public ApplicationResponse (ClientInfo clientInfo, List<Quotation> quotations) {
    this.clientInfo = clientInfo;
    this.quotations = quotations;
  }

  public ApplicationResponse () {}

  public ClientInfo getClientInfo() {
    return this.clientInfo;
  }

  public void setClientInfo(ClientInfo clientInfo) {
    this.clientInfo = clientInfo;
  }

  public List<Quotation> getQuotations() {
    return this.quotations;
  }

  public void setQuotations(List<Quotation> quotations) {
    this.quotations = quotations;
  }
}
