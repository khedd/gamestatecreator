import java.util.ArrayList;

/**
 * Game state class that holds the:
 *  - Current Room
 *  - Current Selection
 *  - Current Items
 *  - Picked Items
 */
class GameState {
    String mCurrentRoom;
    String mSelection;
    ArrayList<String> mItems;
    ArrayList<String> mPicked;

    static GameState fromMenu(){
        GameState gameState = new GameState( "MENU", "",
                new ArrayList<String>(), new ArrayList<String>());
        return gameState;
    }

    GameState (GameState gameState){
        mCurrentRoom = gameState.mCurrentRoom;
        mSelection   = gameState.mSelection;
        mItems = new ArrayList<>();
        mPicked = new ArrayList<>();
        mItems.addAll(gameState.mItems);
        mPicked.addAll(gameState.mPicked);
    }

    GameState(String currentRoom, String selection, ArrayList<String> items, ArrayList<String> picked) {
        mCurrentRoom = currentRoom;
        mSelection = selection;
        mItems = items;
        mPicked = picked;
    }
}
