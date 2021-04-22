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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.amusementpark.gui.AmusementPark;
import com.mygdx.amusementpark.gui.GameScreen;


public class MainMenuScreen implements Screen {
    private AmusementPark game;
    private final Stage stage;
    private OrthographicCamera camera;

    public MainMenuScreen(final AmusementPark game) {
        this.game = game;

        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        Texture backgroundTex = new Texture(Gdx.files.internal("back2.png"));
        TextureRegion backgroundTexReg = new TextureRegion(backgroundTex);

        Image background = new Image(backgroundTexReg);
        background.setSize(1600, 900);

        TextButton playButton = new TextButton("Play Game", skin);
        playButton.setSize(300, 100);
        //playButton.setPosition(stage.getWidth()/2 - playButton.getWidth()/2,stage.getHeight()/2);
        playButton.setPosition(275,100);
        playButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.setScreen(new GameScreen(game));
            }
        });

        TextButton exitButton = new TextButton("Exit Game", skin);
        exitButton.setSize(300, 100);
        //exitButton.setPosition(stage.getWidth()/2 - exitButton.getWidth()/2,stage.getHeight()/2 - exitButton.getHeight());
        exitButton.setPosition(625,100);
        exitButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                Gdx.app.exit();
                System.exit(0);
            }
        });

        stage.addActor(background);
        stage.addActor(playButton);
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
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        //Stage should controll input:
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}

