/**
 * Denotes the actions in the game, each action requires certain pre state in the game
 * and every action causes a state change in the game state.
 * {@link EscapeScenarioCondition} class is used to achieve this.
 */
class UserAction {

    final EscapeScenarioCondition mPreCondition;
    final EscapeScenarioCondition mPostCondition;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String mName;

    UserAction (String name, EscapeScenarioCondition preCondition, EscapeScenarioCondition postCondition){
        mName = name;
        mPreCondition = preCondition;
        mPostCondition = postCondition;
    }

    /**
     * String representation of the class
     * @return Returns the name of the action
     */
    @Override
    public String toString() {
        return "User Action: " + mName;
    }

    /**
     * Gets the name of the Action
     * @return Name of the action
     */
    String getAction() {
        return mName;
    }
}
