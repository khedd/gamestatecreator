package Game;


public abstract class Condition {
    protected String mName;
    protected State  mState;

    /**
     * Creates a condition with {@link Condition.State#DONT_CARE}
     */
    public Condition(){
        mName = "";
        mState = State.DONT_CARE;
    }

    /**
     * Creates a condition with given name and state
     * @param name Required name of the condition to be accepted
     * @param state Required state of the condition to be accepted
     */
    public Condition(String name, State state){
        mName = name;
        mState = state;
    }

    /**
     * Creates a game condition from another game condition. Copy constructor
     * @param condition Another game condition to be copied from
     */
    public Condition(Condition condition) {
        mName = condition.mName;
        mState = condition.mState;
    }

    /**
     * if one of the states is don't care return true else the states must be equal else returns false
     * @param condition Another condition
     * @return equality of condition
     */
    public abstract boolean compare (Condition condition);
    /**
     * Applies the game state to the new game state
     * @param condition another condition to create a condition that
     *                      is subset of these two condition
     */
    public abstract void applyUpdate (Condition condition);
    /**
     * Applies the game state to the new game state
     * @param condition another game condition to create a condition that
     *                      is subset of these two condition
     * @return new game condition subset of two condition
     */
    public abstract Condition apply (Condition condition);

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Condition))
            return false;
        if (obj == this)
            return true;


        Condition rhs = (Condition) obj;
        if ( rhs.mState != mState)
            return false;
        return rhs.mName.compareTo(mName) == 0;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * @return Name of the condition
     */
    public String getName(){
        return mName;
    }

    /**
     * @return State of the {@link Condition}
     */
    public State getState (){
        return mState;
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
    public enum State {
        @SuppressWarnings("SpellCheckingInspection")DONT_CARE,
        TRUE,
        FALSE
    }
}
