import service.core.ClientInfo;
import service.messages.QuotationRequest;
import service.messages.QuotationResponse;

@Test
public void testQuoter() {
  ActorRef quoterRef = system.actorOf(Props.create(Quoter.class), "test");
  TestKit probe = new TestKit(system);

  quoterRef.tell(new Init(new AFQService()), null);
  quoterRef.tell(new QuotationRequest(1,
      new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1")),
    probe.getRef());
  probe.awaitCond(probe::msgAvailable);
  probe.expectMsgClass(QuotationResponse.class);
}
