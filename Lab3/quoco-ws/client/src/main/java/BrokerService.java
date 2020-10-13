package service.core;

import java.util.LinkedList;
import java.util.List;

import java.net.MalformedURLException;

import javax.jws.WebService;
import javax.jws.WebMethod;

import service.core.Quotation;

@WebService
public interface BrokerService {
  @WebMethod LinkedList<Quotation> getQuotations(ClientInfo info, List<String> urls) throws MalformedURLException;
}
