package com.task.vertices.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Vertex {

    public Vertex(long id) {
        this.id = id;
    }

    @Transient
    public final static String SEQUENCE = VertexSequence.DOCUMENT_NAME;

    @Id
    @Getter
    private long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return id == vertex.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
