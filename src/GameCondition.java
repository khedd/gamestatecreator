import java.util.Objects;

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

    /**
     * if one of the states is don't care return true else the states must be equal else returns false
     * @param gameCondition Another game condition
     * @return equality of game condition
     * TODO add a false check create table for better checking
     */
    boolean compare (GameCondition gameCondition){
        if ( mState == State.DONT_CARE || gameCondition.mState == State.DONT_CARE){
            return true;
        }else if ( mState == gameCondition.mState && Objects.equals(mName, gameCondition.mName)){
            return true;
        }
        return false;
    }

    /**
     * Applies the game state to the new game state
     * @param gameCondition
     * @return
     */
    GameCondition apply(GameCondition gameCondition) {
        if ( gameCondition.mState == State.DONT_CARE){
            return new GameCondition(this);
        }else{
            return new GameCondition(gameCondition);
        }

    }

    @Override
    public String toString() {
        return mName + " " + mState.name();
    }

    enum State {
        DONT_CARE,
        TRUE,
        FALSE
    }
}
