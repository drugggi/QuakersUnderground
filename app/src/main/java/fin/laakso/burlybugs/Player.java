package fin.laakso.burlybugs;

import android.graphics.Bitmap;
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

    public Player(Bitmap res, int w, int h, int numFrames) {
        super.x = 100;
        super.y = GamePanel.HEIGHT/2;
        // super.dy = 0;
        super.height = h;
        super.width = w;

        this.score = 0;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i< image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet,i*width,0,width,height);

        }

        animation.setFrames(image);
        animation.setDelay(100);
        startTime = System.nanoTime();

    }

    public void setMoving(boolean b) {
        this.moving = b;
        //dx = 0;
    }

    public void setDirection(float rawX,float rawY) {



        int directionX = (int ) rawX ;
        int directionY = (int) rawY;
        // Log.d("values","x/y: "+ x + "/" + y + "   Dir: " +directionX + "/" + directionY);
        int differenceX = - (x - directionX);
        int differenceY = - (y - directionY);

        // dy = differenceY;

        // Log.d("Diffs",""+differenceX + "/" + differenceY);

        if (!moving && !jumping) {
            dx = differenceX / 10;
            moving = true;
        }
        if (!jumping && differenceY < -20) {
            jumping = true;
            dy = differenceY / 10;
        }
        Log.d("Diffs",""+differenceX + "/" + differenceY);
/*
        if (x < directionX) {
            dx = differenceX;
            //x++;
        }
        else {
            dx = -1;
            // x--;
        }

        if(y < directionY ) {
            dy = 1;
            // y++;
        }
        else {
            dy = -1;
            // y--;
        }*/

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


/*

        if (differenceX > 0) {
            velX = 50;
        }
        else {
            velX = -50;
        }

        if (differenceY > 0) {
            velY = 50;
        }
        else {
            velY = -50;
        }
*/

        Shotgun newShot = new Shotgun(x,y,4,velX,velY);


        return newShot;
    }

    public void update() {
        long elapsed = (System.nanoTime() - startTime)/1000000;
        if (elapsed > 100) {
            score++;
            startTime = System.nanoTime();
        }
        animation.update();

        //Log.d("PLAYER UPDATE","jump: " + jumping + "   x/y: "+x+"/"+y+"  dx/dy: " +dx + "/" + dy);

        if (!moving) {
            // consider dx = dx *  3 /4
            dx = dx / 2;
        }

        if (jumping) {
            dy = dy - GamePanel.GRAVITY;
        }
        else {
            dy = 0;
            jumping = false;
        }
       // y = y + GamePanel.GRAVITY;

        x += dx ;
        if (y > 0 && y <= GamePanel.HEIGHT - 40 && x > 0 && x < GamePanel.WIDTH) {
                y += dy ;

        }

        if (y >= GamePanel.HEIGHT - 40) {
            y = GamePanel.HEIGHT - 40;
            dy = 0;

            jumping = false;
        }
        if (dx == 0) {
            moving = false;
        }



/*

        if (y > 0 && y < GamePanel.HEIGHT - 25 && x > 0 && x < GamePanel.WIDTH) {
            if (moving) {
                x += dx / 50;
                y += dy / 50;

            } else {
                x += dx / 50;
                y += dy / 50;

                dx = dx / 2;
                dy = dy / 2;

            }
        }

*/


/*

            dy = 0;
            dx = 0;
            up = false;
*/

 /*           if (y < 0) { y += 5; dy = 0; }
            else {y -= 5; dy = 0; }
            if (x < 0) { x += 5; dx = 0;}
            else {x -= 5; dx = 0;}*/

/*

        if (y > 0 && y < GamePanel.HEIGHT - 25) {
            if (up) {
                dy = (int) (dya -= 1.1);
            } else {
                dy = (int) (dya += 1.1);
            }

            if (dy > 14) {
                dy = 14;
            }

            if (dy < -14) {
                dy = -14;
            }

            y += dy * 2;
        }
*/

       // Log.d("player update","dx/dy: " + dx +"/"+dy);

        // dy = 0;
    }

    public void draw(Canvas canvas) {
       // Log.d("player DRAW","x/y: " + x +"/"+y);
        canvas.drawBitmap(animation.getImage() , x , y ,null);
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
