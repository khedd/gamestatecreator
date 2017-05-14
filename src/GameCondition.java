import java.util.Objects;

/**
 * GameCondition class that holds information such as condition being
 *  - Not important {@link GameCondition.State#DONT_CARE}
 *  - Must be fulfilled {@link GameCondition.State#TRUE}
 *  - Must not exist {@link GameCondition.State#FALSE}
 */
class GameCondition {
    /**
     * Name of the condition
     */
    private String mName;
    private State mState;

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

    /**
     * Creates a game condition from another game condition. Copy constructor
     * @param gameCondition Another game condition to be copied from
     */
    GameCondition(GameCondition gameCondition) {
        mName = gameCondition.mName;
        mState = gameCondition.mState;
    }

    /**
     * if one of the states is don't care return true else the states must be equal else returns false
     * @param gameCondition Another game condition
     * @return equality of game condition
     */
    boolean compare (GameCondition gameCondition){
        if ( mState == State.DONT_CARE || gameCondition.mState == State.DONT_CARE){
            return true;
        }else if ( mState == gameCondition.mState && Objects.equals(mName, gameCondition.mName)){
            return true;
        }else if ( gameCondition.mState == State.FALSE && !gameCondition.mName.equals(mName)){
            return true;
        }
        return false;
    }

    /**
     * @return State of the GameCondition
     */
    State getState (){
        return mState;
    }
    /**
     * Applies the game state to the new game state
     * @param gameCondition another game condition to create a condition that
     *                      is subset of these two condition
     */
    void applyUpdate(GameCondition gameCondition) {
        if ( gameCondition.mState == State.DONT_CARE){
            mName = "";
            mState = State.DONT_CARE;
        }
        else{
            mName = gameCondition.mName;
            mState = gameCondition.mState;
        }

    }

    /**
     * Applies the game state to the new game state
     * @param gameCondition another game condition to create a condition that
     *                      is subset of these two condition
     * @return new game condition subset of two condition
     */
    GameCondition apply(GameCondition gameCondition) {
        if ( gameCondition.mState == State.DONT_CARE){
            return new GameCondition(this);
        }else{
            return new GameCondition(gameCondition);
        }

    }

    /**
     * @return Name of the condition
     */
    String getName(){
        return mName;
    }

    /**
     * @return String representation of the class
     */
    @Override
    public String toString() {
        return mName + " " + mState.name();
    }
    {}
    /**
     * Game condition states
     * DON'T_CARE is used to annotate that every condition is okay
     */
    enum State {
        @SuppressWarnings("SpellCheckingInspection")DONT_CARE,
        TRUE,
        FALSE
    }
}
