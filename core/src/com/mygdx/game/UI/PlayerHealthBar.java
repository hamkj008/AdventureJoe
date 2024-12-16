package com.mygdx.game.UI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;



public class PlayerHealthBar extends Actor {

    private final Sprite playerHealthBar;
    private final Texture healthBarImage;

    // ===================================================================================================================

    public PlayerHealthBar(){

        healthBarImage   = new Texture("GUI/Cartoon Sci-Fi Game GUI/Misc/Cropped_Cartoon Sci-Fi Game GUI_Progress Bar - Red.png");
        playerHealthBar  = new Sprite(healthBarImage);

        setSize(playerHealthBar.getWidth(), playerHealthBar.getHeight());       // Have to set a size so that the
    }

    // ===================================================================================================================

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(healthBarImage, playerHealthBar.getX(), playerHealthBar.getY(), playerHealthBar.getWidth(), playerHealthBar.getHeight());        // getX() instead of sprite.getX() allows the hbox to manage the size and position
    }

    // ===================================================================================================================

    public void modifyHealth(int hitPoints) {
        playerHealthBar.setSize((playerHealthBar.getWidth() * (hitPoints / 100f)), getHeight());
    }

    // ===================================================================================================================

    public void reset(){
        playerHealthBar.setSize(healthBarImage.getWidth() , healthBarImage.getHeight());
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "PlayerHealthBar.dispose");

        healthBarImage.dispose();
    }

    // ===================================================================================================================

    public Sprite getSprite() { return playerHealthBar; }
}
