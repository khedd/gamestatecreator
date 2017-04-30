import java.util.ArrayList;

/**
 * Generates a Graph from given rules and initial {@link GameState}
 */
class GameGraph {
    GameState mGameState;
    ArrayList<UserAction> mUserActions;
    GameGraph(GameState gameState){
        mGameState = gameState;
        mUserActions = new ArrayList<>();
    }

    void generate(){
        generate( mGameState);
    }

    private void generate (GameState gameState){
        for (UserAction uAction : mUserActions) {
            GameState newGameState = gameState.apply(uAction);
            if ( newGameState != null){
                System.out.println("***************************************");
                System.out.println ("Current room: " + newGameState.mCurrentCondition.mLevel.mName);
                System.out.println ("Current action: " + newGameState.mCurrentCondition.mGameAction.mName);
                System.out.println("Current items: " + newGameState.mCurrentCondition.mItems.toString());
                generate( newGameState);
            }
        }
    }

    void print(){

    }

    void addUserAction(UserAction userAction){
        mUserActions.add(userAction);
    }

    void setUserActions(ArrayList<UserAction> userActions) {
        mUserActions = userActions;
    }
}
