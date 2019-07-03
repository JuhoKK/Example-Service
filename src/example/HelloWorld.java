package example;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService()
public class HelloWorld {
  @WebMethod
  public String sayHelloWorldFrom(String from) {
    String result = "Hello, world, from " + from;
    System.out.println(result);
    return result;
  }

  public static void main(String[] argv) {
    Object implementor = new HelloWorld ();
    String address = "http://test-app-wildfly-testgames.apps.us-east-2.online-starter.openshift.com/HelloWorld";
    Endpoint.publish(address, implementor);
  }
}
