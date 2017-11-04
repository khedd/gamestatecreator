import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used to binarize state\ this class collects information from all user actions to better subdivide
 * state to binary
 */
public class StateBinarization {
    private final ArrayList<UserAction> mUserActions;

    /**
     * Holds property value with its list of usable `thing of property` in the game
     * Inner HashMap holds the index for the name of {@link GameCondition}
     */
    final private HashMap<PROPERTY, HashMap<String, Integer>> mPropertyValues = new HashMap<>();
    //TODO why hold a separate
//    /**
//     * list of all rooms available actions in the game
//     */
//    final private HashMap<String, Integer> mActions = ;
//    /**
//     * list of all rooms available in the game
//     */
//    final private HashMap<String, Integer> mRooms;
//    /**
//     * list of all sub rooms in the game
//     */
//    final private HashMap<String, Integer> mSubRooms;
//    /**
//     * list of all items in the game
//     */
//    final private HashMap<String, Integer> mItems;
//    /**
//     * list of all selectable items in the game
//     */
//    final private HashMap<String, Integer> mSelectedItems;
//    /**
//     * list of all usable items in the game
//     */
//    final private HashMap<String, Integer> mUsedItems;
//    /**
//     * list of all pickable items in the game
//     */
//    final private HashMap<String, Integer> mPickedItems;

    /**
     * Holds the order for properties in which they are added to bitmap
     */
    final private ArrayList<PROPERTY> mPropertyOrder = new ArrayList<>();
    /**
     * Holds the start index for the properties eg
     * ROOM -> 0
     * SUBROOM -> 2
     * ...
     */
    final private HashMap<PROPERTY, Integer> mPropertyBitStart = new HashMap<>();

    /**
     * Holds the bit count for the properties eg
     * ROOM -> 1
     * SUBROOM -> 2
     * ...
     */
    final private HashMap<PROPERTY, Integer> mPropertyBitCount = new HashMap<>();

    /**
     * holds which {@link PROPERTY} are mapped with which {@link METHOD}
     */
    final private HashMap<PROPERTY, METHOD>  mMethod = new HashMap<>();


    /**
     * Creates a way for binarization of {@link GameState} from intelligently
     * calculating a simple conversion
     * @param userActions Available actions in the game
     */
    StateBinarization ( ArrayList<UserAction> userActions){
        initializePropertyMethod();
        initializePropertyValues ();
        initializePropertyOrder ();
        mUserActions = userActions;
        process();
        calculate ();
    }

    /**
     * Initializes property order this will effect the bit order in binarize and debinarize
     * methods
     */
    private void initializePropertyOrder() {
        mPropertyOrder.add( PROPERTY.ITEMS);
        mPropertyOrder.add( PROPERTY.PICKED_ITEMS);
        mPropertyOrder.add( PROPERTY.SELECTED);
        mPropertyOrder.add( PROPERTY.USED_ITEMS);
        mPropertyOrder.add( PROPERTY.GAME_ACTION);
        mPropertyOrder.add( PROPERTY.SUBROOM);
        mPropertyOrder.add( PROPERTY.ROOM);
    }

    private void initializePropertyValues() {
        mPropertyValues.put(PROPERTY.PICKED_ITEMS, new HashMap<String, Integer>());
        mPropertyValues.put(PROPERTY.ITEMS, new HashMap<String, Integer>());
        mPropertyValues.put(PROPERTY.USED_ITEMS, new HashMap<String, Integer>());
        mPropertyValues.put(PROPERTY.GAME_ACTION, new HashMap<String, Integer>());
        mPropertyValues.put(PROPERTY.SELECTED, new HashMap<String, Integer>());
        mPropertyValues.put(PROPERTY.ROOM, new HashMap<String, Integer>());
        mPropertyValues.put(PROPERTY.SUBROOM, new HashMap<String, Integer>());
    }

    /**
     * fills the method for binarization
     */
    private void initializePropertyMethod() {
        //all item related will be separate
        mMethod.put(PROPERTY.PICKED_ITEMS, METHOD.BIT);
        mMethod.put(PROPERTY.ITEMS, METHOD.BIT);
        mMethod.put(PROPERTY.USED_ITEMS, METHOD.BIT);
        mMethod.put(PROPERTY.GAME_ACTION, METHOD.COUNT);
        mMethod.put(PROPERTY.SELECTED, METHOD.COUNT);
        mMethod.put(PROPERTY.ROOM, METHOD.COUNT);
        mMethod.put(PROPERTY.SUBROOM, METHOD.COUNT);
    }

    /**
     * Calculates how many bits are required for a property
     */
    private void calculate() {
        int cumulativeStartIdx = 0;
        for (PROPERTY property: mPropertyOrder){
            int count = calculateBitCount( property);
            mPropertyBitStart.put(property, cumulativeStartIdx);
            mPropertyBitCount.put(property, count);
            cumulativeStartIdx += count;
        }
    }

    /**
     * calculates how many bits that property will take up
     * @param property Property of game condition
     * @return number of bits to represent the property
     */
    private int calculateBitCount(PROPERTY property) {
        int size = mPropertyValues.get( property).size();
        METHOD method = mMethod.get( property);
        switch ( method) {
            case BIT:
                return size;
            case COUNT:
                return calculateCount(size);
        }
        //this is an error
        throw new RuntimeException("Unhandled property method combination in: " + property.name());
    }

    /**
     * calculates bit count of a number
     * @param count integer number noting number of states
     * @return how many bits can represent that number
     */
    private int calculateCount(int count){
        return (int) Math.ceil( log2nlz( count));
    }

    /**
     * Implementation from https://stackoverflow.com/a/3305710/3985387
     * changed 31 to 32 for bit count
     * @param bits Number
     * @return log2 bits
     */
    private static int log2nlz( int bits )
    {
        if( bits == 0 )
            return 0; // or throw exception
        return 32 - Integer.numberOfLeadingZeros( bits );
    }

    /**
     * converts {@link EscapeScenarioCondition} to its binary representation
     * @param escapeScenarioCondition {@link EscapeScenarioCondition} of {@link GameState}
     * @return Binary representation
     */
    long binarize ( EscapeScenarioCondition escapeScenarioCondition){

        long roomBinary     = binarize( PROPERTY.ROOM, escapeScenarioCondition.getLevel());
        long subRoomBinary  = binarize( PROPERTY.SUBROOM, escapeScenarioCondition.getSubRoom());
        long actionBinary   = binarize( PROPERTY.GAME_ACTION, escapeScenarioCondition.getGameAction());
        long usedBinary     = binarize( PROPERTY.USED_ITEMS, escapeScenarioCondition.getUsedItems());
        long selectedBinary = binarize( PROPERTY.SELECTED, escapeScenarioCondition.getSelected());
        long pickedBinary   = binarize( PROPERTY.PICKED_ITEMS, escapeScenarioCondition.getPickedItems());
        long itemBinary     = binarize( PROPERTY.ITEMS, escapeScenarioCondition.getItems());

        return roomBinary | subRoomBinary | actionBinary | usedBinary | selectedBinary | pickedBinary | itemBinary;
    }

    /**
     * Binarizes the given condition that belongs to a certain property
     * @param property Property of game condition
     * @param condition Game condition to be binarized
     * @return Binarized game condition with respect to its property
     */
    private long binarize(PROPERTY property, GameCondition condition) {
        METHOD method = mMethod.get( property);
        switch ( method){
            case BIT:
                return binarizeBit ( property, condition);
            case COUNT:
                return binarizeCount ( property, condition);
        }
        return 0;
    }

    /**
     * Binarizes the given condition that belongs to a certain property by counting method
     * @param property Property of game condition
     * @param condition Game condition to be binarized
     * @return Binarized game condition with respect to its property by counting method
     */
    private long binarizeCount(PROPERTY property, GameCondition condition) {
        int shiftAmount = mPropertyBitStart.get( property);
        int value = mPropertyValues.get(property).get( condition.getName());
        //shift amount to map to correct property location
        return value << shiftAmount;
    }

    /**
     * Binarizes the given condition that belongs to a certain property by bit method
     * @param property Property of game condition
     * @param condition Game condition to be binarized
     * @return Binarized game condition with respect to its property by bit method
     */
    private long binarizeBit(PROPERTY property, GameCondition condition) {
        int shiftAmount = mPropertyBitStart.get( property);
        int value = mPropertyValues.get(property).get( condition.getName());
        //shift it with value as ever gameCondition wil take a bit and its index is its
        //bit that will hold
        //also with shift amount to map to correct property location
        return 1 << (shiftAmount + value);
    }


    private long binarize(PROPERTY property, ArrayList<GameCondition> conditions) {
        int binarization = 0;
        for (GameCondition gameCondition: conditions){
            binarization |= binarize(property, gameCondition);
        }
        return binarization;
    }
    /**
     * converts binary to its {@link EscapeScenarioCondition} equivalent
     * @param binary binary representation
     * @return EscapeScenarioCondition representation
     */
    EscapeScenarioCondition debinarize(long binary){
        //TODO
        GameCondition roomCond      = debinarizeSingle(PROPERTY.ROOM, binary);
        GameCondition subRoomCond   = debinarizeSingle(PROPERTY.ROOM, binary);
        GameCondition actionCond    = debinarizeSingle(PROPERTY.ROOM, binary);
        GameCondition selectedCond  = debinarizeSingle(PROPERTY.ROOM, binary);

        ArrayList<GameCondition> usedCondition  = debinarizeMultiple(PROPERTY.USED_ITEMS, binary);
        ArrayList<GameCondition> pickedCond     = debinarizeMultiple(PROPERTY.PICKED_ITEMS, binary);
        ArrayList<GameCondition> itemCond       = debinarizeMultiple(PROPERTY.ITEMS, binary);

        EscapeScenarioCondition escapeScenarioCondition = new EscapeScenarioCondition(roomCond, selectedCond,
                actionCond, itemCond, pickedCond, usedCondition, subRoomCond);
        return escapeScenarioCondition;
    }

    private GameCondition debinarizeSingle(PROPERTY property, long binary) {
        return null;
    }

    private ArrayList<GameCondition> debinarizeMultiple(PROPERTY property, long binary) {
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
        process( PROPERTY.ROOM, condition.getLevel());
        process ( PROPERTY.SUBROOM, condition.getSubRoom());
        process ( PROPERTY.GAME_ACTION, condition.getGameAction());
        process ( PROPERTY.SELECTED, condition.getSelected());

        for (GameCondition gameCondition: condition.getPickedItems()){
            process ( PROPERTY.PICKED_ITEMS, gameCondition);
        }
        for (GameCondition gameCondition: condition.getItems()){
            process ( PROPERTY.ITEMS, gameCondition);
        }
        for (GameCondition gameCondition: condition.getUsedItems()){
            process ( PROPERTY.USED_ITEMS, gameCondition);
        }
    }

    /**
     * process property to get its HashMap from mPropertyValues and add to it
     * @param property Property of game condition
     * @param gameCondition {@link GameCondition}
     */
    private void process(PROPERTY property, GameCondition gameCondition) {
        HashMap<String, Integer> hashMap = mPropertyValues.get( property);
        //do not insert if it already exists
        if ( !hashMap.containsKey( gameCondition.getName()))
            hashMap.put( gameCondition.getName(), hashMap.size());
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
    private enum PROPERTY {
        PICKED_ITEMS, ITEMS, USED_ITEMS, SELECTED, ROOM, SUBROOM, GAME_ACTION
    }
}
