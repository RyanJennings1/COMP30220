package service.actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import scala.concurrent.duration.Duration;

import akka.actor.ActorRef;
import akka.actor.AbstractActor;

import service.core.ClientInfo;
import service.core.Quotation;
import service.messages.ApplicationRequest;
import service.messages.ApplicationResponse;
import service.messages.RequestDeadline;
import service.messages.QuotationRequest;
import service.messages.QuotationResponse;

public class Broker extends AbstractActor {
  private static List<ActorRef> actorRefs = new ArrayList<ActorRef>();
  private static HashMap<Integer, ClientInfo> clients = new HashMap<Integer, ClientInfo>();
  private static HashMap<Integer, List<Quotation>> quotations = new HashMap<Integer, List<Quotation>>();
  private static ActorRef client;
  private static int SEED_ID = 0;

  @Override
  public Receive createReceive() {
    return receiveBuilder()
      .match(String.class,
        msg -> {
          if (!msg.equals("register")) return;
          System.out.println("=================================");
          System.out.println("Registration received");
          actorRefs.add(getSender());
          System.out.println("=================================");
        })
      .match(QuotationResponse.class,
        msg -> {
          System.out.println("=================================");
          System.out.println("Response Id: --->");
          System.out.println(msg.getId());

          if (quotations.containsKey(msg.getId())) {
            List<Quotation> responseQuotations = quotations.get(msg.getId());
            responseQuotations.add(msg.getQuotation());
            quotations.put(msg.getId(), responseQuotations);
          } else {
            List<Quotation> responseQuotations = new ArrayList<Quotation>();
            responseQuotations.add(msg.getQuotation());
            quotations.put(msg.getId(), responseQuotations);
          }

          System.out.println("=================================");
        })
      .match(ApplicationRequest.class,
        msg -> {
          client = getSender();
          clients.put(SEED_ID, msg.getClientInfo());
          for (ActorRef ref: actorRefs) {
            ref.tell(
                new QuotationRequest(SEED_ID, msg.getClientInfo()), getSelf()
            );
          }

          getContext().system().scheduler().scheduleOnce(
            Duration.create(2, TimeUnit.SECONDS),
            getSelf(),
            new RequestDeadline(SEED_ID++),
            getContext().dispatcher(), null);
        })
      .match(RequestDeadline.class,
        msg -> {
          //getSender().tell(new ApplicationResponse(clients.get(msg.getIdentifier()),
          client.tell(new ApplicationResponse(clients.get(msg.getIdentifier()),
                                              quotations.get(msg.getIdentifier())), null);
        }).build();
  }
}
