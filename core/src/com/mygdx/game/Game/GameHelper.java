package com.mygdx.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Actors.Characters.Character;



public class GameHelper {

    float deltaTime;
    private static boolean isLogScheduled   = false;
    private static String logTag            = "DefaultTag";
    private static String logMessage        = "DefaultMessage";

    // ===================================================================================================================

    public GameHelper() {}

    // ===================================================================================================================

    /**
     *  This method takes in a spritesheet, as well as how many rows, columns and total frames it has, splits it into TextureRegions,
     *  and then loads those regions into an animation. The returned animation contains all the frames.
     **/
    public Animation<TextureRegion> processAnimation(String texturePath, int sheetCols, int sheetRows, int maxFrames) {

        Texture texture                 = new Texture(texturePath);
        TextureRegion[][] temp          = TextureRegion.split(texture, texture.getWidth() / sheetCols, texture.getHeight() / sheetRows);
        TextureRegion[] animationFrames = new TextureRegion[maxFrames];

        int index = 0;
        for (int i = 0; i < sheetRows; i++) {
            for (int j = 0; j < sheetCols; j++) {
                if(index < maxFrames) {
                    animationFrames[index++] = temp[i][j];
                }
            }
        }
        return new Animation<>(0.033f, animationFrames);
    }

    // ===================================================================================================================

    /**
    * This method simplifies speed calculations.
    * It takes a desired speed float, multiplies it by delta time and returns the result.
    **/
    public float setMovement(float movementSpeed) {

        deltaTime = Gdx.graphics.getDeltaTime();

        return movementSpeed * deltaTime;
    }

    // ===================================================================================================================

    // Finds and returns the centre of the sprite for when this is needed.
    public Vector2 getCenteredSpritePosition(Sprite sprite) {
        float x = sprite.getX() + (sprite.getWidth() / 2);
        float y = sprite.getY() + (sprite.getHeight() / 2);

        return new Vector2(x, y);
    }

    // ===================================================================================================================

    public void flipSprite(Character.Direction direction, TextureRegion currentFrame) {

        // Flips the sprite according to the correct direction.
        if (direction == Character.Direction.LEFT && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        else if (direction == Character.Direction.RIGHT && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
    }

    // ===================================================================================================================

    public static void delayLog(float intervalSeconds, String tag, String message) {

        logTag      = tag; // Update log tag
        logMessage  = message; // Update log message

        if (!isLogScheduled) {
            isLogScheduled = true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    Gdx.app.log(logTag, logMessage);
                }
            }, 0, intervalSeconds); // Delay of 0 seconds, repeats every intervalSeconds
        }
    }
}
