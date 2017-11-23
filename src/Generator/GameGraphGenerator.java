package Generator;

import EscapeGame.BinarizedGameState;
import EscapeGame.GameState;
import EscapeGame.StateBinarization;
import EscapeGame.UserAction;

import java.util.ArrayList;
import java.util.Set;

/**
 * Generates a GraphNode from given rules {@link UserAction} and initial {@link GameState}
 * GraphNode is generated for each path until no {@link UserAction} is applicable to the state
 * For a game optimally all states should converge to one state Exit
 * TODO does not generate a graph, just prints visited nodes
 */
public class GameGraphGenerator {
    private final BinarizedGameState mInitialGameState;
    private final ArrayList<UserAction> mAvailableUserActions;

    private GameGraph.GameGraphNode<Long> mGraphNode;
    private GameGraph.GameGraphNode<Long> mGraphStartNode;
//    private BinarizedGraphNode mGraphNode;
//    private BinarizedGraphNode mGraphStartNode;
    private StateBinarization mStateBinarization;
    private final GameGraph<Long> mGameGraph = new GameGraph<>();


    /**
     * Initializes with given game state
     * @param initialGameState Starting point of the graph
     */
    public GameGraphGenerator(BinarizedGameState initialGameState, StateBinarization stateBinarization){
        mInitialGameState = initialGameState;
        mStateBinarization = stateBinarization;
        mAvailableUserActions = new ArrayList<>();
        mGraphStartNode = new GameGraph.GameGraphNode<>();
        mGraphStartNode.vertex = initialGameState.getCondition();
//        mGraphNode = new BinarizedGraphNode(null, mInitialGameState);
//        mGraphStartNode = mGraphNode;
    }

    /**
     * reset the head of graph
     */
    public void reset (){
        mGraphStartNode = mGraphNode;
    }

    /**
     * Generates the {@link GameGraphGenerator} from given state
     */
    public void generate(){
        generate(mGraphStartNode);
    }

    /**
     * Generates a graph from given gameState
     * @param graphNode EscapeGame.GameState that the class is initialized with
     */
    private void generate (GameGraph.GameGraphNode<Long> graphNode){
        mGameGraph.addNode( graphNode.vertex);

        while ( mGameGraph.hasNext ()){
            Long node = mGameGraph.getNext();
            ArrayList<GameGraph.GameGraphNode<Long>> nodes = new ArrayList<>();
            BinarizedGameState bgs = new BinarizedGameState( node);
            for (UserAction uAction : mAvailableUserActions) {
                BinarizedGameState gs = bgs.apply( mStateBinarization, uAction);
                if ( gs != null){
                    GameGraph.GameGraphNode<Long> gameGraphNode = new GameGraph.GameGraphNode<>();
                    gameGraphNode.vertex = gs.getCondition(); // aka state
                    gameGraphNode.edge = uAction.getAction();
                    nodes.add( gameGraphNode);
                }
            }
            mGameGraph.addChildren( node, nodes);
        }
        mGameGraph.print ();
    }

    /**
     * TODO this method should print the graph formed by the {@link #generate()}
     */
    public void print(){
        Set<Long> states = mGameGraph.getVertices();
        for ( Long state: states){
//            System.out.println( state);
            System.out.println( mStateBinarization.debinarize( state).toString());
        }
//        mGraphNode.print();
    }

    /**
     * Adds the given user edge to the {@link GameGraphGenerator}
     * Used to fork new states from graph vertex
     * @param userAction User edge such as {start game, pick an item, return to menu}
     */
    public void addUserAction(UserAction userAction){
        mAvailableUserActions.add(userAction);
    }


    /**
     * Returns the all available user actions for this game
     * @return User Actions
     */
    ArrayList<UserAction> getUserActions (){
        return  mAvailableUserActions;
    }
}
