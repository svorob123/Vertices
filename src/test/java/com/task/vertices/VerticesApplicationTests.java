package com.task.vertices;

import com.task.vertices.model.EdgeType;
import com.task.vertices.model.Vertex;
import com.task.vertices.service.EdgeRepository;
import com.task.vertices.service.VertexRepository;
import com.task.vertices.service.VerticesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
public class VerticesApplicationTests {

    @Autowired
    VertexRepository vertexRepository;

    @Autowired
    EdgeRepository edgeRepository;

    @Autowired
    @Qualifier("VerService")
    VerticesService verticesService;

    private static final String NOT_FOUND = "not found";
    private static boolean isDataReady = false;

    @BeforeEach
    public void setUp() {
        if (!isDataReady) {
            makeTestData();
            isDataReady = true;
        }
    }

    @Test
    void testLongPath() {
        Assertions.assertEquals("0-1-8", verticesService.getPath(new Vertex(0), new Vertex(8)).orElse(NOT_FOUND));
        Assertions.assertEquals("8-1-0", verticesService.getPath(new Vertex(8), new Vertex(0)).orElse(NOT_FOUND));
        Assertions.assertEquals("0-1", verticesService.getPath(new Vertex(0), new Vertex(1)).orElse(NOT_FOUND));
        Assertions.assertEquals("2-1-8", verticesService.getPath(new Vertex(2), new Vertex(8)).orElse(NOT_FOUND));
    }

    @Test
    void testToRework() {
        Assertions.assertEquals("5-3-0-1-2", verticesService.getPath(new Vertex(5), new Vertex(2)).orElse(NOT_FOUND));
        Assertions.assertEquals("4-0-1-2", verticesService.getPath(new Vertex(4), new Vertex(2)).orElse(NOT_FOUND));
    }

    @Test
    void testNotFound() {
        Assertions.assertEquals(NOT_FOUND, verticesService.getPath(new Vertex(0), new Vertex(100)).orElse(NOT_FOUND));
        Assertions.assertEquals(NOT_FOUND, verticesService.getPath(new Vertex(110), new Vertex(2)).orElse(NOT_FOUND));
        Assertions.assertEquals(NOT_FOUND, verticesService.getPath(new Vertex(100), new Vertex(23)).orElse(NOT_FOUND));
    }

    @Test
    void testTooFar() {
        Assertions.assertEquals(NOT_FOUND, verticesService.getPath(new Vertex(0), new Vertex(17)).orElse(NOT_FOUND));
    }

    /**
     * 0 -> 1 -> 2
     * / \ /  \
     * 3   4   \
     * \ / \    \
     * 5   6-7->8 - 9 - 10 - 11 - 12 -13 -14 -15 - 16 - 17
     */
    private void makeTestData() {
        Vertex vertex0 = verticesService.getRootVertex();
        Vertex vertex1 = verticesService.addVertex(vertex0, EdgeType.DIRECTED);
        Vertex vertex2 = verticesService.addVertex(vertex1, EdgeType.DIRECTED);
        Vertex vertex3 = verticesService.addVertex(vertex0, EdgeType.UNDIRECTED);
        Vertex vertex4 = verticesService.addVertex(vertex0, EdgeType.UNDIRECTED);
        Vertex vertex5 = verticesService.addVertex(vertex3, EdgeType.UNDIRECTED);
        Vertex vertex6 = verticesService.addVertex(vertex4, EdgeType.UNDIRECTED);
        Vertex vertex7 = verticesService.addVertex(vertex6, EdgeType.UNDIRECTED);
        Vertex vertex8 = verticesService.addVertex(vertex7, EdgeType.DIRECTED);

        Vertex parent = vertex8;
        for (int i = 9; i < 18; i++) {
            parent = verticesService.addVertex(parent, EdgeType.UNDIRECTED);
        }

        verticesService.addEdge(vertex1, vertex4, EdgeType.UNDIRECTED);
        verticesService.addEdge(vertex1, vertex8, EdgeType.UNDIRECTED);
        verticesService.addEdge(vertex4, vertex5, EdgeType.UNDIRECTED);
    }

}
