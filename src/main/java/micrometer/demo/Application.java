package micrometer.demo;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import java.time.Duration;
import java.util.Random;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.publisher.Flux;

@EnableScheduling
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  private final Service service;

  public Application (Service service){
    this.service = service;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void generateRequests(){

    //Generates a 'stream' of data every second which generates a request and then 'sends' it.
    Flux.interval(Duration.ofSeconds(1))
        .map(Application::createRequest)
        .doOnEach( r -> service.sendRequest(r.get()))
        .subscribe();
  }

  //Creates a request object with a random type and size.
  public static Request createRequest(Long l){

    int requestSize = new Random().nextInt((10 - 1) + 1) + 1;
    String requestType = (new Random().nextInt() < 0) ? "Request Type 1" : "Request Type 2";

    return new Request(requestSize, requestType);
  }

  @Bean
  public TimedAspect timedAspect(MeterRegistry registry) {
    return new TimedAspect(registry);
  }
}