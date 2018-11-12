package fin.laakso.burlybugs;

import android.util.Log;

public class GameCamera {

    private GameObject objectToFollow;
    private int xOffset,yOffset;

    private int directionBonusX=0, directionBonusY = 0;

    public GameCamera(GameObject followThis,int xOffset, int yOffset) {
        objectToFollow = followThis;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void move(int xAmt, int yAmt) {
        xOffset += xAmt;
        yOffset += yAmt;
    }

    public void centerOnGameObject() {

        int DX = objectToFollow.getDX()*10;
        int DY = objectToFollow.getDY()*10;

        if (DX == 0) {
            directionBonusX = directionBonusX*11 / 12;
        }

        else if (DX < 0) {
            directionBonusX -= 4;
        }
        else {
            directionBonusX += 4;
        }

        if (DY == 0) {
            directionBonusY = directionBonusY *11 / 12;
        }
        else if (DY < 0) {
            directionBonusY -= 2;
        }
        else {
            directionBonusY += 2;
        }


        xOffset = objectToFollow.getX() - GamePanel.WIDTH / 2 + directionBonusX;
        yOffset = objectToFollow.getY() - GamePanel.HEIGHT / 2 + directionBonusY;
       // Log.d("offsets","x/y: " + xOffset + "/"+yOffset);

        if (yOffset + GamePanel.HEIGHT > 2400 ) {
            yOffset = 2400 - GamePanel.HEIGHT;
        }
        else if (yOffset < 0) {
            yOffset = 0;
        }

        if (xOffset < 0) {
            xOffset = 0;
        }
        else if (xOffset + GamePanel.WIDTH > 2400) {
            xOffset = 2400 - GamePanel.WIDTH;
        }

    }

    public int getxOffset() {
        return xOffset;
    }
    public int getyOffset() {
        return yOffset;
    }
    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }
    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

}
