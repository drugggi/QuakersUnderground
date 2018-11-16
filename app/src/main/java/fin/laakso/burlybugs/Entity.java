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

        CorpseEffect newCorpse = new CorpseEffect(gameWorld,Assets.torsoanim,x,y,32,32,7);
        newCorpse.setDX(3*dx+GamePanel.rng.nextInt(5)-2);
        newCorpse.setDY(3*dy+GamePanel.rng.nextInt(5)-2);
        pe.add(newCorpse);

        newCorpse = new CorpseEffect(gameWorld,Assets.corpselegsanim,x,y,32,32,6);
        newCorpse.setDX(3*dx+GamePanel.rng.nextInt(5)-2);
        newCorpse.setDY(3*dy+GamePanel.rng.nextInt(5)-2);
        pe.add(newCorpse);

        newCorpse = new CorpseEffect(gameWorld,Assets.corpsehead,x,y,16,16,1);
        newCorpse.setDX(5*dx+GamePanel.rng.nextInt(5)-2);
        newCorpse.setDY(5*dy+GamePanel.rng.nextInt(5)-2);
        pe.add(newCorpse);

        newCorpse = new CorpseEffect(gameWorld,Assets.corpsehand,x,y,32,32,1);
        newCorpse.setDX(4*dx+GamePanel.rng.nextInt(5)-2);
        newCorpse.setDY(4*dy+GamePanel.rng.nextInt(5)-2);
        pe.add(newCorpse);
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

/*

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
*/

    public void setJumping(boolean j ) {
        jumping = j;
    }

    protected void checkHeadTileCollision() {
        int tileY = (y) / Tile.TILE_HEIGHT;

        Tile leftHeadTile = gameWorld.getTile((x) / Tile.TILE_WIDTH, (y) / Tile.TILE_HEIGHT);
        Tile rightHeadTile = gameWorld.getTile((x+width)/Tile.TILE_WIDTH, (y) / Tile.TILE_HEIGHT);

        if (!leftHeadTile.isDestructible() || !rightHeadTile.isDestructible() ) {
            dy = 0;
            //Log.d("tile","x/y: " + (x+width)+ "/" + (y+height) );
            y = tileY * Tile.TILE_HEIGHT+Tile.TILE_HEIGHT+1;
            // jumping = false;
        }
        else if ( leftHeadTile.isSolid() || rightHeadTile.isSolid()) {
            dy = 0;
            //jumping = false;
            // y =
        }
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

    protected void checkRightTileCollision() {
        int tileX = (x+width) /Tile.TILE_WIDTH;

        Tile rightHeadTile = gameWorld.getTile((x+width)/Tile.TILE_WIDTH,y/Tile.TILE_HEIGHT);
        Tile rightMiddleTile = gameWorld.getTile((x+width)/Tile.TILE_WIDTH,(y+height/2)/Tile.TILE_HEIGHT);
        Tile rightLegTile = gameWorld.getTile((x+width)/Tile.TILE_WIDTH,(y+height-1)/Tile.TILE_HEIGHT);

        if (!rightHeadTile.isDestructible() || !rightLegTile.isDestructible() || !rightMiddleTile.isDestructible() ) {
           // dx =0;
            //Log.d("tile","x/y: " + (x+width)+ "/" + (y+height) );
            x = tileX * Tile.TILE_WIDTH - height-1;

            // Checking leg tile, if there is nothing continue jumping = true
            checkLegTileCollision();
            //jumping = false;
        }
/*        else if ( !rightHeadTile.isSolid() || !rightLegTile.isSolid() || !rightMiddleTile.isSolid() ) {
            dx = 0;
            jumping = false;
            // y =
        } else {
            jumping = true;
        }*/
    }

    protected void checkLeftTileCollision() {
        int tileX = (x) /Tile.TILE_WIDTH;

        Tile rightHeadTile = gameWorld.getTile((x)/Tile.TILE_WIDTH,y/Tile.TILE_HEIGHT);
        Tile rightMiddleTile = gameWorld.getTile((x)/Tile.TILE_WIDTH,(y+height/2)/Tile.TILE_HEIGHT);
        Tile rightLegTile = gameWorld.getTile((x)/Tile.TILE_WIDTH,(y+height-1)/Tile.TILE_HEIGHT);

        if (!rightHeadTile.isDestructible() || !rightLegTile.isDestructible() || !rightMiddleTile.isDestructible() ) {
           // dx = 0;
            //Log.d("tile","x/y: " + (x+width)+ "/" + (y+height) );
            x = tileX * Tile.TILE_WIDTH +1+Tile.TILE_WIDTH;

            // Checking leg tile, if there is nothing continue jumping = true
            checkLegTileCollision();
            //jumping = false;
        }
/*        else if ( !rightHeadTile.isSolid() || !rightLegTile.isSolid() || !rightMiddleTile.isSolid() ) {
            dx = 0;
            jumping = false;
            // y =
        } else {
            jumping = true;
        }*/
    }
}
