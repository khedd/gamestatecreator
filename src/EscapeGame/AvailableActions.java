package EscapeGame;

/**
 * Actions that are available in an escape games
 */
public class AvailableActions {
    @SuppressWarnings("unused")
    public enum Option{
        USE, COMBINE, PICK, DISMANTLE,
        SELECT, ZOOM, START, HOME, BACK,
        SELECT_USE, SELECT_COMBINE, DESELECT,
        SELECT_EXIT, EXIT;
    }
}