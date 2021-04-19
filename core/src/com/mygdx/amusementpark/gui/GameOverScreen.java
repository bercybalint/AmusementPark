package com.mygdx.amusementpark.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameOverScreen implements Screen {

    private AmusementPark game;
    private final Stage stage;
    private OrthographicCamera camera;
    private Label gameOver;

    public GameOverScreen(final AmusementPark game) {

        this.game = game;
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        gameOver = new Label("Game Over", skin);
        gameOver.setPosition(550, 700);
        //ez nem az igazi
        gameOver.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getWidth() / 12);

        TextButton newGameButton = new TextButton("New Game", skin);
        newGameButton.setSize(300, 100);
        newGameButton.setPosition(stage.getWidth()/2 - newGameButton.getWidth()/2,stage.getHeight()/2);
        //newGameButton.setPosition(275,100);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.setScreen(new GameScreen(game));
            }
        });

        TextButton exitButton = new TextButton("Exit Game", skin);
        exitButton.setSize(300, 100);
        exitButton.setPosition(stage.getWidth()/2 - exitButton.getWidth()/2,stage.getHeight()/2 - 150);
        //exitButton.setPosition(625,100);
        exitButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                Gdx.app.exit();
                System.exit(0);
            }
        });

        stage.addActor(gameOver);
        stage.addActor(newGameButton);
        stage.addActor(exitButton);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.135f, .206f, .235f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }
    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
