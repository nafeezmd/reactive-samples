package com.labs.reactive.springboot.repository;

import com.labs.reactive.springboot.model.Employee;
import com.labs.reactive.springboot.model.Image;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ExampleRepository {

  private final MongoOperations mongoOperations;

  public ExampleRepository(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  public Flux<Employee> findAllByExample() {
    Employee employee = new Employee();
    employee.setFirstName("baggins");

    ExampleMatcher matcher = ExampleMatcher.matching()
        .withIgnoreCase()
        .withMatcher("lastName", GenericPropertyMatchers.startsWith())
        .withIncludeNullValues();

    Example<Employee> example = Example.of(employee, matcher);
    return Flux.fromStream(mongoOperations.find(new Query(Criteria.byExample(example)), Employee.class).stream());
  }
}
