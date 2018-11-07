package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;

import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;
import static java.lang.Math.tanh;

public class Player extends GameObject {
    private Bitmap spritesheet;
    private int score;
    // private double dya;
    private boolean moving;
    private boolean playing;
    private boolean jumping;
    private Animation animation = new Animation();
    private long startTime;
    private boolean parachute;
    private boolean anchor;

    private GameCamera camera;

    private World gameWorld;

    private int updateAmount;

    public Player(Bitmap res, int w, int h, int numFrames) {
        updateAmount = 0;

        super.x = 600;
        super.y = GamePanel.HEIGHT/2;
        // super.dy = 0;
        super.height = h;
        super.width = w;

        this.score = 0;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;
        //this.camera = camera;
/*
        for (int i = 0; i< image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet,i*width,0,width,height);

            // image[i] = Animation.rotateImage(image[i],45);
        }*/

        int j = 0;
        for (int i = image.length-1; i >=0; i--) {
            image[i] = Bitmap.createBitmap(spritesheet,j*width,0,width,height);
            j++;
        }

        animation.setFrames(image);
        animation.setDelay(100);
        startTime = System.nanoTime();

    }

    public void setWorldObject(World world) {
        gameWorld = world;
    }

    public void setMoving(boolean b) {
        this.moving = b;
        //dx = 0;
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

    public Missile addMissile(float rawX,float rawY,Bitmap missileBM) {


        int directionX = (int ) rawX +camera.getxOffset() ;
        int directionY = (int) rawY+ camera.getyOffset() ;
        //Log.d("values","x/y: "+ x + "/" + y + "   Dir: " +directionX + "/" + directionY);
        float differenceX =  (x - directionX);
        float differenceY = (y- directionY);

        //Log.d("Diffs",""+differenceX+"/"+differenceY);
        double angle = atan(differenceY/differenceX);

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

    public Shotgun addShot(float rawX, float rawY) {


        int directionX = (int ) rawX ;
        int directionY = (int) rawY;
        // Log.d("values","x/y: "+ x + "/" + y + "   Dir: " +directionX + "/" + directionY);
        float differenceX =  (x - directionX);
        float differenceY = (y - directionY);

        Log.d("Diffs",""+differenceX+"/"+differenceY);
        double angle = Math.toDegrees(atan(differenceY/differenceX));

       // Log.d("angle","deeg "+angle);

        angle = Math.toRadians(angle);

       // Log.d("angle","rad "+angle);

        int velX = (int) (50 * cos(angle)) + dx;
        int velY = (int) (50 * sin(angle)) + dy;
        Log.d("velocity","velX/velY.  "+velX + "/" + velY);

        if (differenceX < 0) {
            velX = -1*velX;
            velY = -1*velY;
        }


        Shotgun newShot = new Shotgun(x,y,4,velX,velY);

        return newShot;
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



    }

    public void setDirection(float rawX,float rawY) {



       // Log.d("parachute",""+parachute);
        int directionX = (int ) rawX +camera.getxOffset() ;
        int directionY = (int) rawY + camera.getyOffset() ;
       // Log.d("values","x/y: "+ x + "/" + y + "   Dir: " +directionX + "/" + directionY);
        int differenceX = - (x - directionX);
        int differenceY = - (y - directionY);

        // Log.d("Diffs",""+differenceX + "/" + differenceY);

        // Tile pointedTile = gameWorld.getTile(differenceX/Tile.TILE_WIDTH,directionY/Tile.TILE_HEIGHT);
        // Log.d("Tile",""+ pointedTile.toString() );



        if (!moving && !jumping) {
            dx = differenceX / 20;
            moving = true;
        }

        if (!jumping && differenceY < -20) {
            jumping = true;
            dy = differenceY / 15 ;
        }
        //  Log.d("Diffs",""+differenceX + "/" + differenceY);


    }

    public void update() {
        updateAmount++;
/*
        long elapsed = (System.nanoTime() - startTime)/1000000;
        if (elapsed > 100) {
            score++;
            startTime = System.nanoTime();
        }
        */
        animation.update();

        //Log.d("PLAYER UPDATE","jump: " + jumping + "   x/y: "+x+"/"+y+"  dx/dy: " +dx + "/" + dy);

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
       // y = y + GamePanel.GRAVITY;


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


        if (dx == 0) {
            moving = false;
        }

        //Collision detection
        if (updateAmount % 60 == 0) {
            Log.d("updateAmount",""+updateAmount);
            Log.d("player POSITION","x/y: " + x +"/"+y + "   dx/dy: " + dx + "/" + dy);
            Log.d("offsets","xOFF/yOFF: " + camera.getxOffset() +"/"+camera.getyOffset());
            Tile pointedTile = gameWorld.getTile(x/Tile.TILE_WIDTH,y/Tile.TILE_HEIGHT);

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
       // Log.d("player update","dx/dy: " + dx +"/"+dy);

        // dy = 0;
    }

    public void draw(Canvas canvas) {
       // Log.d("player DRAW","x/y: " + x +"/"+y);
        // This may need offset in future
        canvas.drawBitmap(animation.getImage() , x -camera.getxOffset(), y - camera.getyOffset(),null);
    }

    public int getScore() {
        return score;
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

    public void resetDYA() {
        // dya = 0;
    }
    public void resetScore() {
        score = 0;
    }












}
