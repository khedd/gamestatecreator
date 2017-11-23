package EscapeGame;

import Game.Condition;

import java.util.Objects;

/**
 * EscapeGame.GameCondition class that holds information such as condition being
 *  - Not important {@link GameCondition.State#DONT_CARE}
 *  - Must be fulfilled {@link GameCondition.State#TRUE}
 *  - Must not exist {@link GameCondition.State#FALSE}
 */
public class GameCondition extends Condition {
    /**
     * Name of the condition
     */
    private String mName;
    private State mState;

    public GameCondition (){
        super();
    }

    public GameCondition(GameCondition gameCondition) {
        super( gameCondition);
    }

    public GameCondition(String name, State state) {
        super( name, state);
    }

    /**
     * if one of the states is don't care return true else the states must be equal else returns false
     * @param condition Another condition
     * @return equality of condition
     */
    @Override
    public boolean compare(Condition condition) {
        GameCondition gameCondition = (GameCondition) condition;
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
     * Applies the game state to the new game state
     * @param condition another condition to create a condition that
     *                      is subset of these two condition
     */
    @Override
    public void applyUpdate(Condition condition) {
        GameCondition gameCondition = (GameCondition) condition;
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
     * @param condition another game condition to create a condition that
     *                      is subset of these two condition
     * @return new game condition subset of two condition
     */
    @Override
    public GameCondition apply(Condition condition) {
        GameCondition gameCondition = (GameCondition)condition;
        if ( gameCondition.mState == State.DONT_CARE){
            return new GameCondition(this);
        }else{
            return new GameCondition(gameCondition);
        }
    }

    }
