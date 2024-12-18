package com.mygdx.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Actors.Characters.Character;
import com.mygdx.game.Actors.GameObjects.LevelEnd;
import com.mygdx.game.Actors.GameObjects.PowerUp;
import com.mygdx.game.Levels.LevelFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class GameHelper {

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
    public Animation<TextureRegion> processAnimation(String texturePath, int sheetCols, int sheetRows, int maxFrames, float animationSpeed) {

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
        return new Animation<>((animationSpeed / maxFrames), animationFrames);
    }

    // ===================================================================================================================

    /**
    * This method simplifies speed calculations.
    * It takes a desired speed float, multiplies it by delta time and returns the result.
    **/
    public float setMovement(float movementSpeed) {

        return movementSpeed * Gdx.graphics.getDeltaTime();
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

    // ===================================================================================================================

    /**
     *  Creates a table layout with a colored padding around content
     * @param padding  the amount of colored space bordered around the content
     * @param color    the color of the background
     */
    public Table getBackgroundTable(int padding, Color color) {

        Table table = new Table();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        table.background(new TextureRegionDrawable(new TextureRegion(texture)));

        table.pad(padding);     // the padding is applied but cant be seen untill getPrefWidth
        table.setSize(table.getPrefWidth(), table.getPrefHeight());  // this is the essential part to create the padding, otherwise it automatically sizes to the content
        return table;
    }

    // ===================================================================================================================

    /**
     *  Generates a set of random positions for all the objects in the game with a maximum X boundary the end of the level,
     *  and a minimum y boundary of the ground level (so objects should not appear in the ground).
     *  Also sets a minimum distance that any object can be from the next object
     *  @param levelXBoundary the end boundary of the level
     */
    public ArrayList<Vector2> generateRandomMinDistancePositions(int levelXBoundary, int numberOfPositions, int minPositionDistance) {

        ArrayList<Vector2> positions =  new ArrayList<>();

        for(int i =0; i < numberOfPositions; i++) {
            int retries = 0;
            Vector2 position = createRandomPosition(levelXBoundary);

            while (!isValidPosition(position, positions, minPositionDistance)) {
                retries++;
                if (retries == numberOfPositions) {
                    Gdx.app.log("dos", "Min distance reduced");
                    minPositionDistance -= 50;
                    retries = 0;
                }
                if (minPositionDistance <= 50) {
                    Gdx.app.log("dos", "cannot place object. Min distance reached");
                    break;
                }
                position = createRandomPosition(levelXBoundary);
            }
            Gdx.app.log("dos", "\nPOSITION VALID!: " + position + "\n");
            positions.add(position);
        }
        ArrayList<Float> sortedList = new ArrayList<>();
        for(Vector2 position : positions) {
            sortedList.add(position.x);
        }
        Collections.sort(sortedList);
        for(Float floatPos : sortedList) {
            Gdx.app.log("dos", " " + floatPos);
        }
        return positions;
    }


    public boolean isValidPosition(Vector2 positionToCheck, ArrayList<Vector2> positions, int minPositionDistance) {

        for(Vector2 position : positions) {
            Gdx.app.log("position", "pos: " + positionToCheck.x + "   range: " + (position.x - minPositionDistance) + " <-> " + (position.x + minPositionDistance));
            if(positionToCheck.x > position.x - minPositionDistance && positionToCheck.x < position.x + minPositionDistance) {
                return false;
            }
        }
        return true;
    }

    // ===================================================================================================================

    /**
     * Creates a random position for game objects, making sure they are above ground level
     * and dispersed along the x up to the x boundarylimit
     **/
    public static Vector2 createRandomPosition(int levelXBoundary) {

        Random rand     = new Random();
        float randX     = 200 + (levelXBoundary - 200) * rand.nextFloat();
        float randY     = LevelFactory.getCurrentGroundLevel() + ((Gdx.graphics.getHeight() - 200) - LevelFactory.getCurrentGroundLevel()) * rand.nextFloat();

        return new Vector2(randX, randY);
    }
}
