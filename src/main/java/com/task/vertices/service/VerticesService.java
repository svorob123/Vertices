package com.task.vertices.service;

import com.task.vertices.model.Edge;
import com.task.vertices.model.EdgeType;
import com.task.vertices.model.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

@Service("VerService")
public class VerticesService {

    private SequenceGeneratorService sequenceGeneratorService;
    private VertexRepository vertexRepository;
    private EdgeRepository edgeRepository;
    private MongoTemplate mongoTemplate;
    private Vertex rootVertex;

    private static final int MAX_SEARCH_DEEP = 10;

    public Vertex addVertex(Vertex parent, EdgeType type) {
        long id = sequenceGeneratorService.generateSequence(Vertex.SEQUENCE);
        Vertex vertex = new Vertex(id);
        vertex = vertexRepository.save(vertex);
        if (parent == null) {
            parent = rootVertex;
        }
        addEdge(parent, vertex, type);
        return vertex;
    }

    public Edge addEdge(Vertex vertexA, Vertex vertexB, EdgeType type) {
        return edgeRepository.save(new Edge(vertexA, vertexB, type));
    }

    public Optional<String> getPath(Vertex vertexA, Vertex vertexB) {
        int foundCounter = 0;
        for (Vertex ignored : vertexRepository.findAllById(Arrays.asList(vertexA.getId(), vertexB.getId()))) {
            foundCounter++;
        }
        if (foundCounter < 2) {
            return Optional.empty();
        }
        return pathBuilder(vertexA, vertexB).map(queue -> queue.stream()
                .map(Vertex::getId).map(String::valueOf)
                .collect(Collectors.joining("-")));
    }

    private Optional<Queue<Vertex>> pathBuilder(Vertex vertexA, Vertex vertexB) {
        return pathBuilder(new LinkedList<>(), vertexA, vertexB, new HashSet<>(), 0);
    }

    private Optional<Queue<Vertex>> pathBuilder(Queue<Vertex> resultHolder, Vertex vertexA, Vertex vertexB, Set<Vertex> checkedSet, int deep) {
        resultHolder.add(vertexA);
        checkedSet.add(vertexA); // avoid circular recursion
        Query query = new Query();
        Criteria criteria = new Criteria();
        query.addCriteria(criteria.orOperator(Criteria.where("vertexA").is(vertexA), Criteria.where("vertexB").is(vertexA)));
        List<Edge> edges = mongoTemplate.find(query, Edge.class);

        Set<Vertex> possiblePath = new HashSet<>(); //TODO each level of recursion should have self list of possible paths.
        for (Edge edge : edges) {
            Vertex possibleVertex = edge.getVertexA().equals(vertexA) ? edge.getVertexB() : edge.getVertexA();
            if (possibleVertex.equals(vertexB)) {
                resultHolder.add(vertexB);
                return Optional.of(resultHolder);
            }
            if (!checkedSet.contains(possibleVertex)) {
                possiblePath.add(possibleVertex);
            }
        }
        if (++deep < MAX_SEARCH_DEEP) {
            for (Vertex vertex : possiblePath) {
                Optional<Queue<Vertex>> result = pathBuilder(new LinkedList<>(resultHolder), vertex, vertexB, checkedSet, deep);
                if (result.isPresent()) {
                    return result;
                }
            }
        }
        return Optional.empty();
    }

    public Vertex getRootVertex() {
        return rootVertex;
    }

    @Autowired
    public void setSequenceGeneratorService(SequenceGeneratorService sequenceGeneratorService) {
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Autowired
    public void setVertexRepository(VertexRepository vertexRepository) {
        this.vertexRepository = vertexRepository;
    }

    @Autowired
    public void setEdgeRepository(EdgeRepository edgeRepository) {
        this.edgeRepository = edgeRepository;
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    @Qualifier("rootNode")
    public void setRootVertex(Vertex rootVertex) {
        this.rootVertex = rootVertex;
    }
}
