package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Weapon extends GameObject {

    protected int velocityX;
    protected int velocityY;

    int ammo;

    private Entity whoShotThis;

    public boolean hit;

    protected GameCamera camera;

    Weapon() {
        hit = false;
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

    public void setVelocity(int velX, int velY) {
        velocityX = velX;
        velocityY = velY;
    }

    public boolean isActivated() {
        return true;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean h) {
        hit = h;
    }

    public void collisionEntities(Weapon weapon, Entity ent, ArrayList<WeaponEffect> effects) {

    }

    public void collisionTiles(Weapon weapon,World gameWorld,ArrayList<WeaponEffect> effects) {

    }
}
