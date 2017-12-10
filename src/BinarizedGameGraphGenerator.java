import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

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

        while ( mGameGraph.hasNext ()){
            Long node = mGameGraph.getNext();
            ArrayList<GameGraph.GameGraphNode<Long>> nodes = new ArrayList<>();
            BinarizedGameState bgs = new BinarizedGameState( node);
            for (UserAction uAction : mAvailableUserActions) {
                BinarizedGameState gs = bgs.apply( mStateBinarization, uAction);
                if ( gs != null){
                    GameGraph.GameGraphNode<Long> gameGraphNode = new GameGraph.GameGraphNode<>();
                    gameGraphNode.node = gs.getCondition(); // aka state
                    gameGraphNode.action = uAction.getAction();
                    nodes.add( gameGraphNode);
                }
            }
            mGameGraph.addChildren( node, nodes);
        }
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

    public void printStatistics() {
        // TODO: 10.12.2017
        System.out.println( "-----------Statistics-----------");
        mGameGraph.calculateCyclometicComplexity();
    }

    public void playSequences(ArrayList<ArrayList<String>> sequences) {
        // TODO: 10.12.2017
        System.out.println( "Playing Sequences");
        for (ArrayList<String> sequence : sequences) {
            playSequence ( sequence);
        }
    }

    private void playSequence(ArrayList<String> sequence) {
        reset();
        Long currNode = mGraphNode.getVertex().getCondition();
        for (String seq : sequence) {
            Long nextNode = mGameGraph.visit( currNode, seq);
            if ( nextNode == null) {
                System.out.println("cannot play the sequence: "
                        + Arrays.toString( sequence.toArray())
                        + "Error at: " + seq);
                mGameGraph.printAvailableActions( currNode);
                break;
            }
            currNode = nextNode;
        }

    }

    public void printCoverage() {
        // TODO: 10.12.2017
        mGameGraph.printEdgeCoverage ();
    }
}