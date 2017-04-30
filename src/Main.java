import java.util.ArrayList;

/**
 * Main entry pint of project GameStateCreator
 * Project is used to create a game scenario graph from nodes that have pre conditions and post changes.
 * From entry point algorithm analyzes which paths the game can take and constricts a graph using these
 * pre and post.
 *
 */
public class Main {
    /**
     * Main method
     * @param args Not used
     */
    public static void main(String[] args){
        System.out.println("Game State Creator");
        createTestAdvanced ();

    }
    private static void createTestAdvanced(){

        GameState menuState = GameState.fromMenu();
        GameGraph gameGraph = new GameGraph(menuState);
        gameGraph.addUserAction( createStartAction());
        gameGraph.addUserAction( createPickMakeUpAction());
        gameGraph.addUserAction( createPickDoorHandleAction());

        gameGraph.generate();
    }
    
    
    private static UserAction createStartAction(){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition("MENU", GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition();
        GameCondition preGameActCond =  new GameCondition();
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioConditionPostStart = null;
        GameCondition postLevelCond = new GameCondition("ROOM", GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        
        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        UserAction startAction = new UserAction("START GAME", escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);

        return startAction;
    }

    private static UserAction createPickMakeUpAction(){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition("ROOM", GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition();
        GameCondition preGameActCond =  new GameCondition();
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();
        prePickedItems.add( new GameCondition("MAKEUPIT", GameCondition.State.FALSE));

        EscapeScenarioCondition escapeScenarioConditionPostStart = null;
        GameCondition postLevelCond = new GameCondition("ROOM", GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        postItems.add( new GameCondition("MAKEUPIT", GameCondition.State.TRUE));
        postPickedItems.add( new GameCondition("MAKEUPIT", GameCondition.State.TRUE));

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        UserAction startAction = new UserAction("PICK MAKEUP", escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);

        return startAction;
    }

    private static UserAction createPickDoorHandleAction(){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition("ROOM", GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition();
        GameCondition preGameActCond =  new GameCondition();
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();
        prePickedItems.add( new GameCondition("DOORHANDLEIT", GameCondition.State.FALSE));

        EscapeScenarioCondition escapeScenarioConditionPostStart = null;
        GameCondition postLevelCond = new GameCondition("ROOM", GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        postItems.add( new GameCondition("DOORHANDLEIT", GameCondition.State.TRUE));
        postPickedItems.add( new GameCondition("DOORHANDLEIT", GameCondition.State.TRUE));

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        UserAction startAction = new UserAction("PICK DOOR HANDLE", escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);

        return startAction;
    }
}