package fin.laakso.burlybugs;

import android.graphics.Rect;

public abstract class GameObject {
    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
    protected int width;
    protected int height;

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDX() {
        return dx;
    }

    public int getDY() {
        return dy;
    }

    public void setDX(int newDX) {
        dx=newDX;
    }
    public void setDY(int newDY) {
        dy = newDY;
    }


    public int getHeight() {
        return height;

    }
    public int getCenterX() {
        return (x+width)/2;
    }

    public int getCenterY() {
        return (y+height)/2;
    }

    public int getWidth() {
        return width;
    }

    public Rect getRectangle() {
        return new Rect(x,y,x+width,y+height);
    }


}
