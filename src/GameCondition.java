/**
 * GameCondition class that holds information such as condition being
 *  - Not important {@link GameCondition.State#DONT_CARE}
 *  - Must be fulfilled {@link GameCondition.State#TRUE}
 *  - Must not exist {@link GameCondition.State#FALSE}
 */
class GameCondition {
    String mName;
    State mState;

    /**
     * Creates a condition with {@link GameCondition.State#DONT_CARE}
     */
    GameCondition (){
        mName = "";
        mState = State.DONT_CARE;
    }

    /**
     * Creates a condition with given name and state
     * @param name Required name of the condition to be accepted
     * @param state Required state of the condition to be accepted
     */
    GameCondition(String name, State state){
        mName = name;
        mState = state;
    }

    GameCondition(GameCondition gameCondition) {
        mName = gameCondition.mName;
        mState = gameCondition.mState;
    }

    boolean compare (GameCondition gameCondition){
        return false;
    }

    GameCondition apply(GameCondition mSelected) {
        return null;
    }

    enum State {
        DONT_CARE,
        TRUE,
        FALSE
    }
}
