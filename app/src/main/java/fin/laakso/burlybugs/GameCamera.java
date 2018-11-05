package fin.laakso.burlybugs;

import android.util.Log;

public class GameCamera {

    private GameObject objectToFollow;
    private int xOffset,yOffset;

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
        xOffset = objectToFollow.getX() - GamePanel.WIDTH / 2;
        yOffset = objectToFollow.getY() - GamePanel.HEIGHT / 2;
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
