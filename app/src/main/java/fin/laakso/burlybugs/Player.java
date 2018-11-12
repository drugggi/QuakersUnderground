package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;
import static java.lang.Math.tanh;

public class Player extends Entity {
    private Bitmap spritesheet;

    private boolean moving;
    private boolean playing;

    private boolean parachute;
    private boolean anchor;

    private GameCamera camera;
    // private World gameWorld;

    private int updateAmount;
    private WeaponPanel weaponPanel;

    public Player(Bitmap res, int w, int h, int numFrames) {
        updateAmount = 0;

        super.x = 1000;
        super.y = GamePanel.HEIGHT/2;
        // super.dy = 0;
        super.height = h;
        super.width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        int j = 0;
        for (int i = image.length-1; i >=0; i--) {
            image[i] = Bitmap.createBitmap(spritesheet,j*width,0,width,height);
            j++;
        }

        animation.setFrames(image);
        animation.setDelay(100);

        super.backpack.setBackpackAmmo(new int[] {25,4});

    }

    public void setWorldObject(World world) {
        gameWorld = world;
    }

    public void setMoving(boolean b) {
        this.moving = b;
    }


    public void setCamera(GameCamera camera) {
        this.camera = camera;
    }

    public void setAnchor(boolean a ) {
        anchor = a;
    }
    public boolean isAnchor() {
        return anchor;
    }

    public void setParachute(boolean p) {
        parachute = p;
    }
    public boolean isParachute() {
        return parachute;
    }

    @Override
    public void shoot(ArrayList<Weapon> shootingWeapons, int towardsX, int towradsY) {

        long missileElapsed2 = (System.nanoTime() - backpack.getShotStartTime()) / 1000000;
        if (missileElapsed2 < 500) {
            return;
        }

        towardsX = towardsX + camera.getxOffset();
        towradsY = towradsY + camera.getyOffset();

        float differenceX = (x - towardsX);
        float differenceY = (y - towradsY);

        double angle = atan(differenceY / differenceX);

        int velX = (int) (50 * cos(angle));
        int velY = (int) (50 * sin(angle));

        if (differenceX < 0) {
            velX = -1 * velX;
            velY = -1 * velY;
        }
        angle = Math.toDegrees(angle);

        if (differenceX < 0) {
            angle += 180;

        }
        if (differenceY < 0) {

        }
        Missile newMissile = new Missile(camera, Assets.missile, x + 16, y + 16, 45, 15, 13, (float) angle);
        backpack.setShotStartTime(System.nanoTime());

        newMissile.setVelocity(-velX / 4, -velY / 4);
        newMissile.setEntity(this);

        shootingWeapons.add(newMissile);

    }


    public void keepDirection(float rawX,float rawY) {
        int directionX = (int ) rawX +camera.getxOffset() ;
        int directionY = (int) rawY + camera.getyOffset() ;
        // Log.d("values","x/y: "+ x + "/" + y + "   Dir: " +directionX + "/" + directionY);
        int differenceX = - (x - directionX);
        int differenceY = - (y - directionY);

        if (differenceX < 0) {
                dx--;
            } else {
                dx++;
        }

        if ( dx > 15) {
            dx = 15;
        }
        else if (dx < -15 ){
            dx = -15;
        }


    }

    public void setDirection(float rawX,float rawY) {

        int directionX = (int ) rawX +camera.getxOffset() ;
        int directionY = (int) rawY + camera.getyOffset() ;

        int differenceX = - (x - directionX);
        int differenceY = - (y - directionY);

        if (!moving && !jumping) {
            dx = differenceX / 20;
            moving = true;
        }

        if ( dx > 15) {
            dx = 15;
        }
        else if (dx < -15 ){
            dx = -15;
        }

        if (!jumping && differenceY < -20) {
            jumping = true;
            dy = differenceY / 15 ;
        }

    }

    public void update() {
        updateAmount++;

        animation.update();

        if (!moving && !parachute) {

             dx = dx *  11 / 12;
            // dx = dx / 2;
        }
        x += dx ;
        if (jumping) {
            if (parachute) {
                dy = 1;
            }
            else {
                dy = dy - GamePanel.GRAVITY;
            }
        }
        else {
            dy = 0;
            jumping = false;
            parachute = false;
        }


       //  x += dx ;
        if (y > 0 && y <= gameWorld.getWorldHeight() - super.height && x > 0 && x < gameWorld.getWorldWidth() ) {
            y += dy;
        }

        // player cant be left of the screen
        if (x < 0) {
            x = 1;
            dx = 0;
            moving = false;
        } // over the screen
        if (y < 0) {
            y = 1;
            dy = 0;
        } // right on the screen
        if ( x > gameWorld.getWorldWidth() -super.width ) {
            x = gameWorld.getWorldWidth() -super.width - 1;
            dx = 0;
            moving = false;
        }
        //Log.d("player DRAW","x/y: " + x +"/"+y + "   dx/dy: " + dx + "/" + dy);
        // Log.d("h","" + gameWorld.getWorldHeight() ) ;
        // down the screen
        if (y >= gameWorld.getWorldHeight() - super.height) {
           //  Log.d("h","" + gameWorld.getWorldHeight() ) ;
             y = gameWorld.getWorldHeight() - super.height;
            dy = 0;

            jumping = false;
        }


        if (dx == 0 && !jumping) {
            moving = false;
            animation.startAnimation(false);
            animation.setFrames(4);
        }
        else{
            animation.startAnimation(true);
        }

        //Collision detection
        if (updateAmount % 150 == 0) {
            Log.d("updateAmount",""+updateAmount);
            Log.d("player POSITION","x/y: " + x +"/"+y + "   dx/dy: " + dx + "/" + dy);
            Log.d("offsets","xOFF/yOFF: " + camera.getxOffset() +"/"+camera.getyOffset());
            Tile pointedTile = gameWorld.getTile(x/Tile.TILE_WIDTH,y/Tile.TILE_HEIGHT);

        }
        if (dy >= 0) {

            checkLegTileCollision();
/*
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
            }*/

        }

        else {
            Tile headTile = gameWorld.getTile((x) / Tile.TILE_WIDTH, (y) / Tile.TILE_HEIGHT);
            // Log.d("Tile", "solid: " + headTile.isSolid() + "  " + headTile.toString());
            if ( headTile.isSolid() ) {
                dy = 0;

            }
        }

    }

    public void draw(Canvas canvas) {

        canvas.drawBitmap(animation.getImage() , x -camera.getxOffset(), y - camera.getyOffset(),null);
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean j) {
        jumping = j;
    }













}
