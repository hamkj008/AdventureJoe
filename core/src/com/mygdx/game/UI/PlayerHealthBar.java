package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PlayerHealthBar {

    private final Sprite playerHealthBar;
    private final Texture healthBarImage;
//    private final Sprite playerHealthBarBackground;
//    private final Texture healthBarBackgroundImage;

    // ===================================================================================================================

    public PlayerHealthBar(){

        healthBarImage              = new Texture("GUI/Cartoon Sci-Fi Game GUI/Misc/Cartoon Sci-Fi Game GUI_Progress Bar - Red.png");
        playerHealthBar             = new Sprite(healthBarImage);
//        healthBarBackgroundImage    = new Texture("GUI/Cartoon Sci-Fi Game GUI/Misc/Cartoon Sci-Fi Game GUI_Progress Bar - Background.png");
//        playerHealthBarBackground   = new Sprite(healthBarBackgroundImage);

        playerHealthBar.setPosition(50, Gdx.graphics.getHeight() - healthBarImage.getHeight() - 20);
//        playerHealthBarBackground.setPosition(0,Gdx.graphics.getHeight() - healthBarBackgroundImage.getHeight());
    }


    // ===================================================================================================================

    public void modifyHealth(int hitPoints){
        playerHealthBar.setSize((float) (healthBarImage.getWidth() * hitPoints / 100), healthBarImage.getHeight());
    }

    // ===================================================================================================================

    public void draw(Batch batch) {

        playerHealthBar.draw(batch);
//        playerHealthBarBackground.draw(batch);
    }

    // ===================================================================================================================

    public void reset(){
        playerHealthBar.setSize(healthBarImage.getWidth() , healthBarImage.getHeight());
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "PlayerHealthBarDispose");

        healthBarImage.dispose();
//        healthBarBackgroundImage.dispose();
    }


}
