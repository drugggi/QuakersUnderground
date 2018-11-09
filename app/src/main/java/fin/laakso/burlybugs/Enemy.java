package fin.laakso.burlybugs;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Enemy extends Entity {

    private Bitmap spritesheet;

    private boolean moving;
    private boolean playing;
    // private boolean jumping;
    private Animation animation = new Animation();
    private long startTime;
    private boolean parachute;
    private boolean anchor;
    private boolean shootingTime;

    private GameCamera camera;
    // private World gameWorld;
    private long missileStartTime;

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
      //  Log.d("ENEMY DIRECTION", "diffx/diffy: " + differenceX + "/" + differenceY + "   dx/dy: " + dx + "/" + dy);

    }

    private void makeIntelligentDecision() {

        if (updateAmount % 30 == 0) {
         //   Log.d("ENEMY STATE", "moving:  " + moving + "  jump: " + jumping + "  anch: " + anchor + "  para: " + parachute);
         //   Log.d("ENEMY STATE", "x/y: " + x + "/" + y + "   dx/dy: " + dx + "/" + dy);
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

    public Missile addMissile(float playerX, float playerY, Bitmap missileBM) {


        int directionX = (int ) playerX;// +camera.getxOffset() ;
        int directionY = (int) playerY;//+ camera.getyOffset() ;
        //Log.d("values","x/y: "+ x + "/" + y + "   Dir: " +directionX + "/" + directionY);
        float differenceX =  (x - directionX);
        float differenceY = (y- directionY);

        //Log.d("Diffs",""+differenceX+"/"+differenceY);
        double angle = atan(differenceY/differenceX);
        angle = rng.nextGaussian()*0.5+angle;

        //Log.d("angle","deeg "+angle);

        //Log.d("angle","rad "+angle);

        int velX = (int) (50 * cos(angle));
        int velY = (int) (50 * sin(angle));
        //Log.d("velocity","velX/velY.  "+velX + "/" + velY);

        if (differenceX < 0) {
            velX = -1*velX;
            velY = -1*velY;
        }
        angle = Math.toDegrees(angle);


        //int playerAdditionX = 0,playerAdditionY = 0;
        if (differenceX < 0) {
            angle += 180;
            //playerAdditionX = 40;
        }
        if (differenceY < 0) {
            //playerAdditionY = 64;
        }
        Missile newMissile = new Missile(camera,missileBM,x+16,y+16,45,15,1,13,(float)angle);
        newMissile.setVelocity(-velX/2,-velY/2);

        return newMissile;
/*
        missiles.add(new Missile(BitmapFactory.decodeResource(
                getResources(),R.drawable.missile), WIDTH + 10, HEIGHT/2,45,15,player.getScore(),13));
        */
    }
/*

    public void setKnockBack(int knockX, int knockY) {

        int differenceX = getCenterX() - knockX;
        int differenceY = getCenterY() - knockY;



        //Log.d("KNOCKBACK","center x/y: " + getCenterX() + "/"+getCenterY()+ " knox/y"+knockX + "/" +knockY + "  diff:" + differenceX+ "/" + differenceY);

        //Log.d("Difference","  diff:" + differenceX+ "/" + differenceY);

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
                jumping = true;
                health += differenceY;

            } // explosion under enemy, knockback upwards
        else {
                y += -50;
                dy += -20;
                jumping = true;
                health -= differenceY;
        }

        if (health <= 0) {
            x = rng.nextInt(1200)+100;
            y = 100;
            super.health = 300;
        }

        Log.d("Health","HEALTH: " + health);


    }
*/

    public void update() {
        animation.update();
        updateAmount++;
        makeIntelligentDecision();

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
