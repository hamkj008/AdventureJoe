package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PlayerHealthBar {

    private final Sprite playerHealthBarBackground;
    private final Sprite playerHealthBar;
    private final Texture healthBarImage;
    private final Texture healthBarBackgroundImage;

    // ===================================================================================================================

    public PlayerHealthBar(){

        healthBarBackgroundImage    = new Texture("GUI/Cartoon Sci-Fi Game GUI/Misc/Cartoon Sci-Fi Game GUI_Progress Bar - Background.png");
        playerHealthBarBackground   = new Sprite(healthBarBackgroundImage);
        healthBarImage              = new Texture("GUI/Cartoon Sci-Fi Game GUI/Misc/Cartoon Sci-Fi Game GUI_Progress Bar - Green.png");
        playerHealthBar             = new Sprite(healthBarImage);

        // TODO: need a better way to set the position
        playerHealthBar.setPosition(150, Gdx.graphics.getHeight() - healthBarImage.getHeight() - 40);
        playerHealthBarBackground.setPosition(0,Gdx.graphics.getHeight() - healthBarBackgroundImage.getHeight());
    }


    // ===================================================================================================================

    public void modifyHealth(int hitPoints){
        playerHealthBar.setSize((float) (healthBarImage.getWidth() * hitPoints / 100), healthBarImage.getHeight());
    }

    // ===================================================================================================================

    public void draw(Batch batch){
        playerHealthBarBackground.draw(batch);
        playerHealthBar.draw(batch);
    }

    // ===================================================================================================================

    public void reset(){
        playerHealthBar.setSize(healthBarImage.getWidth() , healthBarImage.getHeight());
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "PlayerHealthBarDispose");

        healthBarBackgroundImage.dispose();
        healthBarImage.dispose();
    }


}
