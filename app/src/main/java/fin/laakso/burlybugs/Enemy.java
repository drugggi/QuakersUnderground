package fin.laakso.burlybugs;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Enemy extends Entity {

    private Bitmap spritesheet;

    private boolean parachute;
    private boolean shootingTime;

    private GameCamera camera;

    private int updateAmount;
    Random rng;
    private int nextDecision;


    public Enemy(Bitmap res, int w, int h, int numFrames) {

        super.x = 1200;
        super.y = GamePanel.HEIGHT/3;
        // super.dy = 0;
        super.height = h;
        super.width = w;

        updateAmount = 0;
        rng = new Random();

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;
        //this.camera = camera;


        int j = 0;
        for (int i = image.length-1; i >=0; i--) {

            image[i] = Bitmap.createBitmap(spritesheet,j*width,0,width,height);
            j++;

        }
        nextDecision = 60;

        animation.setFrames(image);
        animation.setDelay(100);

        shootingTime = false;

        super.health = 100;

    }

    public boolean isShootingTime() {
        return shootingTime;
    }

    public void setShootingTime(boolean st) {
        shootingTime = st;
    }

    public void setWorldObject(World world) {
        gameWorld = world;
        camera = world.getCamera();
    }

    public void setDirection(int differenceX,int differenceY) {


        if (!jumping) {
            dy = differenceY / 15;
            dx = differenceX / 30;
            jumping = true;

            if ( dx > 15) {
                dx = 15;
            }
            else if (dx < -10 ){
                dx = -10;
            }

        }

    }

    private void makeIntelligentDecision() {

        if (updateAmount % nextDecision == 0) {
            nextDecision = 30*rng.nextInt(3)+30;
            // Log.d("NEXT DES",""+nextDecision);
            int directX = rng.nextInt(1200)-600;
            int directY = -rng.nextInt(600)-100;
            setDirection(directX,directY);

        }

    }

    @Override
    public void shoot(ArrayList<Weapon> shootingWeapons, int towardsX, int towradsY) {
        long missileElapsed2 = (System.nanoTime() - backpack.getShotStartTime() )/1000000;
        if (missileElapsed2 < 1000) {return; }

        float differenceX = (x - towardsX);
        float differenceY = (y - towradsY);

        // Not shooting if player is not enemys "screen"
        if (differenceX > 640 || differenceY > 360 || differenceX < -640 ||differenceY < -360) {return; }

        double angle = atan(differenceY/differenceX);
        angle = rng.nextGaussian()*0.2+angle;

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

        Missile newMissile = new Missile(camera,Assets.missile,x+16,y+16,45,15,13,(float)angle);
        backpack.setShotStartTime(System.nanoTime() );

        newMissile.setVelocity(-velX/4,-velY/4);
        newMissile.setEntity(this);

        shootingWeapons.add(newMissile);
    }

    public void update() {
        animation.update();
        updateAmount++;
         makeIntelligentDecision();

        //dx = dx *  11 / 12;
        x += dx ;
        if (jumping) {
            if (parachute) {
                dy = 1;
            }
            else {

                if (dy > 15) {
                    dy = 15;
                }
                else {
                    dy = dy - GamePanel.GRAVITY;
                }
            }
        }
        else {
            dy = 0;
            jumping = false;
            parachute = false;
        }

        if (y > 0 && y <= gameWorld.getWorldHeight() - super.height && x > 0 && x < gameWorld.getWorldWidth() ) {
            y += dy;
        }

        // player cant be left of the screen
        if (x < 0) {
            x = 1;
            dx = 0;

        } // over the screen
        if (y < 0) {
            y = 1;
            dy = 0;
        } // right on the screen
        if ( x > gameWorld.getWorldWidth() -super.width ) {
            x = gameWorld.getWorldWidth() -super.width - 1;
            dx = 0;

        }if (y >= gameWorld.getWorldHeight() - super.height) {
            //  Log.d("h","" + gameWorld.getWorldHeight() ) ;
            y = gameWorld.getWorldHeight() - super.height;
            dy = 0;

            jumping = false;
        }


        if (dy >= 0) {

            checkLegTileCollision();
            /*

            Tile legTile = gameWorld.getTile((x) / Tile.TILE_WIDTH, (y+height) / Tile.TILE_HEIGHT);
            // Log.d("Tile", "solid: " + legTile.isSolid() + "  " + legTile.toString());

            if ( legTile.isSolid() ) {

                dy = 0;
                jumping = false;
                // y =
            } else {
                jumping = true;
            }
*/

        }

        else {
            Tile headTile = gameWorld.getTile((x) / Tile.TILE_WIDTH, (y) / Tile.TILE_HEIGHT);
            // Log.d("Tile", "solid: " + headTile.isSolid() + "  " + headTile.toString());
            if ( headTile.isSolid() ) {
                dy = 0;
                // jumping = false;
                // y =
            }
        }

    }

    @Override
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

    public void draw(Canvas canvas) {

        canvas.drawBitmap(animation.getImage() , x -camera.getxOffset(), y - camera.getyOffset(),null);

    }



}
