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

	private void configureHardCodedGui(){
		GuiPanelStyle  panelStyle = new GuiPanelStyle(guiAssetManager, "panel9p01");
		GuiButtonStyle buttonStyle = new GuiButtonStyle(guiAssetManager, "9pbtn1", "9pbtn1p");

		GuiPanel topPanel = new GuiPanel(panelStyle, new GuiRectangle("0,0,0,0", "0,0,0,100", "0,0,100,0", "0,2,0,0", GuiManager.PivotPoint.TOP_LEFT));
		guiManager.addTopLevelElement(topPanel);

		GuiPanel leftPanel = new GuiPanel(panelStyle, new GuiRectangle("0,0,0,0", "0,-2,0,100", "0,5,0,0", "0,-2,0,100", GuiManager.PivotPoint.TOP_LEFT));
		guiManager.addTopLevelElement(leftPanel);

		GuiPanel rightPanel = new GuiPanel(panelStyle, new GuiRectangle("0,0,100,0", "0,-2,0,100", "0,5,0,0", "0,-2,0,100", GuiManager.PivotPoint.TOP_RIGHT));
		guiManager.addTopLevelElement(rightPanel);


		GuiPanel mainPanel = new GuiPanel(panelStyle, new GuiRectangle("0,6,0,0", "0,-6,0,100", "0,11,0,0", "0,11,0,0", GuiManager.PivotPoint.TOP_LEFT));
		mainPanel.setMovable(true);

		mainPanel.addChild(new GuiButton(buttonStyle, "BALLE",     new GuiRectangle("0,0,0,0", "0,-0.2,0,100", "0,0,33,0", "0,1,0,0", GuiManager.PivotPoint.TOP_LEFT)));
		mainPanel.addChild(new GuiButton(buttonStyle, "FISK", 	  new GuiRectangle("0,0,33,0", "0,-0.2,0,100", "0,0,34,0", "0,1,0,0", GuiManager.PivotPoint.TOP_LEFT)));
        mainPanel.addChild(new GuiButton(buttonStyle, "KØDDEMANN", new GuiRectangle("0,0,67,0", "0,-0.2,0,100", "0,0,33,0", "0,1,0,0", GuiManager.PivotPoint.TOP_LEFT)));

		guiManager.addTopLevelElement(mainPanel);
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		guiManager = new GuiManager();
		Gdx.input.setInputProcessor(guiManager);
		guiAssetManager = new GuiAssetManager();
        GuiElement.mGuiAssetManager = guiAssetManager;
		guiAssetManager.getTexture("bajs");
		configureHardCodedGui();

        Gdx.app.log("GuiLib", "Loading XML");
        guiManager.loadElementsFromFile(GuiAssetManager.defaultAssetDirectory + "\\layout_xml\\test_layouts.xml", false);
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
