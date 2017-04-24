import java.util.ArrayList;

/**
 * Generates a Graph from given rules and initial {@link GameState}
 */
class GameGraph {
    GameState mGameState;
    ArrayList<UserAction> mUserActions;
    GameGraph(GameState gameState){
        mGameState = gameState;
    }

    void generate(){
        for (UserAction uAction : mUserActions) {
             GameState newGameState = uAction.apply(mGameState);
             if ( newGameState != null){
                System.out.println ("Current room: " + newGameState.mCurrentRoom);
             }
        }
    }

    void print(){

    }

    void setUserActions(ArrayList<UserAction> userActions) {
        mUserActions = userActions;
    }
}
