package micrometer.demo;

public class Request {

  final int requestSize;
  final String requestType;

  public Request(int requestSize, String requestType){
    this.requestSize = requestSize;
    this.requestType = requestType;
  }
}
