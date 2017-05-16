import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Used to create actions from {@link EscapeGameAction} such as:
 *  - Pick an item
 *  - Use an item
 *  - Select an item
 *  - Select{Use, Dismantle, Combine}
 *  - Zoom on an item
 *  - Combine an item
 *  - Dismantle an item
 *  - Exit using an item
 */
public class ActionFactory {

    /**
     * Creates an action that takes user back to Menu from every room
     * Only changes the {@link EscapeScenarioCondition#mLevel}
     * @return Return to Menu Action
     */
    static UserAction createMenuAction(){
        EscapeScenarioCondition preESC;
        //this exist in every level except Menu
        GameCondition preLevelCond = new GameCondition(GameRooms.MENU.name(), GameCondition.State.FALSE);
        GameCondition preSelectedCond = new GameCondition();
        GameCondition preGameActCond =  new GameCondition();
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition postESC;
        //menu condition is true
        GameCondition postLevelCond = new GameCondition(GameRooms.MENU.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        GameCondition postSubRoom =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postUsedItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();

        preESC = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        postESC = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems, postUsedItems, postSubRoom);

        return new UserAction("RETURN MENU", preESC, postESC);
    }


    /**
     * Creates a start action that is playable from Menu and takes to room and subRoom
     * @param room Room to go after action takes place
     * @param subRoom SubRoom to go after action takes place
     * @return Start the game action that navigates from Menu -> {room, subRoom}
     */
    static UserAction createStartAction(@NotNull Enum<?> room, @NotNull GameRooms subRoom){
        EscapeScenarioCondition preESC;
        //should be in menu
        GameCondition preLevelCond = new GameCondition(GameRooms.MENU.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition();
        GameCondition preGameActCond =  new GameCondition();
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition postESC;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        ArrayList<GameCondition> postUsedItems = new ArrayList<>();
        GameCondition postSubRoom = new GameCondition(subRoom.name(), GameCondition.State.TRUE);

        preESC = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        postESC = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems, postUsedItems, postSubRoom);

        return new UserAction("START GAME", preESC, postESC);
    }


    /**
     * Creates a pick action of item in room, subRoom. Gets an item from room to inventory
     * @param item Item to be picked
     * @param room Room that item is in
     * @param subRoom SubRoom that item is in
     * @return A pick action of item in room, subRoom
     */
    static UserAction createPickAction (@NotNull Enum<?> item, @NotNull GameRooms room, @NotNull  GameRooms subRoom){
        EscapeScenarioCondition escapeScenarioConditionPreStart;
        //should be in a room
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition();
        //action should be pick
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();
        ArrayList<GameCondition> preUsedItems = new ArrayList<>();
        //item to be picked should not be picked, we are not checking item list as these can change when used
        prePickedItems.add( new GameCondition( item.name(), GameCondition.State.FALSE));
        GameCondition preSubRoom = new GameCondition(subRoom.name(), GameCondition.State.TRUE);

        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        //add to items list
        postItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));
        //add to picked items to prevent picking again
        postPickedItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems, preUsedItems, preSubRoom);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("PICK " +  item.name(), escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);

    }

    /**
     * Creates a select action of an item in room, select the item in UI
     * @param item Item that can be selected
     * @param room Room that selection can happen
     * @return A selection action of item that can be used in room
     */
    static UserAction createSelectAction(@NotNull Enum<?> item, @NotNull GameRooms room){
        EscapeScenarioCondition preESC;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        //selection of item should be false
        GameCondition preSelectedCond = new GameCondition(item.name(), GameCondition.State.FALSE);
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        //item should exist in items, not checking picked as combined items do not list in picked
        preItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition postESC;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        //set selected to true
        GameCondition postSelectedCond = new GameCondition(item.name(), GameCondition.State.TRUE);
        //action becomes select
        GameCondition postGameActCond =  new GameCondition(EscapeGameAction.Option.SELECT.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();

        preESC = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        postESC = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("SELECT " + item.name(), preESC, postESC);
    }

    /**
     * Creates a deselect action of an item in room, deselect selected item using UI
     * @param item Item that can be deselected
     * @param room Room that deselection can happen
     * @return A deselection action of item that can be used in room
     */
    static UserAction createDeselectAction(@NotNull Enum<?> item, @NotNull GameRooms room){
        EscapeScenarioCondition preESC;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        //item should be selected
        GameCondition preSelectedCond = new GameCondition(item.name(), GameCondition.State.TRUE);
        //select should be the action
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.SELECT.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        preItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition postESC;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        //deselect the item
        GameCondition postSelectedCond = new GameCondition(item.name(), GameCondition.State.FALSE);
        //revert to pick action
        GameCondition postGameActCond =  new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();

        preESC = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        postESC = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("DESELECT " + item.name(), preESC, postESC);
    }

    /**
     * Creates a select exit action of an item in room, deselect using UI
     * @param item Item that selection can be exited using UI navigation
     * @param room Room that selection exit can happen
     * @return A selection exit action of item that can be used in room
     */
    static UserAction createSelectExitAction(@NotNull Enum<?> item, @NotNull GameRooms room){
        EscapeScenarioCondition preESC;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        //item should be selected
        GameCondition preSelectedCond = new GameCondition(item.name(), GameCondition.State.TRUE);
        //select should be the action
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.SELECT.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        preItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition postESC;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        //deselect the item
        GameCondition postSelectedCond = new GameCondition(item.name(), GameCondition.State.FALSE);
        //revert to pick action
        GameCondition postGameActCond =  new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();

        preESC = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        postESC = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);
        return new UserAction("SELECT EXIT " + item.name(), preESC, postESC);
    }

    /**
     * Creates a select use action of an item in room
     * @param item Item that selection can be used using UI navigation
     * @param room Room that selection use can happen
     * @return A selection use action of item that can be used in room
     */
    static UserAction createSelectUseAction(@NotNull Enum<?> item, @NotNull GameRooms room){
        EscapeScenarioCondition preESC;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        //item should be selected
        GameCondition preSelectedCond = new GameCondition(item.name(), GameCondition.State.TRUE);
        //action should be select
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.SELECT.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        //item should exist
        preItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition postESC;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        //change the action to use
        GameCondition postGameActCond =  new GameCondition(EscapeGameAction.Option.USE.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();

        preESC = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        postESC = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("SELECT_USE " + item.name() , preESC, postESC);
    }

    /**
     * Creates a select combine action of an item in room
     * @param item Item that selection can be combined using UI navigation
     * @param room Room that selection combine can happen
     * @return A selection combine action of item that can be used in room
     */
    static UserAction createSelectCombineAction(@NotNull Enum<?> item, @NotNull GameRooms room){
        EscapeScenarioCondition preESC;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        //item should be selected
        GameCondition preSelectedCond = new GameCondition(item.name(), GameCondition.State.TRUE);
        //action should be select
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.SELECT.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        preItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition postESC;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        //change action to combine
        GameCondition postGameActCond =  new GameCondition(EscapeGameAction.Option.COMBINE.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();

        preESC = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        postESC = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("SELECT_COMBINE " + item.name() , preESC, postESC);
    }

    /**
     * Creates a combine action that itemF + itemS => itemCombined. Should be noted that reverse combination
     * should also be called.
     * @param itemF First item that can be combined
     * @param itemS Second item that can be combined
     * @param itemCombined Resultant item after combination
     * @param room Room requirement for combine action
     * @return A combine action that takes itemF and itemS and results in itemCombined
     */
    static UserAction createCombineAction(@NotNull Enum<?> itemF, @NotNull Enum<?> itemS, @NotNull Enum<?> itemCombined, @NotNull GameRooms room){
        EscapeScenarioCondition preESC;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition(itemF.name(), GameCondition.State.TRUE);
        //action should be combine
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.COMBINE.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        //both items should exist
        preItems.add( new GameCondition(itemF.name(), GameCondition.State.TRUE));
        preItems.add( new GameCondition(itemS.name(), GameCondition.State.TRUE));
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        EscapeScenarioCondition postESC;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        //after combine occurs change the action to pick
        GameCondition postGameActCond =  new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postItems = new ArrayList<>();
        //remove both items from the list and add combination
        postItems.add( new GameCondition(itemF.name(), GameCondition.State.FALSE));
        postItems.add( new GameCondition(itemS.name(), GameCondition.State.FALSE));
        postItems.add( new GameCondition(itemCombined.name(), GameCondition.State.TRUE));
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();

        preESC = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems);
        postESC = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("COMBINE " + itemF.name() + " " + itemS.name() + " => " + itemCombined.name(), preESC, postESC);
    }

    /**
     * Creates a dismantle action. It dismantles the item and turns it to dismantledItems
     * @param item item to be dismantled
     * @param dismantledItems resultant items of dismantle operation
     * @param room room requirement for this action
     * @return An user action capable of dismantling the given item to dismantledItems in room
     */
    static UserAction createDismantleAction(@NotNull Enum<?> item, @NotNull EnumSet<?> dismantledItems, @NotNull GameRooms room){
        EscapeScenarioCondition preESC;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition(item.name(), GameCondition.State.TRUE);
        //action is select as this is done in UI
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.SELECT.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        //item to be dismantled should exist
        preItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();
        GameCondition preSubRoomCond = new GameCondition();
        ArrayList<GameCondition> preUsedItems = new ArrayList<>();

        EscapeScenarioCondition postESC;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition(item.name(), GameCondition.State.FALSE);
        GameCondition postGameActCond =  new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postItems = new ArrayList<>();
        //remove the item dismantled from the list
        postItems.add( new GameCondition(item.name(), GameCondition.State.FALSE));
        //add all of the dismantle result to item list
        for (Enum<?> dismantledItem: dismantledItems) {
            postItems.add( new GameCondition(dismantledItem.name(), GameCondition.State.TRUE));
        }
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        GameCondition postSubRoomCond = new GameCondition();
        ArrayList<GameCondition> postUsedItems = new ArrayList<>();

        preESC = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems, preUsedItems, preSubRoomCond);
        postESC = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems, postUsedItems, postSubRoomCond);

        return new UserAction("DISMANTLE " + item.name() + " " + dismantledItems.toString(), preESC, postESC);

    }

    /**
     * Creates a use action of item that can be used in room, subRoom
     * @param item Item to be used
     * @param room Room that item can be used in
     * @param subRoom SubRoom that item can be used in
     * @return An action describes use action
     */
    static UserAction createUseAction(@NotNull Enum<?> item, @NotNull GameRooms room, @NotNull GameRooms subRoom){
        EscapeScenarioCondition preESC;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition(item.name(), GameCondition.State.TRUE);
        //action should be use
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.USE.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        //item should exist
        preItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();

        GameCondition preSubRoomCond = new GameCondition(subRoom.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preUsedItems = new ArrayList<>();
        EscapeScenarioCondition postESC;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition(item.name(), GameCondition.State.FALSE);
        GameCondition postGameActCond =  new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postItems = new ArrayList<>();
        //remove the item from items
        postItems.add( new GameCondition(item.name(), GameCondition.State.FALSE));
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        GameCondition postSubRoomCond = new GameCondition();
        ArrayList<GameCondition> postUsedItems = new ArrayList<>();
        //add to used list
        postUsedItems.add( new GameCondition(item.name(), GameCondition.State.TRUE));

        preESC = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems, preUsedItems, preSubRoomCond);
        postESC = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems, postUsedItems, postSubRoomCond);

        return new UserAction("USE " + item.name(), preESC, postESC);
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
        EscapeScenarioCondition preESC;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition();
        //action should be pick
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();
        //current sub room requirement
        GameCondition preSubRoomCondition = new GameCondition(currentSubRoom.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preUsedItems = new ArrayList<>();

        EscapeScenarioCondition postESC;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        //change the sub room
        GameCondition postSubRoomCondition = new GameCondition(newSubRoom.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postUsedItems = new ArrayList<>();

        preESC = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems, preUsedItems, preSubRoomCondition);
        postESC = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems, postUsedItems, postSubRoomCondition);

        return new UserAction("ZOOM " + item.name(), preESC, postESC);
    }

    /**
     * Creates an exit action that checks if the item exists, only drawback is that it requires item to be used
     * However this is the only case available in our game
     * @param item Item to be checked in used list
     * @param room Room requirement of exit condition
     * @param subRoom SubRoom requirement of exit condition
     * @return An exit action described by picking item which is in used list and action is available only in
     * room and subRoom
     */
    static UserAction createExitAction (@NotNull Enum<?> item, @NotNull GameRooms room, @NotNull  GameRooms subRoom){
        EscapeScenarioCondition preESC;
        GameCondition preLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition preSelectedCond = new GameCondition();
        GameCondition preGameActCond =  new GameCondition(EscapeGameAction.Option.PICK.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preItems = new ArrayList<>();
        ArrayList<GameCondition> prePickedItems = new ArrayList<>();
        ArrayList<GameCondition> preUsedItems = new ArrayList<>();
        //item should exist as we are exiting using an item
        preUsedItems.add( new GameCondition( item.name(), GameCondition.State.TRUE));
        //sub room requirement should fit
        GameCondition preSubRoom = new GameCondition(subRoom.name(), GameCondition.State.TRUE);

        EscapeScenarioCondition postESC;
        //post condition is THE END
        GameCondition postLevelCond = new GameCondition("THE END", GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();

        preESC = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems, preUsedItems, preSubRoom);
        postESC = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems);

        return new UserAction("EXIT " +  item.name(), preESC, postESC);

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
        //which sub rooms will display back button
        GameCondition preSubRoomCondition = new GameCondition(currentSubRoom.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> preUsedItems = new ArrayList<>();

        EscapeScenarioCondition escapeScenarioConditionPostStart;
        GameCondition postLevelCond = new GameCondition(room.name(), GameCondition.State.TRUE);
        GameCondition postSelectedCond = new GameCondition();
        GameCondition postGameActCond =  new GameCondition();
        ArrayList<GameCondition> postItems = new ArrayList<>();
        ArrayList<GameCondition> postPickedItems = new ArrayList<>();
        //change the sub room
        GameCondition postSubRoomCondition = new GameCondition(newSubRoom.name(), GameCondition.State.TRUE);
        ArrayList<GameCondition> postUsedItems = new ArrayList<>();

        escapeScenarioConditionPreStart = new EscapeScenarioCondition(preLevelCond, preSelectedCond, preGameActCond, preItems, prePickedItems, preUsedItems, preSubRoomCondition);
        escapeScenarioConditionPostStart = new EscapeScenarioCondition(postLevelCond, postSelectedCond, postGameActCond, postItems, postPickedItems, postUsedItems, postSubRoomCondition);

        return new UserAction("BACK ", escapeScenarioConditionPreStart, escapeScenarioConditionPostStart);
    }

}
