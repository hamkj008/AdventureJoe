package com.mygdx.game.Actors.GameObjects;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Actors.Characters.Character;
import com.mygdx.game.Actors.Characters.Player.Player;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.UI.UICounters;


public class LevelEnd extends Character {

    public enum GoalType    { PRINCESS, BABY }
    public enum GoalState   { IDLE, SPELL }

    private GoalType goalType       = GoalType.BABY;
    private GoalState goalState     = GoalState.IDLE;

    // ---- ANIMATIONS CONTAINERS -------------------------
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> spellAnimation;

    // ---- ANIMATIONS -------------------------
    private final Animation<TextureRegion> princessIdleAnimation;
    private final Animation<TextureRegion> princessSpellAnimation;
    private final Animation<TextureRegion> babyIdleAnimation;
    private final Animation<TextureRegion> babySpellAnimation;

    private boolean endLevel;


    // ===================================================================================================================

    public LevelEnd() {

        // Initialize size and start position
        super.setDirection(Direction.LEFT);
        super.getStartPosition().x = GameScreen.getInstance().getGameStateController().getLevelFactory().getCurrentLevel().getLevelXBoundary();
        super.getSprite().setPosition(getStartPosition().x, getStartPosition().y);

        // ---- ANIMATIONS -------------------------
        // Load all animation frames into animation objects using Game Helper.
        princessIdleAnimation   = GameScreen.getInstance().getHelper().processAnimation("Game Characters/LevelEnd/Princess/Idle Blinking.png", 8, 3, 24, 0.033f);
        princessSpellAnimation  = GameScreen.getInstance().getHelper().processAnimation("Game Characters/LevelEnd/Princess/Spell.png", 5, 3, 15, 0.033f);

        babyIdleAnimation       = GameScreen.getInstance().getHelper().processAnimation("Game Characters/LevelEnd/Super Baby/Idle Blinking.png", 6, 3, 18, 0.033f);
        babySpellAnimation      = GameScreen.getInstance().getHelper().processAnimation("Game Characters/LevelEnd/Super Baby/Spell.png", 4, 3, 12, 0.033f);
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {
        switchStates();
    }

    // ===================================================================================================================

    public void switchStates() {

        if (goalType == GoalType.PRINCESS) {
            idleAnimation = princessIdleAnimation;
            spellAnimation = princessSpellAnimation;
        }

        else if (goalType == GoalType.BABY) {
            idleAnimation = babyIdleAnimation;
            spellAnimation = babySpellAnimation;
        }

        if(goalState == GoalState.IDLE) {
            super.loopingAnimation(idleAnimation);
        }

        if(goalState == GoalState.SPELL) {
            if(super.nonLoopingAnimation(spellAnimation)) {
                // change level
                endLevel = true;
            }
        }
    }

    // ===================================================================================================================

    // Adds additional AI states specific to this enemy, primarily its Attack state
    public void setAIStates(Player player) {

        if ((GameScreen.getInstance().getHelper().getCenteredSpritePosition(player.getSprite()).x - 200) >
                GameScreen.getInstance().getHelper().getCenteredSpritePosition(getSprite()).x && distanceFromPlayer(player) < 1000) {
            super.setDirection(Direction.RIGHT);
        }
        else if ((GameScreen.getInstance().getHelper().getCenteredSpritePosition(player.getSprite()).x + 200) <
                GameScreen.getInstance().getHelper().getCenteredSpritePosition(getSprite()).x && distanceFromPlayer(player) < 1000) {
            super.setDirection(Direction.LEFT);
        }

        // ----- End Level Condition -------------------
        // If the players reaches the endGoal such that the bounding boxes intersect, then the level end goal has been reached.
        if(GameScreen.getInstance().getGameStateController().getPlayer().getSprite().getBoundingRectangle().overlaps(getSprite().getBoundingRectangle())) {
            if(UICounters.enemiesKilled == GameScreen.getInstance().getGameStateController().getLevelFactory().getCurrentLevel().getEnemyKilledExitThreshold()) {
                goalState = GoalState.SPELL;
            }
        }
    }

    // ===================================================================================================================


    public boolean getEndLevel() { return endLevel; }

    public void setGoalType(GoalType goalType) { this.goalType = goalType; }
}
