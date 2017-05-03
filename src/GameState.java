import java.util.ArrayList;

/**
 * State of the Game
 * Game state class that holds the:
 *  - Current Room
 *  - Current Selection
 *  - Current Items
 *  - Picked Items
 *
 * TODO compare game states to find same states
 */
class GameState {
    private final EscapeScenarioCondition mCurrentCondition;

    /**
     * Creates a new GameState using the {@link EscapeScenarioCondition}
     * @param escapeScenarioCondition Current condition of the game
     */
    private GameState(EscapeScenarioCondition escapeScenarioCondition) {
        mCurrentCondition = escapeScenarioCondition;
    }

    /**
     * Creates a new GameState that starts from the Menu with
     *  - Level is MENU
     *  - Nothing is selected
     *  - Action is PICK
     *  - No picked items
     *  - No items
     * @return GameState that fulfills the above condition
     */
    static GameState fromMenu(){
        GameCondition levelMenu = new GameCondition("MENU", GameCondition.State.TRUE);
        GameCondition selectedMenu = new GameCondition("", GameCondition.State.FALSE);
        GameCondition gameAction = new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> pickedItems = new ArrayList<>();
        ArrayList<GameCondition> items = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioCondition = new EscapeScenarioCondition(
                levelMenu, selectedMenu, gameAction, pickedItems, items);
        return new GameState( escapeScenarioCondition);
    }

    /**
     * Copy constructor, uses {@link EscapeScenarioCondition} to copy
     * @param gameState Another game state
     */
    @SuppressWarnings("unused")
    GameState (GameState gameState){
        mCurrentCondition = new EscapeScenarioCondition(gameState.mCurrentCondition);
    }

    /**
     * Creates a new GameState after applying user action
     * @param userAction {@link UserAction} checks if {@link UserAction#mPreCondition} holds
     *                                     and applies {@link UserAction#mPostCondition}
     * @return null if preCondition is not applicable else a new game state of applies PostCondition
     */
    GameState apply ( UserAction userAction){
        if (mCurrentCondition.compare ( userAction.mPreCondition)){
            EscapeScenarioCondition newCondition = mCurrentCondition.apply( userAction.mPostCondition);
            return new GameState( newCondition);
        }else
            return null;
    }

    @Override
    public String toString() {
        return mCurrentCondition.toString();
    }
}
