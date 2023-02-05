package com.task.vertices.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Document
@AllArgsConstructor
public class Edge {

    @Getter
    @DBRef
    private Vertex vertexA;

    @Getter
    @DBRef
    private Vertex vertexB;

    @Getter
    private EdgeType edgeType;

    @Id
    @Override
    public String toString() {
        return Stream.of(vertexA, vertexB).map(Vertex::getId).sorted().map(String::valueOf).collect(Collectors.joining("-"));
    }
}
