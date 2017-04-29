import java.util.ArrayList;

/**
 * Scenario conditions that exists in Escape Games. Used to determine the
 */
class EscapeScenarioCondition extends ScenarioCondition{

    ArrayList<GameCondition> mPickedItems;
    ArrayList<GameCondition> mItems;
    GameCondition mSelected;
    GameCondition mLevel;
    GameCondition mGameAction;

    /**
     * Constructor for creating an {@link EscapeScenarioCondition} with all details
     * @param level level/room requirement for this condition or post level after taking the action
     * @param selected is an item selected to take the action or post selection
     * @param gameAction what has to be the current action of the user, or what is post action of the user
     * @param pickedItems what are the picked items of the user or what will be the picked items of the user
     * @param items what are the items of the user or what will be the items of the user
     */
    EscapeScenarioCondition ( GameCondition level, GameCondition selected, GameCondition gameAction,
                             ArrayList<GameCondition> pickedItems, ArrayList<GameCondition> items){

        mLevel          = level;
        mSelected       = selected;
        mGameAction     = gameAction;
        mPickedItems    = pickedItems;
        mItems          = items;
    }

    /**
     * Copy constructor using deep copy
     * @param escapeScenarioCondition Another instance
     */
    EscapeScenarioCondition ( EscapeScenarioCondition escapeScenarioCondition){
        mGameAction = escapeScenarioCondition.mGameAction;
        for ( GameCondition gameCondition: escapeScenarioCondition.mPickedItems){
            mPickedItems.add( new GameCondition( gameCondition));
        }
        for ( GameCondition gameCondition: escapeScenarioCondition.mItems){
            mItems.add( new GameCondition( gameCondition));
        }
        mSelected = new GameCondition( escapeScenarioCondition.mSelected);
        mLevel = new GameCondition( escapeScenarioCondition.mLevel);
    }

    /**
     * Applies this condition to the gameState thus changing the game state creates a new state
     * @param gameState Current {@link GameState}
     * @return new {@link GameState} that is created after applying this game state
     */
    GameState apply ( GameState gameState){

        return  null;
    }

    /**
     * Compares this condition to other condition
     * @param mPreCondition Pre condition to be compared
     * @return if mPreCondition is subset of current condition returns true else false
     */
    boolean compare(EscapeScenarioCondition mPreCondition) {
        boolean status;
        status =  mGameAction.compare(mPreCondition.mGameAction);
        status &= mLevel.compare( mPreCondition.mLevel);
        status &= mSelected.compare( mPreCondition.mSelected);
        return status;
    }

    /**
     * Applies another condition thus creating a new condition
     * @param mPostCondition Post condition that will shape this condition
     * @return A new condition which is superset of two conditions
     */
    EscapeScenarioCondition apply(EscapeScenarioCondition mPostCondition) {
        GameCondition selected = mSelected.apply ( mPostCondition.mSelected);
        GameCondition level = mLevel.apply ( mPostCondition.mLevel);
        GameCondition gameAction = mGameAction.apply ( mPostCondition.mGameAction);

        //todo apply method for these
        ArrayList<GameCondition> mPickedItems = new ArrayList<>();
        ArrayList<GameCondition> mItems = new ArrayList<>();
        return new EscapeScenarioCondition(level, selected, gameAction, mPickedItems, mItems);
    }
}
