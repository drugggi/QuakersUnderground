package fin.laakso.burlybugs;

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
