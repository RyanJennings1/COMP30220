package service.actor;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.AbstractActor;


// Test
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import service.actor.Quoter;
import service.core.ClientInfo;
import service.messages.Init;
import service.auldfellas.AFQService;
import service.girlpower.GPQService;
import service.dodgydrivers.DDQService;
import service.messages.QuotationRequest;
import service.messages.QuotationResponse;

public class Broker extends AbstractActor {
  private static List<ActorRef> actorRefs = new ArrayList<ActorRef>();
  private static int SEED_ID = 0;

  @Override
  public Receive createReceive() {
    return receiveBuilder()
      .match(String.class,
        msg -> {
          if (!msg.equals("register")) return;
          //actorRefs.add(getSender());
          System.out.println("=================================");
          System.out.println("Registration received");
          System.out.println("=================================");
          ClientInfo info = new ClientInfo("Niki Collier",
                                           ClientInfo.FEMALE,
                                           43, 0, 5,
                                           "PQR254/1");


          getSender().tell(new QuotationRequest(SEED_ID++, info), getSelf());
        })
      .match(QuotationResponse.class,
        msg -> {
          System.out.println("=================================");
          System.out.println("Response Id: --->");
          System.out.println(msg.getId());
          System.out.println("=================================");
        }).build();
  }
}
