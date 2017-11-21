import java.util.ArrayList;

/**
 * Simple GraphNode implementation. Each node holds an edge and vertex. Also connected nodes.
 */
public class BinarizedGraphNode {
    private String mEdge;
    private BinarizedGameState mVertex;

    /**
     * Creates a Node containing an edge to this node and vertex of this node
     * @param edge String value for the edge
     * @param vertex String value for the vertex
     */
    BinarizedGraphNode(String edge, BinarizedGameState vertex) {
        mEdge   = edge;
        mVertex = vertex;
    }

    /**
     * Vertex
     * @return {@link BinarizedGameState} Vertex
     */
    BinarizedGameState getVertex() {
        return mVertex;
    }

    /**
     * @return String representation of class
     */
    @Override
    public String toString() {
        if ( mEdge != null)
            return mEdge + "\n" + mVertex.toString();
        else
            return mVertex.toString();
    }


}
