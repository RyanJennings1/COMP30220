package service.messages;

import service.core.ClientInfo;

public class QuotationRequest {
  private int id;
  private ClientInfo clientInfo;

  public QuotationRequest(int id, ClientInfo clientInfo) {
    this.id = id;
    this.clientInfo = clientInfo;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ClientInfo getClientInfo() {
    return this.clientInfo;
  }

  public void setClientInfo(ClientInfo info) {
    this.clientInfo = info;
  }
}
