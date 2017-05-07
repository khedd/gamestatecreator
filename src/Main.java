import java.util.ArrayList;

/**
 * Main entry pint of project GameStateCreator
 * Project is used to create a game scenario graph from nodes that have pre conditions and post changes.
 * From entry point algorithm analyzes which paths the game can take and constricts a graph using these
 * pre and post.
 *
 */
class Main {
    /**
     * Main method calls {@link #createTests()}
     * @param args Not used
     */
    public static void main(String[] args){
        System.out.println("Game State Creator");
        createTests();

    }

    /**
     * Creates a test case from
     * {@link #createMenuAction()} not active as it causes loops
     * {@link #createStartAction()} starts the game from empty state
     * {@link #createPickDoorHandleAction()} picks door handle
     * {@link #createPickMakeUpAction()} picks make up
     */
    private static void createTests(){

        GameState menuState = GameState.fromMenu();
        GameGraphGenerator gameGraphGenerator = new GameGraphGenerator(menuState);
        gameGraphGenerator.addUserAction( createStartAction());
        gameGraphGenerator.addUserAction( createPickMakeUpAction());
        gameGraphGenerator.addUserAction( createPickDoorHandleAction());
        gameGraphGenerator.addUserAction( createMenuAction());

        gameGraphGenerator.generate();
        gameGraphGenerator.print();
    }

    /**
     * Creates the UserAction that return to Menu from any level that is not MENU
     * @return Return to menu action
     */
    @SuppressWarnings("unused")
    private static UserAction createMenuAction(){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition("MENU", GameCondition.State.FALSE);
        GameCondition preSelectedCond = new GameCondition();
        GameCondition preGameActCond =  new GameCondition();
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition("MENU", GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("RETURN MENU", escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);
    }

    /**
     * Creates a start action that requires room to be menu and changes the level to Room
     * @return Start the game action
     */
    private static UserAction createStartAction(){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition("MENU", GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition();
        GameCondition preGameActCond =  new GameCondition();
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition("ROOM", GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        
        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("START GAME", escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);
    }

    /**
     * Creates an action that enables user to pick the makeup item in the room
     * @return Pick MakeUp action
     */
    private static UserAction createPickMakeUpAction(){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition("ROOM", GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition();
        GameCondition preGameActCond =  new GameCondition();
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();
        prePickedItems.add( new GameCondition("MAKE UP IT", GameCondition.State.FALSE));

        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition("ROOM", GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        postItems.add( new GameCondition("MAKE UP IT", GameCondition.State.TRUE));
        postPickedItems.add( new GameCondition("MAKE UP IT", GameCondition.State.TRUE));

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("PICK MAKE UP", escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);
    }

    /**
     * Creates an action that enables user to pick the door handle item in the room
     * @return Pick Door Handle action
     */
    private static UserAction createPickDoorHandleAction(){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition("ROOM", GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition();
        GameCondition preGameActCond =  new GameCondition();
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();
        prePickedItems.add( new GameCondition("DOOR HANDLE", GameCondition.State.FALSE));

        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition("ROOM", GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        postItems.add( new GameCondition("DOOR HANDLE", GameCondition.State.TRUE));
        postPickedItems.add( new GameCondition("DOOR HANDLE", GameCondition.State.TRUE));

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("PICK DOOR HANDLE", escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);
    }


}