package fin.laakso.burlybugs;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

public class Enemy extends GameObject {

    private Bitmap spritesheet;

    private boolean moving;
    private boolean playing;
    private boolean jumping;
    private Animation animation = new Animation();
    private long startTime;
    private boolean parachute;
    private boolean anchor;
    private boolean shootingTime;

    private GameCamera camera;
    private World gameWorld;
    private long missileStartTime;

    private int updateAmount;
    Random rng;
    private int nextDecision;

    public Enemy(Bitmap res, int w, int h, int numFrames) {

        super.x = 700;
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
            // image[i] = Animation.rotateImage(image[i],45);
        }
        nextDecision = 60;

        animation.setFrames(image);
        animation.setDelay(100);
        startTime = System.nanoTime();

        missileStartTime = System.nanoTime();
        shootingTime = false;

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


            if (rng.nextBoolean() ) {
                dx = differenceX / 20;

                dy = differenceY / 15;
                jumping = true;
                moving = true;
            }
            else {
                shootingTime = true;
            }
        Log.d("ENEMY DIRECTION", "diffx/diffy: " + differenceX + "/" + differenceY + "   dx/dy: " + dx + "/" + dy);

    }

    private void makeIntelligentDecision() {

        if (updateAmount % 30 == 0) {
            Log.d("ENEMY STATE", "moving:  " + moving + "  jump: " + jumping + "  anch: " + anchor + "  para: " + parachute);
            Log.d("ENEMY STATE", "x/y: " + x + "/" + y + "   dx/dy: " + dx + "/" + dy);
        }

        if (updateAmount % nextDecision == 0) {
            nextDecision = 30*rng.nextInt(3)+30;
            // Log.d("NEXT DES",""+nextDecision);
            int directX = rng.nextInt(1200)-600;
            int directY = -rng.nextInt(600)-100;
            setDirection(directX,directY);

           // moving = true;
        }



    }

    public void setKnockBack(int knockX, int knockY) {
        if (x < knockX) {
            dx = -20;
        }
        else {
            dx = 20;
        }

        if (y < knockY) {
            y -= 100;
            dy = -20;
            jumping = true;
        }
        else {
            y -= 100;
            dy = -20;
            jumping = true;
        }

    }

    public void update() {
        animation.update();
        updateAmount++;
        // makeIntelligentDecision();

        dx = dx *  11 / 12;
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
        }if (y >= gameWorld.getWorldHeight() - super.height) {
            //  Log.d("h","" + gameWorld.getWorldHeight() ) ;
            y = gameWorld.getWorldHeight() - super.height;
            dy = 0;

            jumping = false;
        }


        if (dx == 0) {
            moving = false;
        }


        if (dy >= 0) {

            Tile legTile = gameWorld.getTile((x) / Tile.TILE_WIDTH, (y+height) / Tile.TILE_HEIGHT);
            // Log.d("Tile", "solid: " + legTile.isSolid() + "  " + legTile.toString());

            if ( legTile.isSolid() ) {
                dy = 0;
                jumping = false;
                // y =
            } else {
                jumping = true;
            }

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

    public void draw(Canvas canvas) {

        canvas.drawBitmap(animation.getImage() , x -camera.getxOffset(), y - camera.getyOffset(),null);

    }



}
