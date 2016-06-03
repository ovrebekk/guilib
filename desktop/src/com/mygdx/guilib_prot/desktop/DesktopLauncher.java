package com.mygdx.guilib_prot.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.guilib_prot.GuiLib;

public class DesktopLauncher {
	public static void main (String[] arg) {
		String defaultAssetDirectory = "D:\\prosjekt\\java\\guilib_prototype\\android\\assets\\";
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
		config.height = 1200;
		config.resizable = true;
		new LwjglApplication(new GuiLib(defaultAssetDirectory, 42.0f), config);
	}
}
