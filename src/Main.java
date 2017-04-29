import java.util.*;

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
    static ArrayList<UserAction> createActions (){
        UserAction userAction = new UserAction("START", "MENU", "ROOM");
        ArrayList<UserAction> userActions= new ArrayList<>();
        userActions.add(userAction);
        return userActions;
    }
    static EscapeScenarioCondition createConditions(){
        String name = "START GAME";
        String level = "MENU";
        EscapeGameAction.Option escapeGameAction = EscapeGameAction.Option.START;
        ArrayList<String> pickedItems = new ArrayList<>();
        HashMap<String, EscapeScenarioCondition.Option> items = new HashMap<>();
        EscapeScenarioCondition escapeScenarioCondition = new EscapeScenarioCondition(name, level, escapeGameAction,
                pickedItems, items);

        return escapeScenarioCondition;
    }

    static void createTestAdvanced (){

        GameState menuState = GameState.fromMenu();
        GameGraph gameGraph = new GameGraph(menuState);
    }
}