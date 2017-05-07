import java.util.ArrayList;
/**
 * Generates a GraphNode from given rules {@link UserAction} and initial {@link GameState}
 * GraphNode is generated for each path until no {@link UserAction} is applicable to the state
 * For a game optimally all states should converge to one state Exit
 * TODO does not generate a graph, just prints visited nodes
 */
class GameGraphGenerator {
    private final GameState mInitialGameState;
    private final ArrayList<UserAction> mAvailableUserActions;
    private GraphNode mGraphNode;

    /**
     * Initializes with given game state
     * @param initialGameState Starting point of the graph
     */
    GameGraphGenerator(GameState initialGameState){
        mInitialGameState = initialGameState;
        mAvailableUserActions = new ArrayList<>();
    }

    /**
     * Generates the GameGraphGenerator from given state
     */
    void generate(){
        mGraphNode = new GraphNode(null, mInitialGameState);
        generate(mGraphNode);
    }

    /**
     * Generates a graph from given gameState
     * @param graphNode GameState that the class is initialized with
     */
    private void generate (GraphNode graphNode){
        for (UserAction uAction : mAvailableUserActions) {
            GameState newGameState = graphNode.getVertex().apply(uAction);
            if ( newGameState != null){
//                System.out.println("***************************************");
//                System.out.println( newGameState.toString());
                GraphNode newGraphNode = new GraphNode(uAction, newGameState);
                if (!graphNode.AddNode( newGraphNode)) {
                    generate( newGraphNode);
                }
            }
        }
    }

    /**
     * TODO this method should print the graph formed by the {@link #generate()}
     */
    void print(){
        mGraphNode.print ();
    }

    /**
     * Adds the given user action to the GameGraphGenerator
     * Used to fork new states from graph node
     * @param userAction User action such as {start game, pick an item, return to menu}
     */
    void addUserAction(UserAction userAction){
        mAvailableUserActions.add(userAction);
    }
}
