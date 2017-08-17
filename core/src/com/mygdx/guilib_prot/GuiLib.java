package com.mygdx.guilib_prot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.guilib_prot.guilib.*;

public class GuiLib extends ApplicationAdapter {
	SpriteBatch 	batch;
	GuiManager 		guiManager;
	GuiAssetManager guiAssetManager;

	public GuiLib(String defaultAssetDir, float mmToPixelRatio){
		super();
		GuiAssetManager.defaultAssetDirectory = defaultAssetDir;
		GuiAssetManager.imageNotFoundImage = defaultAssetDir + "_not_found.png";
		GuiManager.mMilliMeterToPixelRatio = mmToPixelRatio;
	}

	private void configureGui(){
		Gdx.app.log("GuiLib", "Loading XML");
		guiManager.loadElementsFromFile(GuiAssetManager.defaultAssetDirectory + "\\layout_xml\\test_layouts.xml", false);
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		guiManager = new GuiManager();
		Gdx.input.setInputProcessor(guiManager);
		guiAssetManager = new GuiAssetManager();
        GuiElement.mGuiAssetManager = guiAssetManager;
		guiAssetManager.getTexture("bajs");
		configureGui();

         //GuiXmlParser.loadElementsFromFile(GuiAssetManager.defaultAssetDirectory + "\\layout_xml\\test_layouts.xml");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		guiManager.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		guiManager.resize(width, height);
		batch = new SpriteBatch();
    }


}
