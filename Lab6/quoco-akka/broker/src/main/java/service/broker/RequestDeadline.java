package service.messages;

public class RequestDeadline {
  private int identifier;

  public RequestDeadline(int identifier) {
    this.identifier = identifier;
  }

  public RequestDeadline() {}

  public int getIdentifier() {
    return this.identifier;
  }

  public void setIdentifier(int identifier) {
    this.identifier = identifier;
  }
}
