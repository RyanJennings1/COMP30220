package service.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientApplicationMessage implements Serializable {
  public long clientId;
  public ClientInfo clientInfo;
  public Quotation quote;

  public ClientApplicationMessage(long clientId, ClientInfo clientInfo, Quotation quote) {
    this.clientId = clientId;
    this.clientInfo = clientInfo;
    this.quote = quote;
  }
}
