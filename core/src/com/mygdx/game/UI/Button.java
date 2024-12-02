package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;


// Buttons used for all UI
public class Button {

    private final float x;
    private final float y;
    private final float buttonWidth;
    private final float buttonHeight;
    private boolean isDown = false;
    private boolean isDownPrev = false;

    private final Texture textureUp;
    private final Texture textureDown;

    // ===================================================================================================================

    public Button(float x, float y, float buttonWidth, float buttonHeight, Texture textureUp, Texture textureDown) {
        this.x = x;
        this.y = y;
        this.buttonWidth    = buttonWidth;
        this.buttonHeight   = buttonHeight;

        this.textureUp      = textureUp;
        this.textureDown    = textureDown;
    }

    // ===================================================================================================================

    public void update(boolean checkTouch, int touchX, int touchY) {
        isDown = false;

        if (checkTouch) {
            int graphicsHeight = Gdx.graphics.getHeight();
            //Touch coordinates have origin in top-left instead of bottom left

            isDownPrev = isDown;
            if (touchX >= x && touchX <= (x + buttonWidth) && (graphicsHeight - touchY) >= y && (graphicsHeight - touchY) <= (y + buttonHeight)) {
                isDown = true;
            }
        }
    }

    // ===================================================================================================================

    public void draw(Batch batch) {
        if (!isDown) {
            batch.draw(textureUp, x, y, buttonWidth, buttonHeight);
        } else {
            batch.draw(textureDown, x, y, buttonWidth, buttonHeight);
        }
    }

    // ===================================================================================================================

    public boolean justPressed() {
        return isDown && !isDownPrev;
    }

    // ===================================================================================================================

    public void dispose(){
        textureUp.dispose();
        textureDown.dispose();
    }

    // ===================================================================================================================

    public boolean isDown() {
        return isDown;
    }

    // ===================================================================================================================
}
