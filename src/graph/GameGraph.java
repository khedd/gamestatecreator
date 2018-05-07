package graph;

import java.io.*;
import java.util.*;

/**
 * Adjacency List Graph Implementation.
 *  Currently bears some coverage tools such as
 *   - Calculating CyclometicComplexity
 *   - Edge Coverage
 *   - Basis Path Extraction
 * @param <T>
 */
public class GameGraph<T> {
    public static int UNVISITABLE_EDGE = -1;
    /**
     * Inner class for holding the node data and the edge
     * Currently edge is hold as string, might be parametrized in the future
     * @param <T>
     */
    public static class GameGraphNode<T>{

        public T node; /// vertex
        public String action; /// edge
        public int visitCount = 0; //hope this will not overflow if -1 not a node able to be visited


        /**
         * initializes the node with empty parameters
         */
        GameGraphNode() {
            node = null;
            action = "";
            visitCount = 0;
        }

        /**
         * initializes with node and action parameters
         * @param node Node data
         * @param action Edge
         */
        public GameGraphNode(T node, String action) {
            this.node = node;
            this.action = action;
        }

        public GameGraphNode(GameGraphNode<T> gameGraphNode) {
            this.node = gameGraphNode.node;
            this.action = gameGraphNode.action;
            this.visitCount = gameGraphNode.visitCount;
        }

        @Override
        public String toString() {
            if ( action.isEmpty()){
                return "" + node;
            }else
            {
                return action +  " " + node;
            }
        }

        @Override
        public int hashCode() {
            return node.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof GameGraphNode))
                return false;
            if (obj == this)
                return true;


            GameGraphNode rhs = (GameGraphNode) obj;
            if (!rhs.node.equals(node))
                return false;
            return rhs.action.compareTo(action) == 0;
        }
    }

    /**
     * The graph adjacency list structure.
     * [T] ->[[{@link GameGraphNode}][{@link GameGraphNode}]]
     */
    private final HashMap<T, ArrayList< GameGraphNode<T>>> mGraph = new HashMap<>(); ///adjacency list

    /**
     * todo this has nothing to do with the graph implementation export this to subclass
     * holds the unvisited nodes while exploring the graph
     */
    private final Queue<T> mUnExplored = new LinkedList<>();

    //hold two new nodes to represent the start and end since we do not have that
    //information after we convert to adjacency matrix
    private GameGraphNode<T> startNode = null;
    private GameGraphNode<T> endNode = null;


    public GameGraphNode<T> getStartNode() {
        return startNode;
    }

    public HashMap<T, ArrayList<GameGraphNode<T>>> getGraph() {
        return mGraph;
    }


    /**
     * Adds a node to the graph if it exists does nothing so return false
     * If the node does not exist will create an empty adjacency list and return true
     * @param node Node to insert
     * @return True or False depending on the result of insertion operation
     */
    public boolean addNode ( T node){
        if (mGraph.containsKey( node)){
            return false;
        }else {
            mGraph.put( node, new ArrayList<GameGraphNode<T>>());
            mUnExplored.add( node); //todo add this to subclass
            return true;
        }
    }

    /**
     * Adds children array to the node, completing that nodes adjacency list.
     * Also calls addNode function for each added children
     * While adding to the adjacency list the code does not deep copy the data
     * @param node Node
     * @param children Children in {@link GameGraphNode}
     * @return if the data is added returns true else return false
     */
    public boolean addChildren ( T node,  ArrayList< GameGraphNode<T>> children){
        if ( mGraph.containsKey( node)) {
            mGraph.put( node, children);
            mUnExplored.remove( node);
            for ( GameGraphNode<T> ggn: children){
                addNode( ggn.node);
            }
            return true;
        }
        return false;
    }

    /**
     * sets the start node of the graph as the data is hold as adjacency list
     * we need to hold the head node for exploring
     * @param node Node
     */
    public void setStartNode ( T node){
        startNode = new GameGraphNode<>(node, "");
    }


    /**
     * @return returns the end node
     */
    public GameGraphNode<T> getEndNode() {
        return endNode;
    }

    /**
     * this will also set the end node in the system
     * check every node and find the ones that have 0 outgoing edges
     * meet them at the end
     * @param node data representing the end node for long T choose -1
     */
    public void finalize (T node){
        endNode = new GameGraphNode<>(node, "");
        endNode.visitCount = UNVISITABLE_EDGE; //set visit count as negative as it cannot be visited
        ArrayList<T> endNodes = new ArrayList<>();
        for ( Map.Entry<T, ArrayList<GameGraphNode<T>>> nodes: mGraph.entrySet()){
            if ( nodes.getValue().isEmpty()){
                endNodes.add( nodes.getKey());
//                System.out.println( nodes);
            }
        }
        for(T t: endNodes){
            mGraph.get(t).add( endNode);
        }

        mGraph.put( node, new ArrayList<>());
        //insert end node to the system
    }

    /**
     * todo move to the subclass as it is linked with exploring graph
     * @return returns the next unexplored node
     */
    public T getNext (){
        if ( mUnExplored.isEmpty())
            return null;
        return mUnExplored.peek();
    }

    /**
     * todo move to the subclass as it is linked with exploring graph
     * @return returns whether the graph has a next unexplored node
     */
    public boolean hasNext (){
        return  !( mUnExplored.isEmpty());
    }

    /**
     * todo this is linked with edge coverage move to another implementation
     * visits a given node returns the new state
     * @param node current node
     * @param action action applied to the current node
     * @return new node after visiting
     */
    public T visit (T node, String action){
        if ( mGraph.containsKey( node)){
            ArrayList<GameGraphNode<T>> nodes = mGraph.get( node);
            for (GameGraphNode<T> gameGraphNode : nodes) {
//                System.out.println("NODE ACTIONS: " + gameGraphNode.action);
                if ( gameGraphNode.action.compareTo( action) == 0){
                    gameGraphNode.visitCount++;
                    return gameGraphNode.node;
                }
            }
            return null;
        }else{
            return null;
        }
    }


    /**
     * resets the coverage by clearing visited data for every node
     */
    public void resetCoverage() {
        for ( Map.Entry<T, ArrayList<GameGraphNode<T>>> nodes: mGraph.entrySet()){
            for (GameGraphNode<T> gameGraphNode : nodes.getValue()) {
                if ( gameGraphNode.visitCount != UNVISITABLE_EDGE)
                    gameGraphNode.visitCount = 0;
            }
        }
    }

    /**
     * prints a row of adjacency list
     * @param node
     */
    public void printAdjacencyListRow(T node){
        if ( mGraph.containsKey( node)){
            ArrayList<GameGraphNode<T>> nodes = mGraph.get( node);
            System.out.println("NODE ACTIONS:");
            for (GameGraphNode<T> gameGraphNode : nodes) {
                System.out.println(" " + gameGraphNode.action);
            }
        }
    }

    /**
     * @return the nodes of the adjacency list
     */
    public Set<T> getNodes(){
        return mGraph.keySet();
    }

    /**
     * traverse the graph and find how many visited nodes there are
     */
    @SuppressWarnings("Duplicates")
    public void printEdgeCoverage() {
        int totalEdges = 0;
        int visitedEdges = 0;
        for ( Map.Entry<T, ArrayList<GameGraphNode<T>>> nodes: mGraph.entrySet()){
            int size = nodes.getValue().size();
            totalEdges += size;
            for (GameGraphNode<T> gameGraphNode : nodes.getValue()) {
                if ( gameGraphNode.visitCount > 0)
                    visitedEdges++;
            }
        }
        System.out.println("Total Edges: " + totalEdges +
                           " Visited: " + visitedEdges);
    }

    /**
     * returns the edge coverage percent in range [0-100]
     * @return a double value indicating the coverage amount
     */
    @SuppressWarnings("Duplicates")
    public double getEdgeCoveragePercent() {
        double totalEdges = 0;
        double visitedEdges = 0;
        for ( Map.Entry<T, ArrayList<GameGraphNode<T>>> nodes: mGraph.entrySet()){
            int size = nodes.getValue().size();
            totalEdges += size;
            for (GameGraphNode<T> gameGraphNode : nodes.getValue()) {
                if ( gameGraphNode.visitCount == UNVISITABLE_EDGE){
                    totalEdges--;
                }
                else if ( gameGraphNode.visitCount > 0)
                    visitedEdges++;
            }
        }
        return visitedEdges / totalEdges * 100.0;
    }

    /**
     * calculates the cyclometic complexity
     */
    public void calculateCyclometicComplexity (){
        // FIXME: 10.12.2017 this might be wrong
        int totalNodes = mGraph.size();
        int totalEdges = 0;
        int exits = 0;
        for ( Map.Entry<T, ArrayList<GameGraphNode<T>>> nodes: mGraph.entrySet()){
            int size = nodes.getValue().size();
            if ( size == 0){
                exits++;
            }else{
                totalEdges += size;
            }
        }
        System.out.println("Total Edges: " + totalEdges + " totalNodes: " + totalNodes + " exits: " + exits);
        int complexity = totalEdges - totalNodes + 2 * exits;
        System.out.println("Cyclometic Complexity: " + complexity);
    }

    /**
     * calculates and prints the basis paths
     * fixme will print to file as paths will be long
     * todo add a filename option to chose whether stdout & file
     */
    public void printBasisPaths (){
        BasisPathCalculator<T> basisPathCalculator = new BasisPathCalculator<>( mGraph);

        if ( startNode == null || endNode == null)
            System.out.println( "print basis requires start and end nodes to be known");
        else {
            ArrayList<ArrayList<GameGraphNode<T>>> paths = basisPathCalculator.generate(startNode, endNode);
            for (ArrayList<GameGraphNode<T>> path : paths) {
                System.out.println(Arrays.toString(path.toArray()));
            }
        }
    }

    /**
     * private class for helping with the basis path calculation
     * adapted from Joseph Poole `A Method to Determine a Basis Set of Paths to Perform Program Testing`
     * todo error in loops paths go too large thus causing stackoverflow
     * Assumptions:
     *  - One source one sink
     *  - From the source there exists a path
     *  - From any node there exists a path to the sink
     * todo not sure whether 3rd assumption holds, need to check for algorithms
     * @param <T>
     */
    private static class BasisPathCalculator<T> {
        /**
         * maximum path length hold in the graph
         */
        private final static int MAX_PATH_LENGTH = 2500;

        /**
         * holds a copy of the graph to explore
         */
        final HashMap<T, ArrayList< GameGraphNode<T>>> mGraph;

        /**
         * will hold the paths if we are not writing to a file
         */
        final ArrayList<ArrayList<GameGraphNode<T>>> nodes = new ArrayList<>();
        /**
         * holds the visited nodes
         */
        final Set<T> visitedNodes = new HashSet<>();
        /**
         * for writing to files @requires java 1.7
         */
        Writer writer;

        /**
         * counter for debugging how many of the paths in the cyclometic complexity is covered
         */
        int totalPaths = 0;

        /**
         * initializes the path calculator
         * @param graph graph that the algorithm will work on
         */
        public BasisPathCalculator(final HashMap<T, ArrayList< GameGraphNode<T>>> graph) {
            mGraph = graph;
            //if writer is null wer may return the arraylist?
            initializeWriter ( "BasisPaths");

        }

        /**
         * initializes the writer with the given filename will write using utf-8
         * if the writer cannot be initialized it will remain null and will print the corresponding error
         * todo may the class throw errors
         * @param filename filename
         */
        private void initializeWriter ( final String filename){
            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(filename), "utf-8"));
            } catch (UnsupportedEncodingException | FileNotFoundException e) {
                writer = null;
                e.printStackTrace();
            }
        }

        /**
         * closes the writer
         */
        private void closeWriter ( ){
            if ( writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * writes a given data to the file
         * @param data string data
         */
        private void writeLine ( String data){
            if ( writer != null) {
                try {
                    writer.write(data + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * generates basis paths between the given start and end nodes
         * @param start start node
         * @param end end node
         * @return total basis paths currently empty set as it is written to file
         */
        public ArrayList<ArrayList<GameGraphNode<T>>> generate(GameGraphNode<T> start, GameGraphNode<T> end) {
            ArrayList<GameGraphNode<T>> currentPath = new ArrayList<>();
            generate (start, end, currentPath);

            closeWriter ();
            System.out.println( "Total Paths: " + totalPaths);
            return nodes; //todo empty return
        }

        /**
         * generates the basis paths but due to loops in the system fails to retrieve them all added randomness and max path
         * length to counter stackoverflow problems and looping due to its labeled path is the looping path
         * @param current Current node that we are investigating
         * @param end end node aka sink node todo may be a global
         * @param currentPath current path that we are on will split at nodes that have more than one adjacency
         */
        private void generate(GameGraphNode<T> current, GameGraphNode<T> end, ArrayList<GameGraphNode<T>> currentPath) {
            //node is always added
            currentPath.add( current);
            if ( current.node == end.node){
                writeLine ( Arrays.toString( currentPath.toArray()));
                totalPaths++;

//                nodes.add( currentPath);
            }else if (!visitedNodes.contains( current.node)){

                visitedNodes.add( current.node);
                ArrayList<GameGraphNode<T>> gameGraphNodes = mGraph.get(current.node);

                Random random = new Random();
                int randomEdge = random.nextInt( gameGraphNodes.size());

                //need to hold this externally else after we generate with the current path copies are added to the
                //wrong path
                ArrayList<GameGraphNode<T>> copyPath = new ArrayList<>( currentPath);
                for (int i = 0; i < gameGraphNodes.size(); i++) {
                    GameGraphNode<T> gameGraphNode = gameGraphNodes.get(i);
                    if ( i == randomEdge) {
                        generate(gameGraphNode, end, currentPath);
                    }else{
                        ArrayList<GameGraphNode<T>> newPath = new ArrayList<>( copyPath);
                        generate(gameGraphNode, end, newPath);
                    }
                }

            }else{
                //now the node is visited just chose one of the path
                ArrayList<GameGraphNode<T>> gameGraphNodes = mGraph.get(current.node);
                if ( currentPath.size() < MAX_PATH_LENGTH) {
                    //generate a random to get a random path
                    Random random = new Random();
                    int randomEdge = random.nextInt( gameGraphNodes.size());
                    generate(gameGraphNodes.get(randomEdge), end, currentPath);
                }else {
//                    System.out.println( "eliminated");
//                    System.out.println(Arrays.toString(currentPath.toArray()));
                }
            }
        }


    }
}
