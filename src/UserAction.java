/**
 * Denotes the actions in the game, each action requires certain pre state in the game
 * and every action causes a state change in the game state.
 * {@link EscapeScenarioCondition} class is used to achieve this.
 */
class UserAction {

    EscapeScenarioCondition mPreCondition;
    EscapeScenarioCondition mPostCondition;
    private String mName;

    UserAction (String name, EscapeScenarioCondition preCondition, EscapeScenarioCondition postCondition){
        mName = name;
        mPreCondition = preCondition;
        mPostCondition = postCondition;
    }

//    GameState apply (GameState gameState){
//        if ( gameState.mCurrentRoom.equals(mPreRoom)){
//            GameState gameStateNew = new GameState ( gameState);
//            gameStateNew.mCurrentRoom = mPostRoom;
//
//
//            return gameStateNew;
//        }else
//            return null;
//    }
}
