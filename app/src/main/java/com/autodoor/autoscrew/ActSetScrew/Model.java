package com.autodoor.autoscrew.ActSetScrew;

/**
 * Created by yangyi on 2015/11/4.
 */
public class Model {

    private int id;
    private boolean selected;

    private int x;
    private int y;
    private int z;

    private String direction="左";
    private boolean selectedDirection;

    private boolean available=true;

    public Model(int id,int x,int y ,int z,String direction,boolean available) {
        this.setId(id);
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setDirection(direction);
        this.available = available;
        this.selected = false;//是否被选中
    }



    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isSelectedDirection() {
        return selectedDirection;
    }

    public void setSelectedDirection(boolean selectedDirection) {
        this.selectedDirection = selectedDirection;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
