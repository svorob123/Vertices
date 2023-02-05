package com.task.vertices.conf;

import com.task.vertices.service.VertexRepository;
import com.task.vertices.model.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrepareData {

    private final VertexRepository vertexRepository;

    @Autowired
    public PrepareData(VertexRepository vertexRepository) {
        this.vertexRepository = vertexRepository;
    }

    @Bean("rootNode")
    public Vertex makeRootNode() {
        return vertexRepository.findById(0L).orElse(vertexRepository.save(new Vertex(0)));
    }
}
