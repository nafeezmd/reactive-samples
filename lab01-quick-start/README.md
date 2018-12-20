#Lab 01 - Quick start with Springboot 2.0

Run the com.labs.reactive.springboot.Application class in this module and try out the following:

```
curl -X GET http://localhost:8080/chapters
```

This should give a response:

```
[
 {
   "id": "5c12c6ef9a9fd85cc3dd7053",
   "name": "Learning Springboot 2.0"
 },
 {
   "id": "5c12c6ef9a9fd85cc3dd7054",
   "name": "Clean code"
 },
 {
   "id": "5c12c6ef9a9fd85cc3dd7052",
   "name": "Head first Java"
 }
]
```
   
   
###Testing the Springboot actuator:

With the `endpoints.health.enabled=true` in application.properties

#####To get health info of your application, try:

```
curl -X GET http://localhost:9000/application/health
```

This should show you details about the requests that your application could serve

```
{
  "status": "UP",
  "details": {
    "mongo": {
      "status": "UP",
      "details": {
        "version": "3.2.2"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 250685575168,
        "free": 24057614336,
        "threshold": 10485760
      }
    }
  }
}
```

Since, we haven't enabled other endpoints.{endpoint}.enabled=true, we would not be able to access those endpoints.

For example, following would not work

```
curl -X GET http://localhost:9000/application/metrics
```

##Take aways:
• ReacticeCrudRepository is the new **reactive** Spring Data CRUD repository. CrudRepository is blocking.

• ReacticeCrudRepository extends Repository, a Spring Data's marker interface that signals Spring Data to create a concrete implementation based on
 the reactive paradigm while also capturing domain information. It comes with some predefined CRUD operaitons. 

• Spring Data **does not** engage in Code generation. Code generation has a sordid history of being out of data at some of
 the worst times. (i.e you will not see a *ChapterRepositoryImpl.class* in your build directory)
 
• Spring data uses proxies and other mechanisms to support these operations.

• Spring Boot runs all CommandLineRunner beans after the entire application is up and running.

• @TestPropertySource - play with this more