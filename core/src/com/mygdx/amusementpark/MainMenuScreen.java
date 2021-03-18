package com.mygdx.amusementpark;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import sun.tools.jconsole.Tab;


public class MainMenuScreen implements Screen {

    private AmusementPark game;
    private final Stage stage;
    private OrthographicCamera camera;

    public MainMenuScreen(final AmusementPark game) {
        this.game = game;

        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        TextButton playButton = new TextButton("Play Game", skin);
        playButton.setSize(300, 100);
        playButton.setPosition(stage.getWidth()/2 - playButton.getWidth()/2,stage.getHeight()/2);
        playButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.setScreen(new GameScreen(game));
            }
        });

        TextButton exitButton = new TextButton("Exit Game", skin);
        exitButton.setSize(300, 100);
        exitButton.setPosition(stage.getWidth()/2 - exitButton.getWidth()/2,stage.getHeight()/2 - exitButton.getHeight());
        exitButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                Gdx.app.exit();
                System.exit(0);
            }
        });

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

