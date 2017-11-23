package EscapeGame;

/**
 * Denotes the actions in the game, each action requires certain pre state in the game
 * and every action causes a state change in the game state.
 * {@link EscapeScenarioCondition} class is used to achieve this.
 */
public class UserAction extends Game.UserAction{

    final private EscapeScenarioCondition mPreCondition;
    final private EscapeScenarioCondition mPostCondition;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String mName;

    public UserAction (String name, EscapeScenarioCondition preCondition, EscapeScenarioCondition postCondition){
        mName = name;
        mPreCondition = preCondition;
        mPostCondition = postCondition;
    }

    public EscapeScenarioCondition getPreCondition() {
        return mPreCondition;
    }

    public EscapeScenarioCondition getPostCondition() {
        return mPostCondition;
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
    public String getAction() {
        return mName;
    }
}
