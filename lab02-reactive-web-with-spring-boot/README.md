### Switching from Embedded Netty to Apache Tomcat
```
compile('org.springframework.boot:spring-boot-starter-webflux') {
  exclude group: 'org.springframework.boot', module: 'spring-boot-starter-reactor-netty'
}
compile('org.springframework.boot:spring-boot-starter-tomcat')
```