import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;

/**
 * Used to create actions from {@link EscapeGameAction} such as:
 *  - Pick an item
 *  - Use an item
 *  - Select an item
 *  - Select{Use, Dismantle, Combine}
 *  - Zoom on an item
 *  - TODO Combine an item
 *  - TODO Dismantle an item
 *  - TODO Exit using an item
 *
 * TODO action definitions what it is meant by pick combine etc.
 */
public class ActionFactory {

    /**
     * Creates an action that takes user back to Menu from every room
     * @return Return to Menu Action
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
     * Creates a start action that is playable from Menu and takes to room and subRoom
     * @param room Room to go after action takes place
     * @param subRoom SubRoom to go after action takes place
     * @return Start the game action that navigates from Menu -> {room, subRoom}
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
     * Creates a pick action of item in room, subRoom
     * @param item Item to be picked
     * @param room Room that item is in
     * @param subRoom SubRoom that item is in
     * @return A pick action of item in room, subRoom
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
     * Creates a select action of an item in room
     * @param item Item that can be selected
     * @param room Room that selection can happen
     * @return A selection action of item that can be used in room
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
     * Creates a deselect action of an item in room
     * @param item Item that can be deselected
     * @param room Room that deselection can happen
     * @return A deselection action of item that can be used in room
     */
    static UserAction createDeselectAction(@NotNull Enum<?> item, @NotNull GameRooms room){
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

        return new UserAction("DESELECT " + item.name(), escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);
    }

    /**
     * Creates a select exit action of an item in room
     * @param item Item that selection can be exited using UI navigation
     * @param room Room that selection exit can happen
     * @return A selection exit action of item that can be used in room
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
     * Creates a select use action of an item in room
     * @param item Item that selection can be used using UI navigation
     * @param room Room that selection use can happen
     * @return A selection use action of item that can be used in room
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
     * Creates a use action of item that can be used in room, subRoom
     * @param item Item to be used
     * @param room Room that item can be used in
     * @param subRoom SubRoom that item can be used in
     * @return An action describes use action
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
     * Creates a zoom action that navigates the user from currentSubRoom to newSubRoom
     * @param item Item used to navigate the user TODO not being used
     * @param room Room that item can be used to zoom
     * @param currentSubRoom currentSubRoom that item can be used to zoom
     * @param newSubRoom NewSubRoom to navigate to
     * @return An action describing a zoom action of clicking an object that takes user
     * to another newSubRoom
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
     * Creates a back action to navigate to the initial subRoom, can be used after {@link #createZoomAction(Enum, GameRooms, GameRooms, GameRooms)}
     * @param room Current room
     * @param currentSubRoom SubRoom that back action exists
     * @param newSubRoom SubRoom that user will navigate after the action
     * @return An action used to return to initial room after zoom operation
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
