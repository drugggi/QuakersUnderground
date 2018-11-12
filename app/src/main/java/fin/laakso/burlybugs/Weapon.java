package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Weapon extends GameObject {

    protected int velocityX;
    protected int velocityY;

    int ammo;

    private Entity whoShotThis;
    
    Weapon() {
        ammo = 0;
    }

    public void shoot() {
        if (ammo > 0) {
            ammo--;
        }
    }

    public void update() {

    }

    public void draw(Canvas canvas) {

    }

    public void setEntity(Entity myShot) {
        whoShotThis = myShot;
    }

    public Entity whoShot() {
        return whoShotThis;
    }

    public boolean isActivated() {
        return true;
    }
}
