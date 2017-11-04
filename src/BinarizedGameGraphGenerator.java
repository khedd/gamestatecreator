import java.util.ArrayList;

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
        for (UserAction uAction : mAvailableUserActions) {
            BinarizedGameState newGameState = binarizedGraphNode.getVertex().apply(mStateBinarization, uAction);
            if ( newGameState != null){
                BinarizedGraphNode newGraphNode = new BinarizedGraphNode(uAction.getAction(), newGameState);

                if (binarizedGraphNode.AddNode( newGraphNode)) {
                    generate( newGraphNode);
                }
            }
        }
    }

    /**
     * TODO this method should print the graph formed by the {@link #generate()}
     */
    void print(){
        mGraphNode.getPaths ();
//        mGraphNode.print();
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
     *
     * @param startSeq initializes the graph from given sequence
     *                 also used to test whether sequence is valid
     * @return how many of the sequence is done
     */
    public int playSequence(ArrayList<String> startSeq) {
        BinarizedGraphNode node = mGraphStartNode;
        int seqCounter = 0;
        for (String seq: startSeq) {
            BinarizedGraphNode testNode = testCurrSeq(node, seq);
            if ( testNode == null){
                return seqCounter;
            }else{
                node.AddNode( testNode);
                node = testNode;
                seqCounter++;
            }

        }
        //shift the start node so that generation will begin after this node
        mGraphStartNode = node;
        return seqCounter;
    }


    private BinarizedGraphNode testCurrSeq (BinarizedGraphNode graphNode, String currSeq){
        for (UserAction uAction : mAvailableUserActions) {
            BinarizedGameState newGameState = graphNode.getVertex().apply(mStateBinarization, uAction);
            if ( newGameState != null){
                BinarizedGraphNode newGraphNode = new BinarizedGraphNode(uAction.getAction(), newGameState);
                if ( newGraphNode.toSimplifiedString().compareTo( currSeq) == 0){
                    return  newGraphNode;
                }
            }
        }
        return null;
    }
}
