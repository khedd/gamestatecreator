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
    final private HashMap<PROPERTY, HashMap<String, Long>> mPropertyValues = new HashMap<>();

    /**
     * TODO naming
     */
    final private HashMap<PROPERTY, HashMap<Long, String>> mPropertyReverseValues = new HashMap<>();
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
    final private HashMap<PROPERTY, Long> mPropertyBitStart = new HashMap<>();

    /**
     * Holds the bit count for the properties eg
     * ROOM -> 1
     * SUBROOM -> 2
     * ...
     */
    final private HashMap<PROPERTY, Long> mPropertyBitCount = new HashMap<>();

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

    /**
     * initializes each entry in mPropertyValues
     */
    private void initializePropertyValues() {
        mPropertyValues.put(PROPERTY.PICKED_ITEMS, new HashMap<String, Long>());
        mPropertyValues.put(PROPERTY.ITEMS, new HashMap<String, Long>());
        mPropertyValues.put(PROPERTY.USED_ITEMS, new HashMap<String, Long>());
        mPropertyValues.put(PROPERTY.GAME_ACTION, new HashMap<String, Long>());
        mPropertyValues.put(PROPERTY.SELECTED, new HashMap<String, Long>());
        mPropertyValues.put(PROPERTY.ROOM, new HashMap<String, Long>());
        mPropertyValues.put(PROPERTY.SUBROOM, new HashMap<String, Long>());

        mPropertyReverseValues.put(PROPERTY.PICKED_ITEMS, new HashMap<Long, String>());
        mPropertyReverseValues.put(PROPERTY.ITEMS, new HashMap<Long, String>());
        mPropertyReverseValues.put(PROPERTY.USED_ITEMS, new HashMap<Long, String>());
        mPropertyReverseValues.put(PROPERTY.GAME_ACTION, new HashMap<Long, String>());
        mPropertyReverseValues.put(PROPERTY.SELECTED, new HashMap<Long, String>());
        mPropertyReverseValues.put(PROPERTY.ROOM, new HashMap<Long, String>());
        mPropertyReverseValues.put(PROPERTY.SUBROOM, new HashMap<Long, String>());
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
        long cumulativeStartIdx = 0;
        for (PROPERTY property: mPropertyOrder){
            long count = calculateBitCount( property);
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
    private long calculateBitCount(PROPERTY property) {
        long size = mPropertyValues.get( property).size();
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
     * @param count long number noting number of states
     * @return how many bits can represent that number
     */
    private long calculateCount(long count){
        return (long) Math.ceil( log2nlz( count));
    }

    /**
     * Implementation from https://stackoverflow.com/a/3305710/3985387
     * changed 31 to 32 for bit count
     * @param bits Number
     * @return log2 bits
     */
    private static long log2nlz( long bits )
    {
        if( bits == 0 )
            return 0; // or throw exception
        return 64 - Long.numberOfLeadingZeros( bits );
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
        long shiftAmount = mPropertyBitStart.get( property);
        long value = mPropertyValues.get(property).get( condition.getName());
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
        long shiftAmount = mPropertyBitStart.get( property);
        long value = mPropertyValues.get(property).get( condition.getName());
        //shift it with value as ever gameCondition wil take a bit and its index is its
        //bit that will hold
        //also with shift amount to map to correct property location
        return 1 << (shiftAmount + value);
    }


    /**
     * Binarizes the given condition that belongs to a certain property by bit method
     * @param property Property of game condition
     * @param conditions Game conditions to be binarized
     * @return Binarized game condition with respect to its property by bit method
     */
    private long binarize(PROPERTY property, ArrayList<GameCondition> conditions) {
        long binarization = 0;
        for (GameCondition gameCondition: conditions){
            binarization |= binarize(property, gameCondition);
        }
        return binarization;
    }
    /**
     * converts binary to its {@link EscapeScenarioCondition} equivalent
     * @param binary binary representation
     * @return EscapeScenarioCondition representation
     * TODO done by hand method is not used
     */
    EscapeScenarioCondition debinarize(long binary){
        GameCondition roomCond      = debinarizeSingle(PROPERTY.ROOM, binary);
        GameCondition subRoomCond   = debinarizeSingle(PROPERTY.SUBROOM, binary);
        GameCondition actionCond    = debinarizeSingle(PROPERTY.GAME_ACTION, binary);
        GameCondition selectedCond  = debinarizeSingle(PROPERTY.SELECTED, binary);

        ArrayList<GameCondition> usedCondition  = debinarizeMultiple(PROPERTY.USED_ITEMS, binary);
        ArrayList<GameCondition> pickedCond     = debinarizeMultiple(PROPERTY.PICKED_ITEMS, binary);
        ArrayList<GameCondition> itemCond       = debinarizeMultiple(PROPERTY.ITEMS, binary);

        EscapeScenarioCondition escapeScenarioCondition = new EscapeScenarioCondition(roomCond, selectedCond,
                actionCond, itemCond, pickedCond, usedCondition, subRoomCond);
        return escapeScenarioCondition;
    }

    /**
     *
     * @param property
     * @param binary
     * @return
     */
    private GameCondition debinarizeSingle(PROPERTY property, long binary) {
        //create a mask
        long bitCount = mPropertyBitCount.get( property);
        long shiftAmount = mPropertyBitStart.get( property);
        //shift 1 with bit count and subtract one to get a local mask
        //eg if bit mask is 3; 1 << 3 = 1000; 1000 - 1 = 111
        long mask = ((1 << bitCount) - 1);
        long binaryShifted = binary >> shiftAmount;
        long propertyBinary = mask & binaryShifted;
        //need to get property
        return debinarizeProperty (property, propertyBinary);
    }

    /**
     *
     * @param property
     * @param propertyBinary
     * @return
     */
    private GameCondition debinarizeProperty(PROPERTY property, long propertyBinary) {
        HashMap<Long, String> hashMap = mPropertyReverseValues.get( property);
        String name = hashMap.get( propertyBinary);
        if ( name.isEmpty()){
            return new GameCondition();
        }else{
            return new GameCondition( name, GameCondition.State.TRUE);
        }

    }

    /**
     *
     * @param property
     * @param binary
     * @return
     */
    private ArrayList<GameCondition> debinarizeMultiple(PROPERTY property, long binary) {
        ArrayList<GameCondition> gameConditions = new ArrayList<>();
        return gameConditions;
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
     * TODO naming
     */
    private void process(PROPERTY property, GameCondition gameCondition) {
        HashMap<String, Long> hashMap = mPropertyValues.get( property);
        HashMap<Long, String> reverseHashMap = mPropertyReverseValues.get( property);
        //do not insert if it already exists
        if ( !hashMap.containsKey( gameCondition.getName())) {
            long size = hashMap.size();
            hashMap.put(gameCondition.getName(), size);
            reverseHashMap.put( size, gameCondition.getName());
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
    private enum PROPERTY {
        PICKED_ITEMS, ITEMS, USED_ITEMS, SELECTED, ROOM, SUBROOM, GAME_ACTION
    }
}
