package client;

import java.text.NumberFormat;

import akka.actor.AbstractActor;

import service.core.ClientInfo;
import service.core.Quotation;
import service.messages.ApplicationResponse;

/**
 *
 * ClientActor that extends AbstractActor
 * Handle Application responses and output gathered
 * Quotations from the response
 *
 * displayProfile and displayQuotation are separated
 * from the client.Main.java file
 *
 * @author Ryan
 *
 */
public class ClientActor extends AbstractActor {
  @Override
  public Receive createReceive() {
    return receiveBuilder()
      .match(ApplicationResponse.class,
        msg -> {
          displayProfile(msg.getClientInfo());

          // Retrieve quotations from the broker and display them...
          for (Quotation quotation: msg.getQuotations()) {
            displayQuotation(quotation);
          }

          // Print a couple of lines between each client
          System.out.println("\n");
        }).build();
  }

  /**
   * Display the client info nicely.
   * 
   * @param info
   */
  public static void displayProfile(ClientInfo info) {
    System.out.println("|=================================================================================================================|");
    System.out.println("|                                     |                                     |                                     |");
    System.out.println(
      "| Name: " + String.format("%1$-29s", info.getName()) + 
      " | Gender: " + String.format("%1$-27s", (info.getGender()==ClientInfo.MALE?"Male":"Female")) +
      " | Age: " + String.format("%1$-30s", info.getAge())+" |");
    System.out.println(
      "| License Number: " + String.format("%1$-19s", info.getLicenseNumber()) + 
      " | No Claims: " + String.format("%1$-24s", info.getNoClaims()+" years") +
      " | Penalty Points: " + String.format("%1$-19s", info.getPoints())+" |");
    System.out.println("|                                     |                                     |                                     |");
    System.out.println("|=================================================================================================================|");
  }

  /**
   * Display a quotation nicely - note that the assumption is that the quotation will follow
   * immediately after the profile (so the top of the quotation box is missing).
   * 
   * @param quotation
   */
  public static void displayQuotation(Quotation quotation) {
    System.out.println(
      "| Company: " + String.format("%1$-26s", quotation.getCompany()) + 
      " | Reference: " + String.format("%1$-24s", quotation.getReference()) +
      " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.getPrice()))+" |");
    System.out.println("|=================================================================================================================|");
  }
}
