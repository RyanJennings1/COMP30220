package service.actor.Quoter;

import service.core.Quotation;
import service.core.QuotationService;

public class Quoter extends AbstractActor {
  private QuotationService service;

  @Override
  public Receive createReceive() {
    return receiveBuilder()
      .match(QuotationRequest.class,
        msg -> {
          Quotation quotation =
            service.generateQuotation(msg.getClientInfo());
          getSender().tell(
            new QuotationResponse(msg.getId(), quotation), getSelf());
        }).build();
  }
}
