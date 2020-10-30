package service.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientApplicationMessage implements Serializable {
  public long clientId;
  public ClientInfo clientInfo;
  public List<Quotation> quotations = new ArrayList<Quotation>();

  public ClientApplicationMessage(long clientId, ClientInfo clientInfo, Quotation quotation) {
    this.clientId = clientId;
    this.clientInfo = clientInfo;
    this.quotations.add(quotation);
  }
}
