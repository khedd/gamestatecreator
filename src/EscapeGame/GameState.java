package EscapeGame;

import ExampleEscapeGame.Available;
import ExampleEscapeGame.Available.Levels;

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
public class GameState {
    private final EscapeScenarioCondition mCurrentCondition;

    /**
     * Creates a new EscapeGame.GameState using the {@link EscapeScenarioCondition}
     * @param escapeScenarioCondition Current condition of the game
     */
    private GameState(EscapeScenarioCondition escapeScenarioCondition) {
        mCurrentCondition = escapeScenarioCondition;
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
    public static GameState fromMenu(){
        GameCondition levelMenu = new GameCondition(Available.Levels.Main.MENU.name(), GameCondition.State.TRUE);
        GameCondition selectedMenu = new GameCondition("", GameCondition.State.FALSE);
        GameCondition gameAction = new GameCondition(Available.Actions.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> pickedItems = new ArrayList<>();
        ArrayList<GameCondition> items = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioCondition = new EscapeScenarioCondition(
                levelMenu, selectedMenu, gameAction, items, pickedItems);
        return new GameState( escapeScenarioCondition);
    }
    /**
     * Copy constructor, uses {@link EscapeScenarioCondition} to copy
     * @param gameState Another game state
     */
    @SuppressWarnings("unused")
    public GameState (GameState gameState){
        mCurrentCondition = new EscapeScenarioCondition(gameState.mCurrentCondition);
    }

    /**
     * Creates a new EscapeGame.GameState after applying user action
     * @param userAction {@link UserAction} checks if {@link UserAction#mPreCondition} holds
     *                                     and applies {@link UserAction#mPostCondition}
     * @return null if preCondition is not applicable else a new game state of applies PostCondition
     */
    public GameState apply ( UserAction userAction){
        if (mCurrentCondition.compare ( userAction.getPreCondition())){
            EscapeScenarioCondition newCondition = mCurrentCondition.apply( userAction.getPostCondition());
            return new GameState( newCondition);
        }else
            return null;
    }

    @Override
    public String toString() {
        return mCurrentCondition.toString();
    }

    /**
     * EscapeGame.EscapeScenarioCondition property
     * @return EscapeGame.EscapeScenarioCondition representing the state
     */
    public EscapeScenarioCondition getCondition() {
        return mCurrentCondition;
    }
}
