package service.broker;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;

import service.actor.Broker;

public class Main {
  public static void main(String[] args) {
    ActorSystem system = ActorSystem.create();
    ActorRef ref = system.actorOf(Props.create(Broker.class), "broker");
    //ref.tell(new Broker(), null);
    //ref.tell("register", null);
  }
}
