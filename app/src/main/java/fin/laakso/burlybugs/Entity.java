package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Entity extends GameObject {
    protected int health;
    protected boolean jumping;

    protected Animation animation = new Animation();

    protected Backpack backpack;
    protected World gameWorld;

    Entity() {
        health = 300;
        backpack = new Backpack();
    }

    public boolean isDead() {
        return (health <= 0);
    }

    public Weapon shoot() {
        return new Weapon();
    }

    public void deathAnimation(ArrayList<PhysicalEffect> pe) {

    }

    public void shoot(ArrayList<Weapon> shootingWeapons,int towardsX, int towradsY) {

        long missileElapsed = (System.nanoTime() - backpack.getShotStartTime())/1000000;

        if (missileElapsed > (500)) {

            towardsX += gameWorld.getCamera().getxOffset() ;
            towradsY += gameWorld.getCamera().getyOffset() ;

            float differenceX =  (x - towardsX);
            float differenceY = (y- towradsY);

            double angle = atan(differenceY/differenceX);

            int velX = (int) (50 * cos(angle));
            int velY = (int) (50 * sin(angle));

            if (differenceX < 0) {
                velX = -1*velX;
                velY = -1*velY;
            }
            angle = Math.toDegrees(angle);

            if (differenceX < 0) {
                angle += 180;
            }
            if (differenceY < 0) {

            }

            Missile missileBM = new Missile(gameWorld.getCamera(),Assets.missile,x+16,y+16,45,15,13,(float)angle);
            missileBM.setVelocity(-velX/2,-velY/2);

            shootingWeapons.add(missileBM) ;
            backpack.setShotStartTime(System.nanoTime() );
        }

    }

    public void giveItem(Item pickedItem) {
        health += 150;
    }

    public void update() {

    }

    public void draw(Canvas canvas) {

    }

    public void setHealth(int newHealth) {
        health = newHealth;
    }

    public int getHealth() {
        return health;
    }

    public void increaseHealth(int increment) {
        health += increment;
    }

    public void setKnockback(int increaseX,int increaseY, int increaseDX,int increaseDY) {
        x += increaseX;
        y += increaseY;
        dx += increaseDX;
        dy += increaseDY;
    }


    public void setKnockBack(int knockX, int knockY) {

        int differenceX = getCenterX() - knockX;
        int differenceY = getCenterY() - knockY;

        health -= 100;
        if (differenceX > 0) {
            x += 10;
            dx = 20;
            health += differenceX;
        } else {
            x += -10;
            dx = -20;
            health -= differenceX;
        }

        // explosion over head, knockback downwards
        if (differenceY > 0) {
            y += 50;
            dy += 20;
            // this.jumping = true;
            health += differenceY;

        } // explosion under enemy, knockback upwards
        else {
            y += -50;
            dy += -20;
            // jumping = true;
            health -= differenceY;
        }

        // Entity dead, respawns 1200,100
        if (health <= 0) {
            x = 1200;
            y = 100;
            health = 300;
        }
    }

    public void setJumping(boolean j ) {
        jumping = j;
    }

    protected void checkLegTileCollision() {
        int tileY = (y+height) / Tile.TILE_HEIGHT;

        Tile leftLegTile = gameWorld.getTile((x) / Tile.TILE_WIDTH, (y+height) / Tile.TILE_HEIGHT);
        Tile rightLegTile = gameWorld.getTile((x+width)/Tile.TILE_WIDTH, (y+height) / Tile.TILE_HEIGHT);
        // Log.d("Tile", "x/y: " + (x+width)/Tile.TILE_WIDTH + "/" + (y+height) / Tile.TILE_HEIGHT);

        if (!leftLegTile.isDestructible() || !rightLegTile.isDestructible() ) {
            dy = 0;
            //Log.d("tile","x/y: " + (x+width)+ "/" + (y+height) );
            y = tileY * Tile.TILE_HEIGHT - height;
            jumping = false;
        }
        else if ( leftLegTile.isSolid() || rightLegTile.isSolid()) {
            dy = 0;
            jumping = false;
            // y =
        } else {
            jumping = true;
        }
    }
}
