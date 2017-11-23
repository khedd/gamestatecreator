package ExampleEscapeGame;

import java.util.ArrayList;

public class Available {
    public static class Levels{
        public enum Main{
            MENU, FIRST_ROOM, EXIT
        }
        public enum First{
            LIVING_ROOM, TV_ROOM
        }
    }

    public static class Items{
        public enum First{
            MAKE_UP, DOOR_HANDLE, SCREW, COMBINED_DOOR_HANDLE, TV, USED_MAKE_UP, USED_COMBINED_DOOR_HANDLE
        }
    }

    public enum Actions{
        USE, COMBINE, PICK, DISMANTLE,
        SELECT, ZOOM, START, HOME, BACK,
        SELECT_USE, SELECT_COMBINE, DESELECT,
        SELECT_EXIT, EXIT
    }

    public static ArrayList<String> getAvailableActions(){
        ArrayList<String> actions = new ArrayList<>();
        for ( Available.Actions option:  Available.Actions.values()){
            actions.add( option.name());
        }
        return actions;
    }

    public static ArrayList<String> getFirstLevelItems (){
        ArrayList<String> items = new ArrayList<>();
        for ( Available.Items.First item:  Available.Items.First.values()){
            items.add( item.name());
        }
        return items;
    }

    public static ArrayList<String> getFistLevelRooms (){
        ArrayList<String> rooms = new ArrayList<>();
        for ( Levels.Main option:  Available.Levels.Main.values()){
            rooms.add( option.name());
        }
        return rooms;
    }


    public static ArrayList<String> getFirstLevelSubRooms (){
        ArrayList<String> subRooms = new ArrayList<>();
        for ( Levels.First option:  Available.Levels.First.values()){
            subRooms.add( option.name());
        }
        return subRooms;
    }
}
