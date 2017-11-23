package ExampleEscapeGame;
import EscapeGame.UserAction;
import java.util.ArrayList;
import java.util.EnumSet;

public class GraphActions {
    /**
     * Creates an actions list from all of the actions available
     */
    public static ArrayList<UserAction> levelOneActions(){

        ArrayList<UserAction> userActions = new ArrayList<>();
        userActions.add( ActionFactory.createStartAction(Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM));

        userActions.add( ActionFactory.createPickAction(Available.Items.First.MAKE_UP, Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM));
        userActions.add( ActionFactory.createSelectAction(Available.Items.First.MAKE_UP, Available.Levels.Main.FIRST_ROOM));
        userActions.add( ActionFactory.createDeselectAction(Available.Items.First.MAKE_UP, Available.Levels.Main.FIRST_ROOM));
        userActions.add( ActionFactory.createSelectExitAction(Available.Items.First.MAKE_UP, Available.Levels.Main.FIRST_ROOM));
        userActions.add( ActionFactory.createSelectUseAction(Available.Items.First.MAKE_UP, Available.Levels.Main.FIRST_ROOM));
        userActions.add( ActionFactory.createUseAction(Available.Items.First.MAKE_UP, Available.Items.First.USED_MAKE_UP, Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM));
//
        userActions.add( ActionFactory.createPickAction(Available.Items.First.DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM));
        userActions.add( ActionFactory.createSelectAction(Available.Items.First.DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        userActions.add( ActionFactory.createDeselectAction(Available.Items.First.DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        userActions.add( ActionFactory.createSelectExitAction(Available.Items.First.DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));

        userActions.add( ActionFactory.createPickAction(Available.Items.First.SCREW, Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM));
        userActions.add( ActionFactory.createSelectAction(Available.Items.First.SCREW, Available.Levels.Main.FIRST_ROOM));
        userActions.add( ActionFactory.createDeselectAction(Available.Items.First.SCREW, Available.Levels.Main.FIRST_ROOM));
        userActions.add( ActionFactory.createSelectExitAction(Available.Items.First.SCREW, Available.Levels.Main.FIRST_ROOM));

        userActions.add( ActionFactory.createSelectCombineAction(Available.Items.First.SCREW, Available.Levels.Main.FIRST_ROOM));
        userActions.add( ActionFactory.createSelectCombineAction(Available.Items.First.DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));

        userActions.add( ActionFactory.createCombineAction(Available.Items.First.DOOR_HANDLE, Available.Items.First.SCREW, Available.Items.First.COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        userActions.add( ActionFactory.createCombineAction(Available.Items.First.SCREW, Available.Items.First.DOOR_HANDLE, Available.Items.First.COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        userActions.add( ActionFactory.createSelectAction(Available.Items.First.COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        userActions.add( ActionFactory.createDeselectAction(Available.Items.First.COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        userActions.add( ActionFactory.createSelectExitAction(Available.Items.First.COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        userActions.add( ActionFactory.createSelectUseAction(Available.Items.First.COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM));
        userActions.add( ActionFactory.createUseAction(Available.Items.First.COMBINED_DOOR_HANDLE, Available.Items.First.USED_COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM));
        userActions.add( ActionFactory.createExitAction(Available.Items.First.USED_COMBINED_DOOR_HANDLE, Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM));

        userActions.add( ActionFactory.createDismantleAction(Available.Items.First.COMBINED_DOOR_HANDLE, EnumSet.of(Available.Items.First.SCREW, Available.Items.First.DOOR_HANDLE), Available.Levels.Main.FIRST_ROOM));

        userActions.add( ActionFactory.createZoomAction(Available.Items.First.TV, Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM, Available.Levels.First.TV_ROOM));
        userActions.add( ActionFactory.createBackAction(Available.Levels.Main.FIRST_ROOM, Available.Levels.First.LIVING_ROOM, Available.Levels.First.LIVING_ROOM));

        userActions.add( ActionFactory.createMenuAction());

        return userActions;
    }
}
