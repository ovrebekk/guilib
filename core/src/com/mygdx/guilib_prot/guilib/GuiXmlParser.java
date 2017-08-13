package com.mygdx.guilib_prot.guilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiXmlParser {

    public static List<GuiElement> loadElementsFromFile(String fileName) {
        List<GuiElement> elementsFromFile = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            XmlReader xmlReader = new XmlReader();
            try {
                XmlReader.Element xmlRootElement = xmlReader.parse(fileInputStream);
                if(xmlRootElement.getName().equals("guilib_root")){
                    if(xmlRootElement.getChildCount() > 0) {
                        elementsFromFile = new ArrayList<GuiElement>();
                        for (int i = 0; i < xmlRootElement.getChildCount(); i++) {
                            elementsFromFile.add(decodeGuiElements(xmlRootElement.getChild(i), xmlRootElement, 0));
                        }
                    }
                    else {
                        Gdx.app.error("GuiXmlParser", "No elements in XML file");
                    }
                }
                else {
                    Gdx.app.error("GuiXmlParser", "Invalid XML file");
                }

            }catch(IOException e) {
                Gdx.app.error("GuiXmlParser", "Error in XML file");
            }
        }catch(FileNotFoundException e) {
            Gdx.app.error("GuiXmlParser", "XML file not found. Incorrect path.");
        }
        return elementsFromFile;
    }

    private static GuiElement decodeGuiElements(XmlReader.Element xmlElement, XmlReader.Element xmlRootElement, int tabSpaces) {
        GuiElement newGuiElement = null;
        boolean validElement = true;
        if(xmlElement.getName().equals("guielement")) {
            newGuiElement = new GuiElement(xmlElement, xmlRootElement, tabSpaces);
        }
        else if(xmlElement.getName().equals("drawable")) {
            newGuiElement = new GuiDrawable(xmlElement, xmlRootElement, tabSpaces);
        }
        else if(xmlElement.getName().equals("label")) {
            newGuiElement = new GuiLabel(xmlElement, xmlRootElement, tabSpaces);
        }
        else if(xmlElement.getName().equals("button")) {
            newGuiElement = new GuiButton(xmlElement, xmlRootElement, tabSpaces);
        }
        else {
            validElement = false;
        }

        if(validElement) {
            // If there are children, apply the same process recursively
            for (int i = 0; i < xmlElement.getChildCount(); i++) {
                GuiElement childElement = decodeGuiElements(xmlElement.getChild(i), xmlRootElement, tabSpaces + 1);
                if (childElement != null) {
                    newGuiElement.addChild(childElement);
                }
            }
        }
        else {
            Gdx.app.error("GuiElement", "Invalid element in XML: " + xmlElement.getName());
        }
        return newGuiElement;
    }

    public static Color stringToColor(String colorString) {
        Color newColor;
        if(colorString.split(",").length == 3){
            int r = Integer.valueOf(colorString.split(",")[0]) & 0xFF;
            int g = Integer.valueOf(colorString.split(",")[1]) & 0xFF;
            int b = Integer.valueOf(colorString.split(",")[2]) & 0xFF;
            newColor = new Color(r << 24 | g << 16 | b << 8 | 0xFF);
            return newColor;
        }
        else if(colorString.split(",").length == 4){
            int r = Integer.valueOf(colorString.split(",")[0]) & 0xFF;
            int g = Integer.valueOf(colorString.split(",")[1]) & 0xFF;
            int b = Integer.valueOf(colorString.split(",")[2]) & 0xFF;
            int a = Integer.valueOf(colorString.split(",")[3]) & 0xFF;
            newColor = new Color(r << 24 | g << 16 | b << 8 | a);
            return newColor;
        }
        return null;
    }
}
