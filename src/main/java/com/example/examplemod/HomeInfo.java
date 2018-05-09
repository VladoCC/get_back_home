package com.example.examplemod;

/**
 * Created by Voyager on 06.05.2018.
 */
public class HomeInfo {
    int x;
    int y;
    int z;
    int dim;
    boolean created = false;

    public void setHome(int x, int y, int z, int dim){
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
        created = true;
    }

    public void setHome(int[] home){
        setHome(home[0], home[1], home[2], home[3]);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getDim() {
        return dim;
    }

    public int[] getHome(){
        int[] home = {x, y, z, dim};
        return home;
    }

    public boolean isCreated() {
        return created;
    }

    public void removeHome(){
        created = false;
    }
}
