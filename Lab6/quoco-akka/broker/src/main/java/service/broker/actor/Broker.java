package service.actor;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.AbstractActor;

public class Broker extends AbstractActor {
  List<ActorRef> actorRefs = new ArrayList<ActorRef>();

  @Override
  public Receive createReceive() {
    return receiveBuilder()
      .match(String.class,
        msg -> {
          if (!msg.equals("register")) return;
          actorRefs.add(getSender());
        }).build();
  }
}
