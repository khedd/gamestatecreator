import java.util.ArrayList;
import java.util.Objects;

/**
 * Scenario conditions that exists in Escape Games. Used to determine the
 *
 * Design Decisions Made:
 *  - during {@link #combineLists(ArrayList, ArrayList)} if combined list elements state is false
 *  it is removed from list however removing is inefficiently implemented
 *  - in {@link #apply(EscapeScenarioCondition)} if current selection is false it os converted to
 *  {@link GameCondition#GameCondition()} default constructor
 */
class EscapeScenarioCondition extends ScenarioCondition{

    private ArrayList<GameCondition> mPickedItems;
    private ArrayList<GameCondition> mItems;
    private ArrayList<GameCondition> mUsedItems;
    private final GameCondition mSelected;
    private final GameCondition mLevel;
    private final GameCondition mGameAction;
    private final GameCondition mSubRoom;

    /**
     * Constructor for creating an {@link EscapeScenarioCondition} with all details, initializes subRoom
     * as empty and mUsedItems as empty set
     * @param level level/room requirement for this condition or post level after taking the action
     * @param selected is an item selected to take the action or post selection
     * @param gameAction what has to be the current action of the user, or what is post action of the user
     * @param pickedItems what are the picked items of the user or what will be the picked items of the user
     * @param items what are the items of the user or what will be the items of the user
     */
    EscapeScenarioCondition ( GameCondition level, GameCondition selected, GameCondition gameAction,
                              ArrayList<GameCondition> items, ArrayList<GameCondition> pickedItems){

        mLevel          = level;
        mSelected       = selected;
        mUsedItems      = new ArrayList<>();
        mGameAction     = gameAction;
        mPickedItems    = pickedItems;
        mItems          = items;
        mSubRoom        = new GameCondition();
    }


    /**
     * Constructor for creating an {@link EscapeScenarioCondition} with all details, initializes subRoom
     * @param usedItems used items in the condition
     * @param subRoom current subroom in addition to the current room
     * @param level level/room requirement for this condition or post level after taking the action
     * @param selected is an item selected to take the action or post selection
     * @param gameAction what has to be the current action of the user, or what is post action of the user
     * @param pickedItems what are the picked items of the user or what will be the picked items of the user
     * @param items what are the items of the user or what will be the items of the user
     */
    EscapeScenarioCondition ( GameCondition level, GameCondition selected, GameCondition gameAction,
                              ArrayList<GameCondition> items, ArrayList<GameCondition> pickedItems,
                              ArrayList<GameCondition> usedItems, GameCondition subRoom){

        mLevel          = level;
        mSelected       = selected;
        mUsedItems      = usedItems;
        mGameAction     = gameAction;
        mPickedItems    = pickedItems;
        mItems          = items;
        mSubRoom        = subRoom;
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
        for ( GameCondition gameCondition: escapeScenarioCondition.mUsedItems){
            mUsedItems.add( new GameCondition( gameCondition));
        }
        mSelected = new GameCondition( escapeScenarioCondition.mSelected);
        mLevel = new GameCondition( escapeScenarioCondition.mLevel);
        mSubRoom = new GameCondition( escapeScenarioCondition.mSubRoom);

    }

    /**
     * TODO check loops, might exit if condition is false
     * Compares this condition to other condition
     * @param mPreCondition Pre condition to be compared
     * @return if mPreCondition is subset of current condition returns true else false
     */
    boolean compare(EscapeScenarioCondition mPreCondition) {
        boolean status;
        status =  mGameAction.compare(mPreCondition.mGameAction);
        status &= mLevel.compare( mPreCondition.mLevel);
        status &= mSelected.compare( mPreCondition.mSelected);
        status &= mSubRoom.compare( mPreCondition.mSubRoom);

        status &= compareLists(mItems, mPreCondition.mItems);
        status &= compareLists(mPickedItems, mPreCondition.mPickedItems);
        status &= compareLists(mUsedItems, mPreCondition.mUsedItems);
        return status;
    }

    /**
     * Compares two lists of {@link GameCondition}
     * @param first Our current list
     * @param second List to be compared to the first list
     * @return status of comparison TODO not sure of method
     */
    private boolean compareLists (ArrayList<GameCondition> first, ArrayList<GameCondition> second){
        boolean status = true;
        for (GameCondition firstGameCondition: first) {
            for (GameCondition secondGameCondition: second) {
                if (Objects.equals(firstGameCondition.getName(), secondGameCondition.getName())){
                    status &= firstGameCondition.compare( secondGameCondition);
                }
            }
        }

        //check the inner loop for TRUE
        for (GameCondition secondGameCondition: second) {
            //if the condition is true it must exist in the other
            if (secondGameCondition.getState() == GameCondition.State.TRUE){
                boolean found = false;
                for (GameCondition firstGameCondition: first) {
                    if (Objects.equals(firstGameCondition.getName(), secondGameCondition.getName())){
                        found = true;
                        break;
                    }
                }
                if ( !found)
                    status &= false;
            }
        }
        return status;
    }
    /**
     * Applies another condition thus creating a new condition
     * @param mPostCondition Post condition that will shape this condition
     * @return A new condition which is superset of two conditions
     */
    EscapeScenarioCondition apply(EscapeScenarioCondition mPostCondition) {
        GameCondition selected = mSelected.apply ( mPostCondition.mSelected);
        //check selected for false
        if ( selected.getState() == GameCondition.State.FALSE)
            selected = new GameCondition();

        GameCondition level = mLevel.apply ( mPostCondition.mLevel);
        GameCondition gameAction = mGameAction.apply ( mPostCondition.mGameAction);
        GameCondition subRoom = mSubRoom.apply( mPostCondition.mSubRoom);
        ArrayList<GameCondition> gamePickedItems = combineLists(mPickedItems, mPostCondition.mPickedItems);
        ArrayList<GameCondition> gameItems = combineLists(mItems, mPostCondition.mItems);
        ArrayList<GameCondition> usedItems = combineLists(mUsedItems, mPostCondition.mUsedItems);
        return new EscapeScenarioCondition(level, selected, gameAction, gameItems, gamePickedItems, usedItems, subRoom);
    }

    /**
     * Combines given two lists of {@link GameCondition}. If only one of the lists contains an element that element
     * is added else other game condition is added after the result of {@link GameCondition#apply(GameCondition)}
     * first list is copied to another list and apply method is called on copies
     * TODO use requires element to disappear so a condition like FALSE will be applied to TRUE however apply does not handle
     * @param first Our current list
     * @param second List to be applied to the first list
     * @return Subset of these two sets
     */
    private ArrayList<GameCondition> combineLists(ArrayList<GameCondition> first, ArrayList<GameCondition> second){
        ArrayList<GameCondition> combined = new ArrayList<>();
        //first add from first
        for ( GameCondition gameCondition: first){
            combined.add( new GameCondition( gameCondition));
        }
        for (GameCondition gameConditionSecond: second) {
            boolean found = false;
            for (GameCondition gameConditionFirst: new ArrayList<>(combined)) {
                if (Objects.equals(gameConditionFirst.getName(), gameConditionSecond.getName())){
                    gameConditionFirst.applyUpdate( gameConditionSecond);
                    //if the item is false remove it
                    if ( gameConditionFirst.getState() == GameCondition.State.FALSE)
                        combined.remove(gameConditionFirst);
                    found = true;
                }
            }
            if ( !found){
                combined.add( gameConditionSecond);
            }
        }
        return combined;
    }

    /**
     * Converts to string representation
     * @return String representation
     */
    @Override
    public String toString() {
        String returnString = "";
        returnString += "Current room: " + mLevel.toString() + "\n";
        returnString += "Sub room: " + mSubRoom.toString() + "\n";
        returnString += "Current action: " + mGameAction.toString() + "\n";
        returnString += "Current selection: " + mSelected.toString() + "\n";
        returnString += "Current items: " + mItems.toString() + "\n";
        returnString += "Current used items: " + mUsedItems.toString() + "\n";
        returnString += "Current picked items: " + mPickedItems.toString() + "\n";

        return returnString;
    }
}
