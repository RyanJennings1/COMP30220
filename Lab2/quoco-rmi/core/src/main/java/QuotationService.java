package core;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface to define the behaviour of a quotation service.
 * 
 * @author Rem
 *
 */
public interface QuotationService extends java.rmi.Remote {
  public Quotation generateQuotation(ClientInfo info) throws java.rmi.RemoteException;
}
