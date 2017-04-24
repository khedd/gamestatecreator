import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Scenario conditions that exists in Escape Games
 */
class EscapeScenarioCondition extends ScenarioCondition{
    EscapeGameAction.Option mGameAction;
    ArrayList<String> mPickedItems;
    HashMap<String, Option> mItems;
    String mLevel;
    String mName;


    EscapeScenarioCondition (String name, String level, EscapeGameAction.Option gameAction,
                             ArrayList<String> pickedItems, HashMap<String, EscapeScenarioCondition.Option> items){
        mName = name;
        mLevel = level;
        mGameAction = gameAction;
        mPickedItems = pickedItems;
        mItems = items;
    }
    enum Option{
        TRUE, CHANGE, FALSE
    }
}
