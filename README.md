**Micrometer Demo**

This little app is a simple spring program which generate an object with a random size and type. it then adds that object to a list.

Micrometer metrics have been added on top of this to show simple integration between a spring app, prometheus and grafana.

This demo contains the following micrometer implementations:

- counter: reports the amount of request objects created per type
- gauge: reports the total amount of request object created regardless of type.
- timer: times a method implemented that removes a request object from the total list.

**How to run**

The entire thing has been dockerised, so unless you want to pick it apart and fiddle around with it, you can get it all to run by going:
`docker-compose up` in the project folder (assuming you have docker up and running ofcourse!). This will launch the following:

- An instance of the app running on `localhost:8080/actuator/prometheus` where you can see the raw metrics displayed.
- An instance of prometheus running on `localhost:9090`
- An instance of grafana running on `localhost:3000`

**Configuration**

The only bit of configuration required to get the apps to talk to each other is within grafana. You will need to add the prometheus datasource as a 'server' address of `host.docker.internal:9090`. This will then link up prometheus and grafana allowing you to execute the same commands you do in prometheus inside grafana as well. 

**Commands**

Within prometheus/grafana, these basic commands can be run to get a visualisation of what is happening within this app. There are loads more that can be ran, but these take advantage of the methods implemented.

- **Request_Gauge** - Shows the total number of request objects created.
- **Request_Counter_total** - Shows the total number of request object create per object type.
- **rate(Request_Counter_total[5m])** - Shows the rate of request objects created per type over a five-minute period.
- **rate(method_timed_seconds_duration_sum[5m]) / rate(method_timed_seconds_active_count[5m])** - The rate of how frequently an object is removed from the list.
- **method_timed_seconds_duration_sum** - time taken to remove a request from the list.

**Gotchas**

If you get an error on docker compose along the lines of `Cannot create container for service prometheus: status code not OK but 500:` make sure docker has permission to read that folder. I made this in Windows and had to give docker permisson to the project folder in the 'File Sharing' options of docker for Windows.