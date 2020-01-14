package com.kroy.game;

import java.util.HashMap;

public class Tooltip {

    private String name;
    private int x, y;
    private boolean render;
    private int iconSize;
    private int fontSpacing;
    private HashMap<String, Object> values;


    public Tooltip(String characterName,int xPosition, int yPosition, int sizeOfTile, int sizeOfFont) {

        name = characterName;
        x = xPosition;
        y = yPosition;
        iconSize = sizeOfTile;
        fontSpacing = sizeOfFont;
        render = false;
        values = new HashMap<String, Object>();
    }


    public void addValue(String iconRoot, Object value) {
        if (!(values.containsKey(iconRoot))) {
            values.put(iconRoot, value);
        } else {
            //icon is already being used in this tool tip

        }
    }


    public void updateValue(String iconRoot, Object value) {
        if (values.containsKey(iconRoot)) {
            values.put(iconRoot, value);
        } else {
            //icon is not used in values for tooltip

        }
    }


    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    public HashMap<String, Object> getValues() {
        return values;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void updateValue(HashMap<String, Object> values) {
        this.values = values;
    }

    public int getIconSize() {
        return iconSize;
    }

    public int getFontSpacing() {
        return fontSpacing;
    }

    public String getName() {
        return name;
    }
}
