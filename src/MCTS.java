

import graph.GameGraph;
import graph.ai.RolloutPolicy;
import graph.ai.ScoringPolicy;
import graph.ai.SelectionCriteria;
import graph.ai.StateInformation;

import java.util.*;

public class MCTS<T> {
    /**
     * How many simulations will be done in one MCTS run
     */
    @SuppressWarnings("FieldCanBeLocal")
    private int mSimulationLength = 100;
    /**
     * How deep will we go at each rollout while simulating a node
     */
    @SuppressWarnings("FieldCanBeLocal")
    private int mRolloutLength = 5;
    /**
     * Tree policy node selection criteria
     */
    private SelectionCriteria<T> mSelectionCriteria    = null;
    /**
     * Rollout Policy, how we will choose the next node during simulation
     */
    private RolloutPolicy<T> mRolloutPolicy        = null;
    /**
     * How to score our simulation
     */
    private ScoringPolicy<T> mScoringPolicy        = null;

    /**
     * The node that MCTS will run on
     */
    private GameGraph.GameGraphNode<T> mStartNode;

    /**
     * stores the parent nodes used in back propagation, updated in expansion
     */
    private final HashMap<GameGraph.GameGraphNode<T>, ArrayList<GameGraph.GameGraphNode<T>>> mParentMap = new HashMap<>();
    private final HashMap<GameGraph.GameGraphNode<T>, StateInformation> mVisible = new HashMap<>(); ///adjacency list

    /**
     * total number of visits during selection
     */
    private int mTotalVisits = 0;
    /**
     * PreLoaded graph this is uncommon in MCTS as we should not know the whole graph before
     */
    private final HashMap<T, ArrayList<GameGraph.GameGraphNode<T>>> mGraph;

    /**
     * If utilized records past MCTS runs so that we can increase coverage
     */
    private Memory<T> mMemory = null;


    /**
     * Initialize a MCTS which will work on the given Graph Structure from the given Starting Node
     * @param graph Graph that MCTS will simulate to get the best possible action
     * @param startNode The starting node for simulations
     */
    MCTS(HashMap<T, ArrayList<GameGraph.GameGraphNode<T>>> graph, GameGraph.GameGraphNode<T> startNode){
        mGraph = graph;
        mStartNode = startNode;
    }

    /**
     * Sets a memory that can be used in MCTS, this holds the previous visits
     * @param memory Memory of previous runs, if null will not be utilized
     */
    void setMemory ( Memory<T> memory){
        mMemory = memory;
    }

    /**
     * updates the parent map of the node used in back propagation
     * @param parent parent to be added
     * @param child child to be updated
     */
    private void updateParentMap (GameGraph.GameGraphNode<T> parent, GameGraph.GameGraphNode<T>  child){
        ArrayList<GameGraph.GameGraphNode<T>> arrayList = mParentMap.get(child);
        if ( arrayList == null){
            mParentMap.put( child, new ArrayList<>());
        }
        mParentMap.get(child).add( parent);

    }

    /**
     * @return the highest scoring child of the rootNode/startNode
     */
    private GameGraph.GameGraphNode<T> getHighestChild (){
        ArrayList<GameGraph.GameGraphNode<T>> gameGraphNodes = mGraph.get(mStartNode.node);
        double maxScore = -Double.MAX_VALUE;
        GameGraph.GameGraphNode<T> maxChild = useMemoryChild(mStartNode);
        if ( maxChild == null) {
            for (GameGraph.GameGraphNode<T> gameGraphNode : gameGraphNodes) {
                double score = mVisible.get(gameGraphNode).score;
                if (score > maxScore) {
                    maxScore = score;
                    maxChild = gameGraphNode;
                }
            }
        }
        updateMemory ( mStartNode, maxChild);
        return maxChild;
    }

    private void updateMemory (GameGraph.GameGraphNode<T> parent, GameGraph.GameGraphNode<T> child){
        if ( mMemory != null) {
            mMemory.update(parent, child);
        }
    }

    /**
     * uses the memory if applicable and returns a child suggested from memory
     * @param parent
     * @return null if memory is null or not active, a child object chosen by memory
     */
    private GameGraph.GameGraphNode<T> useMemoryChild(GameGraph.GameGraphNode<T> parent){
        if ( mMemory != null){
            if ( mMemory.useMemory()){
                return mMemory.selectChild( parent);
            }
        }
        return null;
    }

    /**
     * selects a node using the {@link SelectionCriteria}
     * @return node selected from the selection criteria
     */
    private GameGraph.GameGraphNode<T> selection(){
        GameGraph.GameGraphNode<T> maxNode = null;
        double maxScore = Double.MIN_VALUE;
        for (Map.Entry<GameGraph.GameGraphNode<T>, StateInformation> nodes: mVisible.entrySet()){
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
    private void expansion(GameGraph.GameGraphNode<T> node){
        ArrayList<GameGraph.GameGraphNode<T>> gameGraphNodes = mGraph.get(node.node);
        if ( gameGraphNodes != null && !gameGraphNodes.isEmpty()){
            for( GameGraph.GameGraphNode<T> gameGraphNode: gameGraphNodes){
                if ( mVisible.get(gameGraphNode) == null)
                    mVisible.put( gameGraphNode, new StateInformation());
                updateParentMap( node, gameGraphNode);
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
    private double rollout(GameGraph.GameGraphNode<T> node){
        mRolloutPolicy.reset ();
        GameGraph.GameGraphNode<T> currentNode = node;
        ArrayList<GameGraph.GameGraphNode<T>> nodesVisited = new ArrayList<>();
        for (int i = 0; i < mRolloutLength; i++) {
            //get currentNodes children
            nodesVisited.add( currentNode);
            ArrayList<GameGraph.GameGraphNode<T>> gameGraphNodes = mGraph.get(currentNode.node);
            if ( gameGraphNodes != null && !gameGraphNodes.isEmpty()){
                //update the current node from the selection
                currentNode = mRolloutPolicy.select(currentNode, gameGraphNodes);
            }else {
                break; // we have visited a terminal node
            }
        }
        double totalScore = 0;
        totalScore = mScoringPolicy.evaluate( nodesVisited);
//        if ( mStateBinarization == null) {
//            // TODO: 27.04.2018 this currently cannot be called as we are using action
////            mScoringPolicy.evaluate(nodesVisited);
//        }else {
//            totalScore = evaluateWeights ( nodesVisited);
//        }
        return totalScore + 1;
    }

    /**
     * back propagate the found score to the parent nodes
     * @param node current node start of back propagation
     * @param score score to back propagate
     */
    private void backPropagate(GameGraph.GameGraphNode<T>  node, double score){
        //hold a visited set to cancel loops
        Set<GameGraph.GameGraphNode<T> > visited = new HashSet<>();
        ArrayList<GameGraph.GameGraphNode<T> > currentNodes = new ArrayList<>();
        currentNodes.add( node);
        while (!currentNodes.isEmpty()){
            GameGraph.GameGraphNode<T>  currentNode = currentNodes.remove(0);
            if (!visited.contains( currentNode)) {
                visited.add( currentNode);
                update ( node, score);
                ArrayList<GameGraph.GameGraphNode<T> > parents = mParentMap.get( currentNode);
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
    private void update(GameGraph.GameGraphNode<T>  node, double score) {
        StateInformation stateInformation = mVisible.get(node);
        stateInformation.visit++;
        stateInformation.score = Math.max( stateInformation.score, score);
    }

    /**
     * runs the monte carlo simulations
     */
    public GameGraph.GameGraphNode<T> run(){
        if ( mScoringPolicy == null || mRolloutPolicy == null || mSelectionCriteria == null){
            throw new RuntimeException("Missing Policy");
        }
        //start expansion from the start node
        expansion( mStartNode);
        if ( mVisible.size() > 1) {
            for (int i = 0; i < mSimulationLength; i++) {
                treeWalk();
            }
        }
        return getHighestChild();
        //return the node of the parent that has the max score

    }

    private void treeWalk() {
        GameGraph.GameGraphNode<T> selection = selection();

        //todo this is removed as there will be single expansion step
        //where we add all the child nodes
        if ( false && mVisible.get(selection).visit != 0){
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

}
