package service.messages;

import service.core.ClientInfo;

public class ApplicationRequest implements MySerializable {
  private ClientInfo clientInfo;

  public ApplicationRequest (ClientInfo clientInfo) {
    this.clientInfo = clientInfo;
  }

  public ApplicationRequest () {}

  public ClientInfo getClientInfo() {
    return this.clientInfo;
  }

  public void setClientInfo(ClientInfo clientInfo) {
    this.clientInfo = clientInfo;
  }
}
