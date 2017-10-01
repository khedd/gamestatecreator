import java.util.ArrayList;

/**
 * Simple GraphNode implementation. Each node holds an edge and vertex. Also connected nodes.
 */
public class GraphNode {

    private static class CycleData{
        private enum Type { NONE, START, END};
        private int  id;
        private Type type;

        CycleData (int id, Type type){
            this.id = id;
            this.type = type;
        }
    }

    private ArrayList<CycleData> mCycleList;
    private UserAction mEdge;
    private GameState mVertex;
    private GraphNode mParent;

    private ArrayList<GraphNode> mNodes;

    private static int CYCLE_ID = 0;
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
        mCycleList = new ArrayList<>();
    }
    /**
     * Adds nodes to the current node, sets the parent
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

        int cycleStartEnd[] = new int[2];
        int cycleLength = checkCycle( names, cycleStartEnd);
        if ( cycleLength != 0){

            markCycle ( this, cycleStartEnd);
            pruneParentCycleFromGraph(this, cycleLength);
            return true;
        }else
        {
            return names.size() >= 16;
        }
    }

    private void markCycle(GraphNode node, int[] cycleStartEnd) {
        GraphNode parent = node;
        for (int i = 0; i < cycleStartEnd[0]; i++) {
            parent = parent.mParent;
        }
        parent.mCycleList.add( new CycleData(CYCLE_ID, CycleData.Type.END));
        for (int i = cycleStartEnd[0]; i < cycleStartEnd[1]; i++) {
            parent = parent.mParent;
        }
        parent.mCycleList.add( new CycleData(CYCLE_ID, CycleData.Type.START));
        CYCLE_ID++;
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
     * @param cycleStartEnd 2element array first element has the start, second has the end
     * @return True if there is cycle false if not
     */
    private int checkCycle ( ArrayList<UserAction> userActions, int cycleStartEnd[]){
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
                if (hasCycle) {
                    int cycleSize = cycle.size();
                    cycleStartEnd[0] = cycleSize;
                    cycleStartEnd[1] = cycleSize * 2 - 1;
                    return cycleSize;
                }
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
     * Gets all available paths from the starting node

     */
    public void getPaths (){
        ArrayList<ArrayList<SimplifiedNode>> paths = new ArrayList<>();
        getPaths( this, paths, null, 0);
        //convert paths to string array

        for ( ArrayList<SimplifiedNode> path: paths){
            ArrayList<String> strings = simplifiedToString( path);
            if ( checkIfExits(strings))
            {
                System.out.println("**************************************");
                for (String s : strings) {
                    System.out.println(s);
                }
            }

        }
//            if ( checkIfExits(path))
//            {
//                System.out.println("**************************************");
//                for (String node : path) {
//                    System.out.println(node);
//                }
//            }
//        }
//        return null;
    }

    /**
     * Check if the last element is the EXIT
     * @param path
     * @return
     */
    private boolean checkIfExits ( ArrayList<String> path){
        if ( path.isEmpty())
            return false;

        int idx = path.size() - 1;
        return path.get(idx).startsWith("EXIT");
    }



    private void getPaths (GraphNode root, ArrayList<ArrayList<SimplifiedNode>> paths,
                           ArrayList<SimplifiedNode> currentPath, int depth){

        //manage the path
        if ( currentPath == null){
            currentPath = new ArrayList<>();
        }else{
            if ( currentPath.size() > depth) {
                ArrayList<SimplifiedNode> newPath = new ArrayList<>();
                for (int i = 0; i < depth; i++) {
                    newPath.add( new SimplifiedNode( currentPath.get(i)));
                }
                currentPath = newPath;
            }
//                currentPath.subList(depth, currentPath.size()).clear();
        }
        //add current node
        currentPath.add( root.toSimplifiedNode());
        depth++;

        if ( root.mNodes.isEmpty()){
            paths.add( currentPath);
        }else{
            for (GraphNode graphNode : root.mNodes) {
                getPaths(graphNode, paths, currentPath, depth);
            }
        }


    }

    private ArrayList<String> simplifiedToString ( ArrayList<SimplifiedNode> simplifiedNodes){
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < simplifiedNodes.size(); i++) {
            SimplifiedNode simplifiedNode = simplifiedNodes.get(i);

            if ( simplifiedNode.mType == CycleData.Type.NONE) {
                for (int j = i + 1; j < simplifiedNodes.size(); j++) {
                    SimplifiedNode endCycleNode = simplifiedNodes.get(j);
                    if (endCycleNode.existsCycleId(simplifiedNode.mCycleList)) {
                        simplifiedNode.mVerifiedCycle = true;
                        simplifiedNode.mType = CycleData.Type.START;

                        endCycleNode.mVerifiedCycle = true;
                        endCycleNode.mType = CycleData.Type.END;
                        break;
                    }
                }
            }
            strings.add( simplifiedNodes.get(i).toString());
//            boolean isCycleStart
//            for (int j = i + 1; j < simplifiedNodes.size(); j++) {
//
//
//            }
        }
        return strings;
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

    private SimplifiedNode toSimplifiedNode (){
        SimplifiedNode simplifiedNode   = new SimplifiedNode();
        simplifiedNode.mCycleList       = mCycleList;

        if ( mEdge != null){
            simplifiedNode.mName = mEdge.getAction();
        }else {
            simplifiedNode.mName = "";
        }
////        return toString();
//        String string = "";
//        if ( mCycle == CYCLE.END){
//            string += "</CYCLE" + Arrays.toString( mCycleList.toArray()) + ">";
//        }
//        if ( mCycle == CYCLE.START){
//            string += "<CYCLE" + Arrays.toString( mCycleList.toArray()) + ">";
//        }
//        if ( mEdge != null)
//            string += mEdge.getAction();// + "\n" + mVertex.get();
////        else
////            return mVertex.toString();
        return simplifiedNode;
    }

    private static class SimplifiedNode {
        private ArrayList<CycleData>    mCycleList;
        private String                  mName;
        boolean                         mVerifiedCycle = false;
        CycleData.Type                  mType = CycleData.Type.NONE;

        SimplifiedNode (){
            mCycleList      = new ArrayList<>();
            mName           = "";
            mVerifiedCycle  = false;
            mType           = CycleData.Type.NONE;
        }

        SimplifiedNode(SimplifiedNode simplifiedNode) {
            mCycleList = simplifiedNode.mCycleList;
            mName      = simplifiedNode.mName;
            mVerifiedCycle = simplifiedNode.mVerifiedCycle;
            mType           = simplifiedNode.mType;
        }

        boolean existsCycleId ( ArrayList<CycleData> cycleIds){
            for (CycleData otherCycleData : cycleIds) {
                //if type is start search for the id with END
                if ( otherCycleData.type == CycleData.Type.START){
                    for ( CycleData ourCycleData: mCycleList){
                        if ( ourCycleData.type == CycleData.Type.END && ourCycleData.id == otherCycleData.id){
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public String toString() {
            String string = "";
            if ( mVerifiedCycle && mType == CycleData.Type.START){
                string += "<CYCLE>";
            }
            string += mName;
            if ( mVerifiedCycle && mType == CycleData.Type.END){
                string += "</CYCLE>";
            }
//            String aString = "";
//            for (CycleData data: mCycleList){
//                aString += data.id + " ";
//            }
            return string;
        }
    }

}
