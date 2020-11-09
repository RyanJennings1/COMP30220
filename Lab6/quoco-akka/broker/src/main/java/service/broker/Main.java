package service.broker;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;

//import service.core.BrokerService;
import service.actor.Quoter;
import service.messages.Init;

public class Main {
  public static void main(String[] args) {
    ActorSystem system = ActorSystem.create();
    ActorRef ref = system.actorOf(Props.create(Quoter.class), "broker");
    ref.tell(new Init(new LocalBrokerService()), null);
  }
}
