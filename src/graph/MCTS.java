package graph;

import java.util.*;

public class MCTS<T> {
    int mSimulationLength = 100;
    SelectionCriteria<T>           mSelectionCriteria    = null;
    RolloutPolicy<T>               mRolloutPolicy        = null;
    ScoringPolicy<T>               mScoringPolicy        = null;

    T  mStartNode;

    /**
     * stores the parent nodes used in back propagation, updated in expansion
     */
    private final HashMap<T, ArrayList<T>> mParentMap = new HashMap<>();
    private final HashMap<T, StateInformation> mVisible = new HashMap<>(); ///adjacency list
    private int mTotalVisits = 0;

    private final HashMap<T, ArrayList<GameGraph.GameGraphNode<T>>> mGraph;

    public MCTS(HashMap<T, ArrayList<GameGraph.GameGraphNode<T>>> graph, T startNode){
        mGraph = graph;
        mStartNode = startNode;
    }



    /**
     * updates the parent map of the node used in back propagation
     * @param parent parent to be added
     * @param child child to be updated
     */
    private void updateParentMap (T parent, T child){
        ArrayList<T> arrayList = mParentMap.get(child);
        if ( arrayList == null){
            mParentMap.put( child, new ArrayList<>());
        }
        mParentMap.get(child).add( parent);

    }

    /**
     * @return the highest scoring child of the rootNode/startNode
     */
    private T getHighestChild (){
        ArrayList<GameGraph.GameGraphNode<T>> gameGraphNodes = mGraph.get(mStartNode);
        double maxScore = -1;
        T maxChild = null;
        for (GameGraph.GameGraphNode<T> gameGraphNode: gameGraphNodes){
            double score = mVisible.get(gameGraphNode.node).score;
            if ( score > maxScore){
                maxScore = score;
                maxChild = gameGraphNode.node;
            }
        }
        return maxChild;
    }
    /**
     * selects a node using the {@link SelectionCriteria}
     * @return node selected from the selection criteria
     */
    private T selection(){
        T maxNode = null;
        double maxScore = Double.MIN_VALUE;
        for (Map.Entry<T, StateInformation> nodes: mVisible.entrySet()){
            double score = mSelectionCriteria.calculate(mTotalVisits, nodes.getValue());
            if ( score > maxScore){
                maxScore = score;
                maxNode = nodes.getKey();
            }
        }
        return maxNode;
    }

    /**
     * adds its leaf nodes to the mVisible
     * @param node Node to expand to visible
     */
    private void expansion(T node){
        ArrayList<GameGraph.GameGraphNode<T>> gameGraphNodes = mGraph.get(node);
        if ( gameGraphNodes != null && !gameGraphNodes.isEmpty()){
            for( GameGraph.GameGraphNode<T> gameGraphNode: gameGraphNodes){
                if ( mVisible.get(gameGraphNode.node) == null)
                    mVisible.put( gameGraphNode.node, new StateInformation());
                updateParentMap( node, gameGraphNode.node);
            }
        }else {
            System.out.println( "node does not have any arraylist associated");
        }
    }

    /**
     * simulates on the given node wrt {@link RolloutPolicy}
     * @param node
     * @return score returned from scoring policy
     */
    private double rollout(T node){
        mRolloutPolicy.reset ();
        T currentNode = node;
        ArrayList<T> nodesVisited = new ArrayList<>();
        for (int i = 0; i < mSimulationLength; i++) {
            //get currentNodes children
            nodesVisited.add( currentNode);
            ArrayList<GameGraph.GameGraphNode<T>> gameGraphNodes = mGraph.get(currentNode);
            if ( gameGraphNodes != null && !gameGraphNodes.isEmpty()){
                //update the current node from the selection
                currentNode = mRolloutPolicy.select(currentNode, gameGraphNodes);
            }else {
                break; // we have visited a terminal node
            }
        }
        double totalScore = mScoringPolicy.evaluate( nodesVisited);

        return totalScore + 1;
    }

    /**
     * back propagate the found score to the parent nodes
     * @param node current node start of back propagation
     * @param score score to back propagate
     */
    private void backPropagate(T node, double score){
        //hold a visited set to cancel loops
        Set<T> visited = new HashSet<>();
        ArrayList<T> currentNodes = new ArrayList<>();
        currentNodes.add( node);
        while (!currentNodes.isEmpty()){
            T currentNode = currentNodes.remove(0);
            if (!visited.contains( currentNode)) {
                visited.add( currentNode);
                update ( node, score);
                ArrayList<T> parents = mParentMap.get( currentNode);
                if ( parents != null)
                    currentNodes.addAll( parents); //continue with parents
                //until no parent left

            }
        }

        //back propagate until is reaches the
    }

    /**
     * updates the current node in the mVisible
     * @param node
     * @param score
     */
    private void update(T node, double score) {
        StateInformation stateInformation = mVisible.get(node);
        stateInformation.visit++;
        stateInformation.score = Math.max( stateInformation.score, score);
    }

    /**
     * runs the monte carlo simulations
     */
    public T run(){
        if ( mScoringPolicy == null || mRolloutPolicy == null || mSelectionCriteria == null){
            throw new RuntimeException("Missing Policy");
        }
        //start expansion from the start node
        expansion( mStartNode);
        for (int i = 0; i < mSimulationLength; i++) {
            treeWalk ();
        }
        return getHighestChild();
        //return the node of the parent that has the max score

    }

    private void treeWalk() {
        T selection = selection();

        //fixme nullptr is thrown here so no selection is returned?
        if ( mVisible.get(selection).visit != 0){
            expansion( selection);
        }else{
            double score = rollout( selection);
            backPropagate( selection, score);
            mTotalVisits++;
        }
        //select a new node from its children if it exists else chose another
        //increments its count to make less viable?
        //do a simulation on the node and get the result
        //back propagate the result
    }

    public void setPolicies(ScoringPolicy<T> scoringPolicy, RolloutPolicy<T> rolloutPolicy, SelectionCriteria<T> selectionCriteria) {
        mScoringPolicy = scoringPolicy;
        mRolloutPolicy = rolloutPolicy;
        mSelectionCriteria = selectionCriteria;
    }

    public abstract static class SelectionCriteria<T>{
        /**
         * calculates a nodes value
         * @param totalVisits Total Number of visits in total tree
         * @param stateInformation Node state
         * @return selection score
         */
        public abstract double calculate(int totalVisits, StateInformation stateInformation);
    }

    public abstract static class RolloutPolicy<T> {
        final ScoringPolicy<T> mScoringPolicy;

        public RolloutPolicy(ScoringPolicy scoringPolicy){
            mScoringPolicy = scoringPolicy;
        }
        /**
         * resets the current information
         */
        public abstract void reset();

        /**
         * select a new node from the child nodes
         * @param node Current Node
         * @param children children
         * @return
         */
        public abstract T select(T node, ArrayList<GameGraph.GameGraphNode<T>> children);
    }

    public abstract static class ScoringPolicy<T> {
        public abstract double evaluate(T node);
        public abstract double evaluate(ArrayList<T> nodes);
    }

    public static class StateInformation {
        public int visit = 0;
        public double score = 0;
    }
}
