package com.mygdx.amusementpark.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Textures
{
    public Texture wall_texture;
    public Texture grass_texture;
    public Texture gate_texture;
    public Texture fence_texture;
    public Texture roller_texture;
    public Texture castle_texture;
    public Texture cleanerHouse_texture;
    public Texture mechanicHouse_texture;
    public Texture bush_texture;
    public Texture water_texture;
    public Texture hamburger_texture;
    public Texture trashcan_texture;
    public Texture trash_texture;
    public Texture cleaner_texture;
    public Texture mechanic_texture;


     /**
      * d Különböző út textúrák beállítása, szomszédoktól függően.
     */
    public Texture road_down_to_up;
    public Texture road_down_to_left;
    public Texture road_down_to_right;
    public Texture road_left_to_right;
    public Texture road_top_to_right;
    public Texture road_top_to_left;
    public Texture road_threeway_to_up;
    public Texture road_threeway_to_down;
    public Texture road_threeway_to_right;
    public Texture road_threeway_to_left;
    public Texture road_from_all;
    public Texture guest_texture;
    public TextureRegion textureRegionRoad;
    public TextureRegionDrawable textureRegionDrawableRoad;
    public TextureRegion textureRegionGameRoller;
    public TextureRegionDrawable textureRegionDrawableGameRoller;
    public TextureRegion textureRegionGameCastle;
    public TextureRegionDrawable textureRegionDrawableGameCastle;
    public TextureRegion textureRegionPlantBush;
    public TextureRegionDrawable textureRegionDrawablePlantBush;
    public TextureRegion textureRegionCleaner;
    public TextureRegionDrawable textureRegionDrawableCleaner;
    public TextureRegion textureRegionMechanic;
    public TextureRegionDrawable textureRegionDrawableMechanic;
    public TextureRegion textureRegionTrash;
    public TextureRegionDrawable textureRegionDrawableTrash;
    public TextureRegion textureRegionHamburger;
    public TextureRegionDrawable textureRegionDrawableHamburger;
    public TextureRegion textureRegionWater;
    public TextureRegionDrawable textureRegionDrawableWater;
    public Texture happy_texture;
    public Texture annoyed_texture;
    public Texture angry_texture;

    public Textures()
    {
        guest_texture = new Texture(Gdx.files.internal("people.png"));
        wall_texture = new Texture(Gdx.files.internal("tile.png"));
        grass_texture = new Texture(Gdx.files.internal("grass.png"));
        gate_texture = new Texture(Gdx.files.internal("gate.png"));
        fence_texture = new Texture(Gdx.files.internal("fence.png"));

        road_down_to_up = new Texture(Gdx.files.internal("road_down.png"));
        road_down_to_left = new Texture(Gdx.files.internal("road_left_to_down.png"));
        road_down_to_right = new Texture(Gdx.files.internal("road_down_to_right.png"));
        road_left_to_right = new Texture(Gdx.files.internal("road_right.png"));
        road_top_to_right = new Texture(Gdx.files.internal("road_turn.png"));
        road_top_to_left = new Texture(Gdx.files.internal("road_down_to_left.png"));
        road_threeway_to_up = new Texture(Gdx.files.internal("road_three_up.png"));
        road_threeway_to_down = new Texture(Gdx.files.internal("road_three_down.png"));
        road_threeway_to_right = new Texture(Gdx.files.internal("road_three_right.png"));
        road_threeway_to_left = new Texture(Gdx.files.internal("road_three_left.png"));
        road_from_all = new Texture(Gdx.files.internal("road_inter.png"));


        roller_texture = new Texture(Gdx.files.internal("roller.png"));
        castle_texture = new Texture(Gdx.files.internal("castle.png"));
        bush_texture = new Texture(Gdx.files.internal("bush.png"));
        hamburger_texture = new Texture(Gdx.files.internal("hamburger.png"));
        water_texture = new Texture(Gdx.files.internal("water.png"));
        trash_texture = new Texture(Gdx.files.internal("trash.png"));
        trashcan_texture = new Texture(Gdx.files.internal("trashcan.png"));
        cleaner_texture = new Texture(Gdx.files.internal("cleaner.png"));
        mechanic_texture = new Texture(Gdx.files.internal("mechanic.png"));


        cleanerHouse_texture = new Texture(Gdx.files.internal("cleanerhouse.png"));
        mechanicHouse_texture = new Texture(Gdx.files.internal("mechanichouse.png"));

        happy_texture = new Texture(Gdx.files.internal("guestHappy.png"));
        annoyed_texture = new Texture(Gdx.files.internal("guestAnnoyed.png"));
        angry_texture = new Texture(Gdx.files.internal("guestAngry.png"));

        textureRegionRoad = new TextureRegion(road_down_to_up);
        textureRegionDrawableRoad = new TextureRegionDrawable(textureRegionRoad);

        textureRegionGameRoller = new TextureRegion(roller_texture);
        textureRegionDrawableGameRoller = new TextureRegionDrawable(textureRegionGameRoller);
        textureRegionGameCastle = new TextureRegion(castle_texture);
        textureRegionDrawableGameCastle = new TextureRegionDrawable(textureRegionGameCastle);

        textureRegionPlantBush = new TextureRegion(bush_texture);
        textureRegionDrawablePlantBush = new TextureRegionDrawable(textureRegionPlantBush);

        textureRegionHamburger = new TextureRegion(hamburger_texture);
        textureRegionDrawableHamburger = new TextureRegionDrawable(textureRegionHamburger);
        textureRegionWater = new TextureRegion(water_texture);
        textureRegionDrawableWater = new TextureRegionDrawable(textureRegionWater);

        textureRegionTrash = new TextureRegion(trashcan_texture);
        textureRegionDrawableTrash = new TextureRegionDrawable(textureRegionTrash);

        textureRegionCleaner = new TextureRegion(cleanerHouse_texture);
        textureRegionDrawableCleaner = new TextureRegionDrawable(textureRegionCleaner);
        textureRegionMechanic = new TextureRegion(mechanicHouse_texture);
        textureRegionDrawableMechanic = new TextureRegionDrawable(textureRegionMechanic);

    }
}
