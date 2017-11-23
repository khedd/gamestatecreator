package ExampleEscapeGame;

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

}
