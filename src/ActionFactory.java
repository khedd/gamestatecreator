import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;

/**
 * Used to create actions from {@link EscapeGameAction} such as:
 *  - Pick an item
 *  - Use an item
 *  - Select an item
 *  - Zoom on an item
 *  - Select{Use, Dismantle, Combine}
 */
public class ActionFactory {
    /**
     * TODO should reset subRoom
     * Creates the UserAction that return to Menu from any level that is not MENU
     * @return Return to menu action
     */
    static UserAction createMenuAction(){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition(GameRooms.MENU.name(), GameCondition.State.FALSE);
        GameCondition preSelectedCond = new GameCondition();
        GameCondition preGameActCond =  new GameCondition();
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition(GameRooms.MENU.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        GameCondition postSubRoom =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postUsedItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems, postUsedItems, postSubRoom);

        return new UserAction("RETURN MENU", escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);
    }

    /**
     * Creates a start action that requires room to be menu and changes the level to Room
     * @return Start the game action
     */
    static UserAction createStartAction(@NotNull Enum<?> room, @NotNull GameRooms subRoom){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition(GameRooms.MENU.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition();
        GameCondition preGameActCond =  new GameCondition();
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        ArrayList<GameCondition> postUsedItems = new ArrayList<>();
        GameCondition postSubRoom = new GameCondition(subRoom.name(), GameCondition.State.TRUE);

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems, postUsedItems, postSubRoom);

        return new UserAction("START GAME", escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);
    }

    /**
     * TODO add subRoom check
     * @param item
     * @param room
     * @param subRoom
     * @return
     */
    static UserAction createPickAction (@NotNull Enum<?> item, @NotNull GameRooms room, @NotNull  GameRooms subRoom){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition();
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();
        ArrayList<GameCondition> preUsedItems = new ArrayList<>();
        prePickedItems.add( new GameCondition( item.name(), GameCondition.State.FALSE));
        GameCondition preSubRoom = new GameCondition(subRoom.name(), GameCondition.State.TRUE);

        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        postItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));
        postPickedItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems, preUsedItems, preSubRoom);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("PICK " +  item.name(), escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);

    }

    /**
     * Creates an action that enables user to select the makeup item in the room
     * @return Select MakeUp action
     */
    static UserAction createSelectAction(@NotNull Enum<?> item, @NotNull GameRooms room){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition(item.name(), GameCondition.State.FALSE);
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        preItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition(item.name(), GameCondition.State.TRUE);
        GameCondition postGameActCond =  new GameCondition(EscapeGameAction.Option.SELECT.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("SELECT " + item.name(), escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);
    }

    /**
     * Creates an action that enables user to select the makeup item in the room
     * @return Select MakeUp action
     */
    static UserAction createUnSelectAction(@NotNull Enum<?> item, @NotNull GameRooms room){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition(item.name(), GameCondition.State.TRUE);
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.SELECT.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        preItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition(item.name(), GameCondition.State.FALSE);
        GameCondition postGameActCond =  new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("UNSELECT " + item.name(), escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);
    }

    /**
     * Creates an action that enables user to select the makeup item in the room
     * @return Select MakeUp action
     */
    static UserAction createSelectExitAction(@NotNull Enum<?> item, @NotNull GameRooms room){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition(item.name(), GameCondition.State.TRUE);
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.SELECT.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        preItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition(item.name(), GameCondition.State.FALSE);
        GameCondition postGameActCond =  new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("SELECT EXIT " + item.name(), escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);
    }

    /**
     * Creates an action that enables user to select use the makeup item in the room
     * @return SelectUse MakeUp action
     */
    static UserAction createSelectUseAction(@NotNull Enum<?> item, @NotNull GameRooms room){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition(item.name(), GameCondition.State.TRUE);
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.SELECT.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        preItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition(EscapeGameAction.Option.USE.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("SELECT_USE " + item.name() , escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);
    }

    /**
     * Creates an action that enables user to use the makeup item in the room
     * @return Use MakeUp action
     */
    static UserAction createUseAction(@NotNull Enum<?> item, @NotNull GameRooms room, @Nullable GameRooms subRoom){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition(item.name(), GameCondition.State.TRUE);
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.USE.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        preItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        GameCondition preSubRoomCond;
        ArrayList<GameCondition> preUsedItems = new ArrayList<>();
        if ( subRoom == null){
            preSubRoomCond = new GameCondition();
        }else {
            preSubRoomCond = new GameCondition(subRoom.name(), GameCondition.State.TRUE);
        }
        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition(item.name(), GameCondition.State.FALSE);
        GameCondition postGameActCond =  new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postItems = new ArrayList<>();
        postItems.add( new GameCondition(item.name(), GameCondition.State.FALSE));
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        GameCondition postSubRoomCond = new GameCondition();
        ArrayList<GameCondition> postUsedItems = new ArrayList<>();
        postUsedItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems, preUsedItems, preSubRoomCond);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems, postUsedItems, postSubRoomCond);

        return new UserAction("USE " + item.name(), escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);
    }

    /**
     * Creates an action that enables user to use the makeup item in the room
     * @return Use MakeUp action
     */
    static UserAction createZoomAction(@NotNull Enum<?> item, @NotNull GameRooms room, @NotNull GameRooms currentSubRoom, @NotNull GameRooms newSubRoom){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition();
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();
        GameCondition preSubRoomCondition = new GameCondition(currentSubRoom.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preUsedItems = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        GameCondition postSubRoomCondition = new GameCondition(newSubRoom.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postUsedItems = new ArrayList<>();

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems, preUsedItems, preSubRoomCondition);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems, postUsedItems, postSubRoomCondition);

        return new UserAction("ZOOM " + item.name(), escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);
    }
    /**
     * Creates an action that enables user to use the makeup item in the room
     * @return Use MakeUp action
     */
    static UserAction createBackAction(@NotNull GameRooms room, @NotNull GameRooms currentSubRoom, @NotNull GameRooms newSubRoom){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition();
        GameCondition preGameActCond =  new GameCondition();
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();
        GameCondition preSubRoomCondition = new GameCondition(currentSubRoom.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preUsedItems = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        GameCondition postSubRoomCondition = new GameCondition(newSubRoom.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postUsedItems = new ArrayList<>();

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems, preUsedItems, preSubRoomCondition);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems, postUsedItems, postSubRoomCondition);

        return new UserAction("BACK ", escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);
    }

}
