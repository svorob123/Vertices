package com.task.vertices.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = VertexSequence.DOCUMENT_NAME)
@Data
public class VertexSequence {
    public final static String DOCUMENT_NAME ="vertex_sequences";

    @Id
    private String id;

    private long seq;

}
