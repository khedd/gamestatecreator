import java.util.ArrayList;

/**
 * Generates a Graph from given rules {@link UserAction} and initial {@link GameState}
 * Graph is generated for each path until no {@link UserAction} is applicable to the state
 * For a game optimally all states should converge to one state Exit
 * TODO does not generate a graph, just prints visited nodes
 */
class GameGraph {
    private final GameState mGameState;
    private final ArrayList<UserAction> mUserActions;

    /**
     * Initializes with given game state
     * @param gameState Starting point of the graph
     */
    GameGraph(GameState gameState){
        mGameState = gameState;
        mUserActions = new ArrayList<>();
    }

    /**
     * Generates the GameGraph from given state
     */
    void generate(){
        generate( mGameState);
    }

    /**
     * Generates a graph from given gameState
     * @param gameState GameState that the class is initialized with
     */
    private void generate (GameState gameState){
        for (UserAction uAction : mUserActions) {
            GameState newGameState = gameState.apply(uAction);
            if ( newGameState != null){
                System.out.println("***************************************");
                System.out.println( newGameState.toString());
                generate( newGameState);
            }
        }
    }

    /**
     * TODO this method should print the graph formed by the {@link #generate()}
     */
    @SuppressWarnings({"unused", "EmptyMethod"})
    void print(){

    }

    /**
     * Adds the given user action to the GameGraph
     * Used to fork new states from graph node
     * @param userAction User action such as {start game, pick an item, return to menu}
     */
    void addUserAction(UserAction userAction){
        mUserActions.add(userAction);
    }
}
