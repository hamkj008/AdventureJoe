package com.mygdx.game.Levels;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Actors.Characters.Character;
import com.mygdx.game.Actors.Characters.Player.Player;



/**
 * Creates levels. Loads a tmx map, then applies a tmx renderer, viewports cameras and layers.
 * Implements infinite scrolling, and parallax scrolling.
 */
@SuppressWarnings("FieldCanBeLocal")
public class LevelCreator {

    private final int graphicsWidth;
    private final int graphicsHeight;

    private TiledMap loadedMap;

    // Map Renderer
    private TiledMapRenderer foregroundTiledMapRenderer;
    private TiledMapRenderer backgroundTiledMapRenderer;

    // Viewports
    private FitViewport foregroundViewport1;
    private FitViewport foregroundViewport2;
    private FitViewport backgroundViewport1;
    private FitViewport backgroundViewport2;

    private int[] foregroundMapLayers;
    private int[] backgroundMapLayers;

    private Rectangle groundRectangle;
    private Rectangle[] collisionRectangle;
    private Sprite[] collisionSprites1;
    private Sprite[] collisionSprites2;
    private Sprite currentPlatform;

    private float groundLevel;
    private final int platformClearanceFactor = 50;      // The additional clearance needed to be off of a platform to avoid confusion between two states

    private int enemyKilledExitThreshold;
    private int levelXBoundary = 0;


    // ===================================================================================================================

    public LevelCreator() {
        Gdx.app.log("flow", "LevelCreator");

        // Convenient to set up getWidth() and getHeight() here so they are easier to use.
        graphicsWidth   = Gdx.graphics.getWidth();
        graphicsHeight  = Gdx.graphics.getHeight();
    }

    // ===================================================================================================================

    public void createLevel(String filePath, int[] foregroundLayers, int[] backgroundLayers, int numberOfCollisionObjects, int levelXBoundary) {
        Gdx.app.log("flow", "createLevel");

        this.levelXBoundary = levelXBoundary;
        loadedMap = new TmxMapLoader().load(filePath);

        foregroundTiledMapRenderer  = new OrthogonalTiledMapRenderer(loadedMap);
        backgroundTiledMapRenderer  = new OrthogonalTiledMapRenderer(loadedMap);
        backgroundMapLayers         = backgroundLayers;
        foregroundMapLayers         = foregroundLayers;
        currentPlatform             = new Sprite();

        /*
         * Map scrolling is first implemented with two viewports that will both draw the level. The first and second viewport will be drawn.
         * Once viewport 1 leaves the screen drawing stops, then the camera is teleported back behind viewport 2 while viewport 2 is being drawn.
         * Once viewport 2 leaves the screen it is teleported behind viewport 1 etc. If the player moves in the opposite direction it is the same but in reverse.
         */
        // --------- CAMERAS ------------------------------------

        // Foreground cameras. Two cameras used to execute map scrolling
        OrthographicCamera foregroundCamera1 = new OrthographicCamera();
        foregroundCamera1.setToOrtho(false, graphicsWidth, graphicsHeight);

        OrthographicCamera foregroundCamera2 = new OrthographicCamera();
        foregroundCamera2.setToOrtho(false, graphicsWidth, graphicsHeight);
        // Foreground Camera 2's starting position is behind viewport 1.
        foregroundCamera2.position.x = -graphicsWidth + (graphicsWidth / 2f);

        // Background camera used for parallax scrolling
        OrthographicCamera backgroundCamera1 = new OrthographicCamera();
        backgroundCamera1.setToOrtho(false, graphicsWidth, graphicsHeight);

        OrthographicCamera backgroundCamera2 = new OrthographicCamera();
        backgroundCamera2.setToOrtho(false, graphicsWidth, graphicsHeight);
        // Background Camera 2's starting position is behind viewport 1.
        backgroundCamera2.position.x = -graphicsWidth + (graphicsWidth / 2f);


        foregroundViewport1 = new FitViewport(graphicsWidth, graphicsHeight, foregroundCamera1);
        foregroundViewport2 = new FitViewport(graphicsWidth, graphicsHeight, foregroundCamera2);
        backgroundViewport1 = new FitViewport(graphicsWidth, graphicsHeight, backgroundCamera1);
        backgroundViewport2 = new FitViewport(graphicsWidth, graphicsHeight, backgroundCamera2);


        // ---------- Ground Collision ------------------------
        groundRectangle = new Rectangle();
        MapObject groundCollision = loadedMap.getLayers().get("GroundCollision").getObjects().get("Ground");
        if(groundCollision instanceof RectangleMapObject) {
            RectangleMapObject rmo = (RectangleMapObject) groundCollision;
            groundRectangle = rmo.getRectangle();
        }

        groundLevel = groundRectangle.getY() + groundRectangle.getHeight();


        // ----  Platform Collision  --------------------------------
        collisionRectangle  = new Rectangle[numberOfCollisionObjects];
        collisionSprites1   = new Sprite[numberOfCollisionObjects];
        collisionSprites2   = new Sprite[numberOfCollisionObjects];

        // Extract all the collision objects from the map, and duplicate them ready for infinite map scrolling.
        MapLayer collisionLayer = loadedMap.getLayers().get("PlatformCollision");
        for(int i = 0; i < collisionLayer.getObjects().getCount(); i++) {
            if(collisionLayer.getObjects().get(i) instanceof RectangleMapObject) {
                RectangleMapObject rmo  = (RectangleMapObject) collisionLayer.getObjects().get(i);
                collisionRectangle[i]   = rmo.getRectangle();
                collisionSprites1[i]    = new Sprite();
                collisionSprites2[i]    = new Sprite();

                collisionSprites1[i].setBounds(collisionRectangle[i].getX(), collisionRectangle[i].getY(),
                        collisionRectangle[i].getWidth(), collisionRectangle[i].getHeight());

                collisionSprites2[i].setBounds(collisionRectangle[i].getX(),
                        collisionRectangle[i].getY(), collisionRectangle[i].getWidth(), collisionRectangle[i].getHeight());
                collisionSprites2[i].setX(collisionRectangle[i].getX() + graphicsWidth);
            }
        }
    }

    // ===================================================================================================================

    /**
    *  Goes through both sets of platforms for the map and applies the platform mechanics.
    **/
    public void checkMapPlatformCollision(Player player) {
        
        for (Sprite collisionSprite : collisionSprites1) {
            platformMechanics(player, collisionSprite);
        }

        for (Sprite collisionSprite : collisionSprites2) {
            platformMechanics(player, collisionSprite);
        }
    }

    // ===================================================================================================================

    /**
    *  Checks and applies if the player overlaps a platform and is high enough then it is set to the platform.
    *  If the player exceeds the bounds of the platform it starts falling.
    *  @ collisionSprite = the current platfom that the player is on
    **/
    public void platformMechanics(Player player, Sprite collisionSprite) {

        if(!player.getIsGrounded() && player.getSprite().getBoundingRectangle().overlaps(collisionSprite.getBoundingRectangle()) && player.getCharacterState() == Character.CharacterState.FALLING) {

            // If the player is at a high enough level to the platform
            if (player.getSprite().getY() > collisionSprite.getY()) {     // There must be enough sprite to overlap (why collisionSprite.getHeight is not included)

                // Assign the player to the platform.
                player.getSprite().setY(collisionSprite.getY() + collisionSprite.getHeight());
                player.setPlayerLevel(collisionSprite.getY() + collisionSprite.getHeight());

                player.setIsGrounded(true);
                player.setCharacterState(Character.CharacterState.IDLE);
                currentPlatform = collisionSprite;          // Store the platform the player collided with to check when it is cleared
            }
        }

        // The player is on a platform not the ground
        if(player.getIsGrounded() && player.getPlayerLevel() > LevelFactory.getCurrentGroundLevel()) {

            // If the player moves off the platform to the left or right
            if ((player.getSprite().getX() + player.getSprite().getWidth()) < (currentPlatform.getX() - platformClearanceFactor) ||
                    (player.getSprite().getX() > (currentPlatform.getX() + currentPlatform.getWidth() + platformClearanceFactor))) {
                player.setCharacterState(Character.CharacterState.FALLING);
            }
        }
    }

    // ===================================================================================================================

    /**
    * Moves the collision objects in the opposite direction to oppose the cameras movement, giving the impression that they are static objects.
    * All "compensate" methods do this.
    **/
    public void collisionCompensateCamera(float cameraPositionAmount) {

        for (Sprite collisionSprite : collisionSprites1) {
            collisionSprite.translate(cameraPositionAmount, 0);
        }

        for (Sprite collisionSprite : collisionSprites2) {
            collisionSprite.translate(cameraPositionAmount, 0);
        }
    }

    // ===================================================================================================================

    /**
    * Moves the camera over the map.
    */
    public void moveCamera(Player player) {

        if(player.getDirection() == Character.Direction.LEFT) {
            foregroundViewport1.getCamera().translate(-player.getPositionAmount().x, 0, 0);
            foregroundViewport2.getCamera().translate(-player.getPositionAmount().x, 0, 0);
            backgroundViewport1.getCamera().translate(-player.getPositionAmount().x / 8, 0, 0);
            backgroundViewport2.getCamera().translate(-player.getPositionAmount().x / 8, 0, 0);
        }
        else {
            foregroundViewport1.getCamera().translate(player.getPositionAmount().x, 0, 0);
            foregroundViewport2.getCamera().translate(player.getPositionAmount().x, 0, 0);
            backgroundViewport1.getCamera().translate(player.getPositionAmount().x / 8, 0, 0);
            backgroundViewport2.getCamera().translate(player.getPositionAmount().x / 8, 0, 0);
        }
    }

    // ===================================================================================================================

    /**
     * Map scrolling is first implemented with two viewports that will both draw the level. The first and second viewport will be drawn.
     * Once viewport 1 leaves the screen drawing stops, then the camera is teleported back behind viewport 2 while viewport 2 is being drawn.
     * Once viewport 2 leaves the screen it is teleported behind viewport 1 etc. If the player moves in the opposite direction it is the same but in reverse.
     *
     * The same system is used separately for background layers and foreground layers, i.e there are two background viewports and two foreground viewports.
     */
    public void renderMap(Player player) {

        // Render the foreground
        if (player.getDirection() == Character.Direction.RIGHT) {

            // ---- BACKGROUND LAYERS ----------------------

            /* Draw the first map camera for one full length untill it is out of view.
             * Once out of view it is reset back to its original position to come into view again.
             */
            if (backgroundViewport1.getCamera().position.x > graphicsWidth + (graphicsWidth / 2f)) {
                backgroundViewport1.getCamera().position.x = (-graphicsWidth / 2f);
            }
            else {
                backgroundViewport1.update(graphicsWidth, graphicsHeight);
                backgroundTiledMapRenderer.setView((OrthographicCamera) backgroundViewport1.getCamera());
                backgroundTiledMapRenderer.render(backgroundMapLayers);
            }

            /* Draw the second map camera for one full length untill it is out of view.
             * Once out of view it is reset back to its original position to come into view again.
             */
            if (backgroundViewport2.getCamera().position.x > graphicsWidth + (graphicsWidth / 2f)) {
                backgroundViewport2.getCamera().position.x = (-graphicsWidth / 2f);
            }
            else {
                backgroundViewport2.update(graphicsWidth, graphicsHeight);
                backgroundTiledMapRenderer.setView((OrthographicCamera) backgroundViewport2.getCamera());
                backgroundTiledMapRenderer.render(backgroundMapLayers);
            }


            // ---- FOREGROUND LAYERS -----

            /* Draw the first map camera for one full length untill it is out of view.
             * Once out of view it is reset back to its original position to come into view again.
             */
            if (foregroundViewport1.getCamera().position.x > graphicsWidth + (graphicsWidth / 2f)) {
                // Reset
                foregroundViewport1.getCamera().position.x = (-graphicsWidth / 2f);
                for (Sprite collisionSprite : collisionSprites1) {
                    collisionSprite.setX(collisionSprite.getX() + (graphicsWidth * 2));
                }
            }
            else {
                foregroundViewport1.update(graphicsWidth, graphicsHeight);
                foregroundTiledMapRenderer.setView((OrthographicCamera) foregroundViewport1.getCamera());
                foregroundTiledMapRenderer.render(foregroundMapLayers);
            }

            /* Draw the second map camera for one full length untill it is out of view.
             * Once out of view it is reset back to its original position to come into view again.
             */
            if (foregroundViewport2.getCamera().position.x > graphicsWidth + (graphicsWidth / 2f)) {
                foregroundViewport2.getCamera().position.x = (-graphicsWidth / 2f);
                for (Sprite collisionSprite : collisionSprites2) {
                    collisionSprite.setX(collisionSprite.getX() + (graphicsWidth * 2));
                }
            }
            else {
                foregroundViewport2.update(graphicsWidth, graphicsHeight);
                foregroundTiledMapRenderer.setView((OrthographicCamera) foregroundViewport2.getCamera());
                foregroundTiledMapRenderer.render(foregroundMapLayers);
            }
        }

        if (player.getDirection() == Character.Direction.LEFT) {

            // ---- BACKGROUND LAYERS ----------------------

            if (backgroundViewport1.getCamera().position.x < -graphicsWidth / 2f) {
                backgroundViewport1.getCamera().position.x = graphicsWidth + (graphicsWidth / 2f);
            }
            else {
                backgroundViewport1.update(graphicsWidth, graphicsHeight);
                backgroundTiledMapRenderer.setView((OrthographicCamera) backgroundViewport1.getCamera());
                backgroundTiledMapRenderer.render(backgroundMapLayers);
            }

            if (backgroundViewport2.getCamera().position.x < -graphicsWidth / 2f) {
                backgroundViewport2.getCamera().position.x = graphicsWidth + (graphicsWidth / 2f);
            }
            else {
                backgroundViewport2.update(graphicsWidth, graphicsHeight);
                backgroundTiledMapRenderer.setView((OrthographicCamera) backgroundViewport2.getCamera());
                backgroundTiledMapRenderer.render(backgroundMapLayers);
            }


            // ---- FOREGROUND LAYERS ----------------------

            if (foregroundViewport1.getCamera().position.x < -graphicsWidth / 2f) {
                foregroundViewport1.getCamera().position.x = graphicsWidth + (graphicsWidth / 2f);
                for (Sprite collisionSprite : collisionSprites1) {
                    collisionSprite.setX(collisionSprite.getX() - (graphicsWidth * 2f));
                }
            }
            else {
                foregroundViewport1.update(graphicsWidth, graphicsHeight);
                foregroundTiledMapRenderer.setView((OrthographicCamera) foregroundViewport1.getCamera());
                foregroundTiledMapRenderer.render(foregroundMapLayers);
            }

            if (foregroundViewport2.getCamera().position.x < -graphicsWidth / 2f) {
                foregroundViewport2.getCamera().position.x = graphicsWidth + (graphicsWidth / 2f);
                for (Sprite collisionSprite : collisionSprites2) {
                    collisionSprite.setX(collisionSprite.getX() - (graphicsWidth * 2));
                }
            }
            else {
                foregroundViewport2.update(graphicsWidth, graphicsHeight);
                foregroundTiledMapRenderer.setView((OrthographicCamera) foregroundViewport2.getCamera());
                foregroundTiledMapRenderer.render(foregroundMapLayers);
            }
        }
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "levelCreator.dispose");

        loadedMap.dispose();
    }

    // ======================================= GETTERS AND SETTERS ==================================================================

    public float getGroundLevel() { return groundLevel; }

    public int getEnemyKilledExitThreshold() { return enemyKilledExitThreshold; }

    public void setEnemyKilledExitThreshold(int enemyKilledExitThreshold) {
        this.enemyKilledExitThreshold = enemyKilledExitThreshold;
    }

    public int getLevelXBoundary() { return levelXBoundary; }

    public Sprite[] getCollisionSprites1() { return collisionSprites1; }

    public Sprite[] getCollisionSprites2() { return collisionSprites2; }
}
