import java.util.ArrayList;

/**
 * Simple GraphNode implementation. Each node holds an edge and vertex. Also connected nodes.
 */
public class GraphNode {

    private UserAction mEdge;
    private GameState mVertex;
    private GraphNode mParent;
    private ArrayList<GraphNode> mNodes;
    /**
     * Pruned nodes
     */
    private ArrayList<GraphNode> mForbiddenNodes;

    /**
     * Creates a Node containing an edge to this node and vertex of this node
     * @param edge String value for the edge
     * @param vertex String value for the vertex
     */
    GraphNode(UserAction edge, GameState vertex) {
        mEdge   = edge;
        mVertex = vertex;
        mParent = null;
        mNodes = new ArrayList<>();


    }
    /**
     * Adds nodes to the current node, sets the paret
     * @param node Connected nodes
     * @return If the node is rejected for any reason such as creating a cycle the returns false
     * if the node is added return true
     */
    boolean AddNode ( GraphNode node){
        node.mParent = this;
        if (!node.checkCycleParentAndPrune()) {
            mNodes.add(node);
            return true;
        }
        return false;
    }

    /**
     * Checks whether parent nodes have a cycle, if there is a cycle also prunes the list
     * @return True if there is cycle in parent nodes false if not
     */
    private boolean checkCycleParentAndPrune(){
        ArrayList<UserAction> names = new ArrayList<>();
        checkCycleParentHelperGetNames ( names);

        int cycleLength = checkCycle( names);
        if ( cycleLength != 0){
            pruneParentCycleFromGraph(this, cycleLength);
            return true;
        }else
        {
            return names.size() >= 15;
        }
    }

    /**
     * prunes the cycle from the graph starting from this node going to parent. If parent does
     * not have any outgoing nodes parent also calls this method
     */
    private void pruneParentCycleFromGraph(GraphNode node, int cycleLength) {
        if (cycleLength != 0) {
            if (this == node) {
                mParent.pruneParentCycleFromGraph(this, cycleLength);
            } else {
                mNodes.remove(node);
                mParent.pruneParentCycleFromGraph(this, cycleLength - 1);
            }
        }
    }

    /**
     * Checks the user actions name to find whether it includes a cycle
     * @param userActions edges, user actions in child to parent order
     * @return True if there is cycle false if not
     */
    private int checkCycle ( ArrayList<UserAction> userActions){
        ArrayList<UserAction> cycle = new ArrayList<>();
        //a cycle must have at least three elements in this game
        if ( userActions.size() > 3) {
            for (int i = 0; i < userActions.size(); i++) {
                //loop will not set hasCycle to false if there is no element
                boolean hasCycle = !cycle.isEmpty();
                for (int j = 0; hasCycle && j < cycle.size(); j++) {
                    if (cycle.get(j) != userActions.get(i + j)) {
                        hasCycle = false;
                    }
                }
                if (hasCycle)
                    return cycle.size();
                cycle.add(userActions.get(i));
                //there is no cycle if cycle to be checked is greater than half of the list
                if ( cycle.size() * 2 > userActions.size()){
                    return 0;
                }
            }
        }
        return 0;
    }

    /**
     * Collects all the edges through the initial node of the graph
     * @param userActions Edges list
     */
    private void checkCycleParentHelperGetNames (ArrayList<UserAction> userActions){
        if ( mParent != null){
            userActions.add(mEdge);
            mParent.checkCycleParentHelperGetNames( userActions);
        }
    }

    /**
     * Vertex
     * @return {@link GameState} Vertex
     */
    GameState getVertex() {
        return mVertex;
    }

    /**
     * Prints the graph in text format displaying user action and game state
     */
    void print() {
        if ( mNodes.isEmpty()){
            System.out.println ( toString());
        }else {
            System.out.println ( toString());
            for (GraphNode graphNode : mNodes) {
                System.out.println("***************************************");
                graphNode.printNodes();

            }
        }

    }

    /**
     * Helper method used in {@link #print()}
     */
    private void printNodes(){
        if ( mNodes.isEmpty()){
            System.out.println ( toString());
            System.out.println("***************************************");
        }else {
            System.out.println ( toString());
            for (GraphNode graphNode : mNodes) {
                graphNode.printNodes();
            }
        }
    }

    /**
     * @return String representation of class
     */
    @Override
    public String toString() {
        if ( mEdge != null)
            return mEdge.toString() + "\n" + mVertex.toString();
        else
            return mVertex.toString();
    }
}
