package EscapeGame;

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
public class BinarizedGameState {
    private final long mCurrentCondition;

    /**
     * Creates a new EscapeGame.GameState using the {@link EscapeScenarioCondition}
     * @param binary escapeScenarioCondition of the game
     */
    public BinarizedGameState(long binary) {
        mCurrentCondition = binary;
    }


    /**
     * Creates a new EscapeGame.GameState that starts from the Menu with
     *  - Level is MENU
     *  - Nothing is selected
     *  - Action is PICK
     *  - No picked items
     *  - No items
     * @return EscapeGame.GameState that fulfills the above condition
     */
    public static long fromMenu(StateBinarization stateBinarization){
        GameCondition levelMenu = new GameCondition(AvailableRooms.MENU.name(), GameCondition.State.TRUE);
        GameCondition selectedMenu = new GameCondition("", GameCondition.State.FALSE);
        GameCondition gameAction = new GameCondition(AvailableActions.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> pickedItems = new ArrayList<>();
        ArrayList<GameCondition> items = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioCondition = new EscapeScenarioCondition(
                levelMenu, selectedMenu, gameAction, items, pickedItems);

        return stateBinarization.binarize( escapeScenarioCondition);
    }

    /**
     *
     * @param stateBinarization
     * @param userAction
     * @return
     */
    public BinarizedGameState apply (StateBinarization stateBinarization, UserAction userAction){
        EscapeScenarioCondition esc = stateBinarization.debinarize( mCurrentCondition);

        if (esc.compare ( userAction.getPreCondition())){
            EscapeScenarioCondition newCondition = esc.apply( userAction.getPostCondition());
            return new BinarizedGameState( stateBinarization.binarize( newCondition));
        }else
            return null;
    }

    @Override
    public String toString() {
        return mCurrentCondition + "";
    }

    /**
     * EscapeGame.EscapeScenarioCondition property
     * @return EscapeGame.EscapeScenarioCondition representing the state
     */
    public long getCondition() {
        return mCurrentCondition;
    }
}
