package micrometer.demo;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Service {

  //The MeterRegistry is responsible for collecting and managing your application's meters.
  private final MeterRegistry meterRegistry;

  //Micrometer counters which will track requests for each type.
  Counter RequestType1;
  Counter RequestType2;

  //Arraylist to store the total number of requests coming into the service.
  private final List<Request> requests = new ArrayList<>();

  //Constructor for the service.
  public Service(MeterRegistry meterRegistry){
    this.meterRegistry = meterRegistry;

    //Setup the micrometer counters.
    initialiseCounters();
    
    //Setup the micrometer gauge.
    initialiseGauge();
    
  }

  //Initialise the two request counters.
  private void initialiseCounters(){
    RequestType1 = this.meterRegistry.counter("Request_Counter", "type", "Request Type 1");
    RequestType2 = this.meterRegistry.counter("Request_Counter", "type", "Request Type 2");
  }
  
  private void initialiseGauge(){

    //Created using the fluent API. You can create counters like this too and vice versa.
    Gauge.builder("Request_Gauge", requests, Collection::size)
        .description("Total amount of requests.")
        .register(meterRegistry);
  }

  //'Send' a request. Increment the total request count by one and increment the counter based on the request objects type.
  void sendRequest(Request request){
    requests.add(request);

    switch (request.requestType) {
      case "Request Type 1" -> RequestType1.increment(1.0);
      case "Request Type 2" -> RequestType2.increment(1.0);
    }
  }

  //'Process' a request by removing entries from the list.
  @Scheduled(fixedRate = 5000)
  @Timed(description = "Time spent processing requests.", longTask = true)
  public void processRequest() throws InterruptedException{
    if(!requests.isEmpty()){
      Request request = requests.remove(0);
      Thread.sleep(1000L * request.requestSize);
    }
  }
}