package endpoint;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.xml.ws.WebServiceContext;


@javax.ejb.Stateless
@WebService(
    name="Hello",
    serviceName="HelloService",
    targetNamespace="http://example.com/Hello"
)
public class Hello {
@Resource
   private WebServiceContext sc;

	public Hello() {}

	@WebMethod(operationName="sayHello", action="urn:SayHello")
	public String sayHello(String who) {
		return "Hello " + who;
	}
}
