package com.task.vertices.service;

import com.task.vertices.model.Edge;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EdgeRepository extends MongoRepository<Edge, Long> {
}
