package com.mygdx.game.UI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.Game.GameStateController;
import com.mygdx.game.Game.MasterStateController;



public class UIControlsOverlay extends Actor {

    // Buttons
    private final Button leftButton;
    private final Button rightButton;

    private final Button jumpButton;
    private final Button shootButton;

    private final ImageButton musicBtn;
    private final ImageButton pauseBtn;


    // ===================================================================================================================

    public UIControlsOverlay(Stage stage) {
        Gdx.app.log("flow", "UIControlsOverlay");

        float buttonSize    = Gdx.graphics.getHeight() * 0.07f;

        // ============== CHARACTER CONTROLS =================

        // Normal
        Texture leftBtnImage            = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Normal/Buttons Pack - Normal_Button Normal - Left Arrow.png");
        Texture rightBtnImage           = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Normal/Buttons Pack - Normal_Button Normal - Right Arrow.png");
        Texture shootBtnImage           = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Normal/Buttons Pack - Normal_Button Normal - Increase.png");
        Texture jumpBtnImage            = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Normal/Buttons Pack - Normal_Button Normal - Up Arrow.png");

        // Pressed
        Texture pressLeftBtnImage       = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Disabled/Buttons Pack - Disabled_Button Disabled - Left Arrow.png");
        Texture pressRightBtnImage      = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Disabled/Buttons Pack - Disabled_Button Disabled - Right Arrow.png");
        Texture pressShootBtnImage      = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Disabled/Buttons Pack - Disabled_Button Disabled - Increase.png");
        Texture pressJumpBtnImage       = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Disabled/Buttons Pack - Disabled_Button Disabled - Up Arrow.png");

        leftButton      = new Button(0.0f, buttonSize, buttonSize, buttonSize, leftBtnImage, pressLeftBtnImage);
        rightButton     = new Button(buttonSize * 2, buttonSize, buttonSize, buttonSize, rightBtnImage, pressRightBtnImage);
        shootButton     = new Button(Gdx.graphics.getWidth() - buttonSize * 3, buttonSize , buttonSize, buttonSize, shootBtnImage, pressShootBtnImage);
        jumpButton      = new Button(Gdx.graphics.getWidth() - buttonSize, buttonSize, buttonSize, buttonSize, jumpBtnImage, pressJumpBtnImage);


        // ============== MENU CONTROLS =================

        Texture musicBtnImage           = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Normal/Buttons Pack - Normal_Button Normal - Music.png");
        Texture musicBtnDisabledImage   = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Disabled/Buttons Pack - Disabled_Button Disabled - Music.png");
        Texture pauseBtnImage           = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Normal/Buttons Pack - Normal_Button Normal - Pause.png");
        Texture pauseBtnDisabledImage   = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Disabled/Buttons Pack - Disabled_Button Disabled - Pause.png");

        musicBtn = new ImageButton(musicBtnImage, musicBtnDisabledImage,
                ImageButton.ButtonType.MUSIC, new Vector2(Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight() - 100));
        pauseBtn = new ImageButton(pauseBtnImage, pauseBtnDisabledImage,
                ImageButton.ButtonType.PAUSE, new Vector2(Gdx.graphics.getWidth() - 160, Gdx.graphics.getHeight() - 100));

        // Register for events
        stage.addActor(musicBtn);
        stage.addActor(pauseBtn);
    }

    // ===================================================================================================================

    @Override
    public void draw(Batch batch, float alpha) {

        leftButton.draw(batch);
        rightButton.draw(batch);
        shootButton.draw(batch);
        jumpButton.draw(batch);
    }

    // ===================================================================================================================

    public void update(boolean checkTouch,int touchX, int touchY){
        leftButton.update(checkTouch, touchX, touchY);
        rightButton.update(checkTouch, touchX, touchY);
        jumpButton.update(checkTouch, touchX, touchY);
        shootButton.update(checkTouch,touchX,touchY);
    }

    // ===================================================================================================================

    public void dispose(){
        leftButton.dispose();
        rightButton.dispose();
        jumpButton.dispose();
        shootButton.dispose();

        musicBtn.dispose();
        pauseBtn.dispose();
    }

    // ========================================== IMAGE BUTTON =================================================================

    // A button consisting of an enabled and a disabled image. The user toggles the enabled/disabled state
    public static class ImageButton extends Group {

        private final Image enabledImage;
        private final Image disabledImage;

        private final Texture enabledTexture;
        private final Texture disabledTexture;

        public enum ButtonType { MUSIC, PAUSE }

        private final ButtonType buttonType;


        // --------------------------------------------------------------

        public ImageButton(Texture enabledTexture, Texture disabledTexture, ButtonType buttonType, Vector2 position) {
            Gdx.app.log("flow", "ImageButton");

            this.enabledTexture = enabledTexture;
            this.disabledTexture = disabledTexture;

            this.enabledImage = new Image(enabledTexture);
            this.disabledImage = new Image(disabledTexture);

            this.enabledImage.setSize(50, 50);
            this.disabledImage.setSize(50, 50);

            this.enabledImage.setVisible(true);
            this.disabledImage.setVisible(false);

            this.buttonType = buttonType;

            this.enabledImage.setPosition(position.x, position.y);
            this.disabledImage.setPosition(position.x, position.y);

            addActor(this.enabledImage);
            addActor(this.disabledImage);

            this.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Gdx.app.log("flow", "ImageButtonListener");

                    ImageButton imageButton = (ImageButton) event.getListenerActor();
                    imageButton.setControl(imageButton);
                    return true;
                }
            });
        }

        // --------------------------------------------------------------

        public void setControl(ImageButton imageButton) {
            Gdx.app.log("flow", "setControl");

            boolean enabled = enabledImage.isVisible();

            // First toggle the event before switching button display
            if(imageButton.buttonType == ImageButton.ButtonType.MUSIC) {
                MasterStateController.toggleMusic(enabled);
            }

            else if(imageButton.buttonType == ImageButton.ButtonType.PAUSE) {
                GameStateController.togglePauseGame(enabled);
            }

            // Switch the image display
            enabledImage.setVisible(!enabled);
            disabledImage.setVisible(enabled);
        }

        // --------------------------------------------------------------

        public void dispose(){
            Gdx.app.log("dispose", "UIControlsOverlayDispose");

            this.enabledTexture.dispose();
            this.disabledTexture.dispose();
        }
    }

    // ===================================================================================================================

    public Button getLeftButton() {
        return leftButton;
    }

    public Button getRightButton() {
        return rightButton;
    }

    public Button getJumpButton() {
        return jumpButton;
    }

    public Button getShootButton() {
        return shootButton;
    }
}
