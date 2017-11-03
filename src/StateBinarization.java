import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Used to binarize state\ this class collects information from all user actions to better subdivide
 * state to binary
 */
public class StateBinarization {
    private final ArrayList<UserAction> mUserActions;

    /**
     * list of all rooms available actions in the game
     */
    final private Set<String> mActions = new HashSet<>();
    /**
     * list of all rooms available in the game
     */
    final private Set<String> mRooms = new HashSet<>();
    /**
     * list of all sub rooms in the game
     */
    final private Set<String> mSubRooms = new HashSet<>();
    /**
     * list of all items in the game
     */
    final private Set<String> mItems = new HashSet<>();
    /**
     * list of all selectable items in the game
     */
    final private Set<String> mSelectedItems = new HashSet<>();
    /**
     * list of all usable items in the game
     */
    final private Set<String> mUsedItems = new HashSet<>();
    /**
     * list of all pickable items in the game
     */
    final private Set<String> mPickedItems = new HashSet<>();

    /**
     * Holds the start index for the properties eg
     * ROOM -> 0
     * SUBROOM -> 2
     * ...
     */
    final private HashMap<PROPERTIES, Integer> mPropertyBitStart = new HashMap<>();

    /**
     * Holds the bit count for the properties eg
     * ROOM -> 1
     * SUBROOM -> 2
     * ...
     */
    final private HashMap<PROPERTIES, Integer> mPropertyBitCount = new HashMap<>();

    /**
     * holds which {@link PROPERTIES} are mapped with which {@link METHOD}
     */
    final private HashMap<PROPERTIES, METHOD>  mMethod = new HashMap<>();


    /**
     * Creates a way for binarization of {@link GameState} from intelligently
     * calculating a simple conversion
     * @param userActions Available actions in the game
     */
    StateBinarization ( ArrayList<UserAction> userActions){
        initializePropertyMethod();
        mUserActions = userActions;
        process();
        calculate ();
    }

    /**
     * fills the method for binarization
     */
    private void initializePropertyMethod() {
        //all item related will be separate
        mMethod.put(PROPERTIES.PICKED_ITEMS, METHOD.BIT);
        mMethod.put(PROPERTIES.ITEMS, METHOD.BIT);
        mMethod.put(PROPERTIES.USED_ITEMS, METHOD.BIT);
        mMethod.put(PROPERTIES.GAME_ACTION, METHOD.COUNT);
        mMethod.put(PROPERTIES.SELECTED, METHOD.COUNT);
        mMethod.put(PROPERTIES.ROOM, METHOD.COUNT);
        mMethod.put(PROPERTIES.SUBROOM, METHOD.COUNT);
    }

    /**
     * Calculates how many bits are required for a property
     */
    private void calculate() {
        //also represents the way bits are mapped from high to low
        int roomCount          = mRooms.size();
        int subRoomCount       = mSubRooms.size();
        int action             = mActions.size();
        int usedItemCount      = mUsedItems.size();
        int selectedItemCount  = mSelectedItems.size();
        int pickedItemCount    = mPickedItems.size();
        int itemCount          = mItems.size();


    }

    /**
     * converts {@link EscapeScenarioCondition} to its binary representation
     * @param escapeScenarioCondition {@link EscapeScenarioCondition} of {@link GameState}
     * @return Binary representation
     */
    long binarize ( EscapeScenarioCondition escapeScenarioCondition){
        //TODO
        return 0;
    }

    /**
     * converts binary to its {@link EscapeScenarioCondition} equivalent
     * @param binary binary representation
     * @return EscapeScenarioCondition representation
     */
    EscapeScenarioCondition unbinarize ( long binary){
        //TODO
        return null;
    }

    /**
     * processes user actions to find unique properties
     */
    private void process (){
        for (UserAction userAction: mUserActions) {
            process( userAction);
        }
    }

    /**
     * Processes {@link UserAction} to find unique properties
     * @param userAction {@link UserAction}
     */
    private void process(UserAction userAction) {
        process( userAction.mPreCondition);
        process( userAction.mPostCondition);
    }

    /**
     * processes {@link EscapeScenarioCondition} to extract its properties such as
     * its items, rooms this way we are filling number of unique properties in the game
     * @param condition {@link EscapeScenarioCondition}
     */
    private void process(EscapeScenarioCondition condition) {
        mRooms.add( condition.getLevel().getName());
        mSubRooms.add( condition.getSubRoom().getName());
        mActions.add( condition.getGameAction().getName());
        mSelectedItems.add( condition.getSelected().getName());

        for (GameCondition gameCondition: condition.getPickedItems()){
            mPickedItems.add( gameCondition.getName());
        }
        for (GameCondition gameCondition: condition.getItems()){
            mItems.add( gameCondition.getName());
        }
        for (GameCondition gameCondition: condition.getUsedItems()){
            mUsedItems.add( gameCondition.getName());
        }
    }

    /**
     * enumeration for binarization if it is count then it will produce bits in count
     * else it will set every bit as separate
     */
    private enum METHOD{
        COUNT, BIT
    }

    /**
     * available properties for this game
     */
    private enum PROPERTIES{
        PICKED_ITEMS, ITEMS, USED_ITEMS, SELECTED, ROOM, SUBROOM, GAME_ACTION
    }
}
