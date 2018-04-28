import graph.GameGraph;
import graph.ai.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Generates a GraphNode from given rules {@link UserAction} and initial {@link GameState}
 * GraphNode is generated for each path until no {@link UserAction} is applicable to the state
 * For a game optimally all states should converge to one state Exit
 * TODO does not generate a graph, just prints visited nodes
 */
class BinarizedGameGraphGenerator {
    private final BinarizedGameState mInitialGameState;
    private final ArrayList<UserAction> mAvailableUserActions;
    private BinarizedGraphNode mGraphNode;
    private BinarizedGraphNode mGraphStartNode;
    private StateBinarization mStateBinarization;
    private final GameGraph<Long> mGameGraph = new GameGraph<>();


    /**
     * Initializes with given game state
     * @param initialGameState Starting point of the graph
     */
    BinarizedGameGraphGenerator(BinarizedGameState initialGameState, StateBinarization stateBinarization){
        mInitialGameState = initialGameState;
        mStateBinarization = stateBinarization;
        mAvailableUserActions = new ArrayList<>();
        mGraphNode = new BinarizedGraphNode(null, mInitialGameState);
        mGraphStartNode = mGraphNode;
    }

    /**
     * reset the head of graph
     */
    void reset (){
        mGraphStartNode = mGraphNode;
    }

    /**
     * Generates the GameGraphGenerator from given state
     */
    void generate(){
        generate(mGraphStartNode);
    }

    /**
     * Generates a graph from given gameState
     * @param binarizedGraphNode GameState that the class is initialized with
     */
    private void generate (BinarizedGraphNode binarizedGraphNode){
        mGameGraph.addNode( binarizedGraphNode.getVertex().getCondition());
        mGameGraph.setStartNode( binarizedGraphNode.getVertex().getCondition());
        while ( mGameGraph.hasNext ()){
            Long node = mGameGraph.getNext();
            ArrayList<GameGraph.GameGraphNode<Long>> nodes = new ArrayList<>();
            BinarizedGameState bgs = new BinarizedGameState( node);
            for (UserAction uAction : mAvailableUserActions) {
                BinarizedGameState gs = bgs.apply( mStateBinarization, uAction);
                if ( gs != null){
                    GameGraph.GameGraphNode<Long> gameGraphNode = new GameGraph.GameGraphNode<>(gs.getCondition(), uAction.getAction());
                    nodes.add( gameGraphNode);
                }
            }
            mGameGraph.addChildren( node, nodes);
        }
        mGameGraph.finalize((long) -1);

        //TODO totally experimental


    }

    /**
     * calls MCTS from the graph start to the end until maxLen is reached
     * @param maxLen max len to be obtained from the MCTS, if it reaches a terminal node
     *               it ends but may get in loop so better to set this variable
     * @param scoreTable scoring table to be used to weight the moves that MCTS take
     *                   if empty {@link MaxPathScoring} will be used which is to weight
     *                   the longest unique path
     * @param defaultScore default score to be used if the action does not exist in table
     *                     only used if scoreTable is set
     * @return the path generated using these settings
     */
    ArrayList<GameGraph.GameGraphNode<Long>> testWMCTS (int maxLen, HashMap<String, Double> scoreTable, double defaultScore){
        // initialize the policies to be used in MCTS

        //scoring policy is a bit tricky
        ScoringPolicy<Long> scoringPolicy;
        if ( scoreTable == null || scoreTable.isEmpty()) {
            scoringPolicy = new MaxPathScoring<>();
        }else {
            scoringPolicy = new WeightedScoring<>( scoreTable, defaultScore);
        }
        RolloutPolicy<Long> randomRollout = new RandomRollout<>(scoringPolicy, 100);
        SelectionCriteria<Long> selectionCriteria = new UCB1<>(4.0);

        //get the start and end nodes to simulate from correct
        GameGraph.GameGraphNode<Long> currentNode = mGameGraph.getStartNode();
        GameGraph.GameGraphNode<Long> endNode = mGameGraph.getEndNode();

        //this will be used to see the path visited
        ArrayList<GameGraph.GameGraphNode<Long>> visitedNodes = new ArrayList<>();
        visitedNodes.add( currentNode);

        //run MCTS until maxLen, create a new instance every time as MCTS will give us
        // one node at a time
        for (int i = 0; i < maxLen; i++) {
            MCTS<Long> mcts = new MCTS<>(mGameGraph.getGraph(), currentNode);
            mcts.setStateBinarizer( mStateBinarization);
            mcts.setPolicies (scoringPolicy, randomRollout, selectionCriteria);
            currentNode = mcts.run();
            visitedNodes.add( currentNode);
            if (currentNode == null || currentNode.node.equals(endNode.node)){
                break;
            }
//            System.out.println( "After MCTS:" + currentNode);
        }
        return visitedNodes;
    }


   /**
     * Adds the given user action to the GameGraphGenerator
     * Used to fork new states from graph node
     * @param userAction User action such as {start game, pick an item, return to menu}
     */
    void addUserAction(UserAction userAction){
        mAvailableUserActions.add(userAction);
    }


    /**
     * Returns the all available user actions for this game
     * @return User Actions
     */
    ArrayList<UserAction> getUserActions (){
        return  mAvailableUserActions;
    }

    /**
     * todo not sure still this method works as graphs with cycles are not adequate
     */
    public void printStatistics() {
        System.out.println( "-----------Statistics-----------");
        mGameGraph.calculateCyclometicComplexity();
    }

    /**
     * plays the given sequences
     * @param sequences composed of strings of actions
     */
    public void playSequences(ArrayList<ArrayList<String>> sequences) {
        System.out.println( "Playing Sequences");
        for (ArrayList<String> sequence : sequences) {
            _playSequence ( sequence);
        }
    }

    /**
     * play the given sequence
     * @param sequence composed of strings of actions
     */
    public void playSequence (ArrayList<String> sequence){
        _playSequence ( sequence);
    }

    /**
     * resets the coverage caused by playing sequences
     */
    public void resetCoverage (){
        mGameGraph.resetCoverage();
    }

    private void _playSequence(ArrayList<String> sequence) {
        reset();
        Long currNode = mGraphNode.getVertex().getCondition();
        for (String seq : sequence) {
            Long nextNode = mGameGraph.visit( currNode, seq);
            if ( nextNode == null) {
                System.out.println("cannot play the sequence: "
                        + Arrays.toString( sequence.toArray())
                        + "Error at: " + seq);
                mGameGraph.printAdjacencyListRow( currNode);
                break;
            }
            currNode = nextNode;
        }

    }

    /**
     * returns the edge coverage percent in range [0-100]
     * @return a double value indicating the coverage amount
     */
    public double getEdgeCoveragePercent (){
        return mGameGraph.getEdgeCoveragePercent();
    }

    /**
     * prints the coverage statistics
     */
    public void printCoverage() {
        mGameGraph.printEdgeCoverage ();
    }

    /**
     * // FIXME: 30.12.2017 currently bugged
     */
    public void printBasisPaths (){
        mGameGraph.printBasisPaths();
    }
}
