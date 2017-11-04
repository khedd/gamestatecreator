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
class BinarizedGameState {
    private final long mCurrentCondition;

    /**
     * Creates a new GameState using the {@link EscapeScenarioCondition}
     * @param binary escapeScenarioCondition of the game
     */
    private BinarizedGameState(long binary) {
        mCurrentCondition = binary;
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
    static BinarizedGameState fromMenu(StateBinarization stateBinarization){
        GameCondition levelMenu = new GameCondition("MENU", GameCondition.State.TRUE);
        GameCondition selectedMenu = new GameCondition("", GameCondition.State.FALSE);
        GameCondition gameAction = new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> pickedItems = new ArrayList<>();
        ArrayList<GameCondition> items = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioCondition = new EscapeScenarioCondition(
                levelMenu, selectedMenu, gameAction, items, pickedItems);

        return new BinarizedGameState( stateBinarization.binarize( escapeScenarioCondition));
    }

    /**
     *
     * @param stateBinarization
     * @param userAction
     * @return
     */
    BinarizedGameState apply (StateBinarization stateBinarization, UserAction userAction){
        EscapeScenarioCondition esc = stateBinarization.debinarize( mCurrentCondition);

        if (esc.compare ( userAction.mPreCondition)){
            EscapeScenarioCondition newCondition = esc.apply( userAction.mPostCondition);
            return new BinarizedGameState( stateBinarization.binarize( newCondition));
        }else
            return null;
    }

    @Override
    public String toString() {
        throw new RuntimeException("this is irrelevant");
    }

    /**
     * EscapeScenarioCondition property
     * @return EscapeScenarioCondition representing the state
     */
    public long getCondition() {
        return mCurrentCondition;
    }
}
