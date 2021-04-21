package com.mygdx.amusementpark.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import main.java.com.mygdx.amusementpark.gui.AmusementPark;

public class DesktopLauncher
{
	public static void main (String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = true;
		config.width=1200;
		config.height=900;
		new LwjglApplication(new AmusementPark(), config);
	}
}
