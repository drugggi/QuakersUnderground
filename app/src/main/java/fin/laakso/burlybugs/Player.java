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

    private boolean shooting;

    private GameCamera camera;
    // private World gameWorld;

    private int updateAmount;
    private WeaponPanel weaponPanel;

    public Player(Bitmap res,WeaponPanel wp, int w, int h, int numFrames) {

        weaponPanel = wp;
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

    public void setShooting(boolean s) {
        shooting = s;
    }
    public boolean isShooting() {
        return shooting;
    }

    @Override
    public void shoot(ArrayList<Weapon> shootingWeapons, int towardsX, int towradsY) {




        long shotElapsed = (System.nanoTime() - backpack.getShotStartTime()) / 1000000;
        // Log.d("shot elapse",System.nanoTime() + "        " + shotElapsed);
        if (shotElapsed < 0 ) {
            // Run click sound
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

        Weapon newWeapon;

        if (weaponPanel.getSelectedItemPosition() == 0) {
            newWeapon = new Shotgun(camera,x+16,y+16,towardsX,towradsY);

            // newWeapon.setVelocity(-velX,-velY);
            newWeapon.setEntity(this);
            backpack.setShotStartTime(System.nanoTime()+ Shotgun.SHOTGUN_COOLDOWNTIME);
        }

        else if (weaponPanel.getSelectedItemPosition() == 1) {
            newWeapon = new Shaft(camera,x+16,y+16,towardsX,towradsY);

            newWeapon.setEntity(this);
            backpack.setShotStartTime(System.nanoTime() + Shaft.SHAFT_COOLDOWNTIME);
        }

        else {
            newWeapon = new Missile(camera, Assets.missile, x + 16, y + 16, 45, 15, 13, (float) angle);

            newWeapon.setVelocity(-velX / 4, -velY / 4);
            newWeapon.setEntity(this);
            backpack.setShotStartTime(System.nanoTime()+Missile.MISSILE_COOLDOWNTIME);
        }
        shootingWeapons.add(newWeapon);

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
        int differenceY = - (y - directionY)*2;


        Log.d("DIfrrerenceX","" + differenceX);

        if ( (differenceX > -200) && (differenceX < 200) ) {
            moving = true;
            dx = dx/2;
        }


        if (!moving && !jumping )  {
            dx = differenceX / 20;
            moving = true;
        }

        if ( dx > 10) {
            dx = 10;
        }
        else if (dx < -10 ){
            dx = -10;
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
        x+= dx;
        if (dx > 0) {
            checkRightTileCollision();
        }
        else if (dx < 0) {
            checkLeftTileCollision();
        }

        if (jumping) {
                if (dy > 15) {
                    dy = 15;
                }
                else {
                    dy = dy - GamePanel.GRAVITY;
                }
        }
        else {
            dy = 0;
            jumping = false;
        }


       //  x += dx ;
        if (y > 0 && y <= gameWorld.getWorldHeight() - super.height && x > 0 && x < gameWorld.getWorldWidth() ) {
            y += dy;
        }

        if (dy >= 0) {

            checkLegTileCollision();
        }

        else {

            checkHeadTileCollision();
  /*
            Tile headTile = gameWorld.getTile((x) / Tile.TILE_WIDTH, (y) / Tile.TILE_HEIGHT);
            // Log.d("Tile", "solid: " + headTile.isSolid() + "  " + headTile.toString());
            if ( headTile.isSolid() ) {
                dy = 0;
            }*/
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

        if (updateAmount % 150 == 0) {
            Log.d("updateAmount",""+updateAmount);
            Log.d("player POSITION","x/y: " + x +"/"+y + "   dx/dy: " + dx + "/" + dy);
            Log.d("offsets","xOFF/yOFF: " + camera.getxOffset() +"/"+camera.getyOffset());
            Tile pointedTile = gameWorld.getTile(x/Tile.TILE_WIDTH,y/Tile.TILE_HEIGHT);

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
