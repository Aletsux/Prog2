import java.util.List;

// Temporary version from chatGPT
public interface Graph<N> {
    void addNode(N node);
    void addEdge(N node1, N node2);
    List<N> getNeighbors(N node);
    List<N> getNodes();
}

