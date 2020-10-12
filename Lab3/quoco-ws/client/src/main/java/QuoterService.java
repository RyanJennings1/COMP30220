package service.core;

import javax.jws.WebService;
import javax.jws.WebMethod;

import service.core.Quotation;

@WebService
public interface QuoterService {
  @WebMethod Quotation generateQuotation(ClientInfo info);
}
