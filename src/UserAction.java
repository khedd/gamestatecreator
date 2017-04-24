/**
 * Denotes the actions in the game
 */
class UserAction {
    private String mPreRoom;
    private String mPostRoom;
    private String mName;

    UserAction (String name, String preRoom, String postRoom){
        mName = name;
        mPreRoom = preRoom;
        mPostRoom = postRoom;
    }

    GameState apply (GameState gameState){
        if ( gameState.mCurrentRoom.equals(mPreRoom)){
            GameState gameStateNew = new GameState ( gameState);
            gameStateNew.mCurrentRoom = mPostRoom;


            return gameStateNew;
        }else
            return null;
    }
}
