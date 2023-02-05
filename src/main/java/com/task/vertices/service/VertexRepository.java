package com.task.vertices.service;

import com.task.vertices.model.Vertex;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VertexRepository extends MongoRepository<Vertex, Long> {
}
