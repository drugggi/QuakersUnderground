package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;

public class Player extends GameObject {
    private Bitmap spritesheet;
    private int score;
    private double dya;
    private boolean up;
    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;

    public Player(Bitmap res, int w, int h, int numFrames) {
        super.x = 100;
        super.y = GamePanel.HEIGHT/2;
        super.dy = 0;
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

    public void setUp(boolean b) {
        this.up = b;
    }

    public void setDirection(float rawX,float rawY) {
        int directionX = (int ) rawX ;
        int directionY = (int) rawY;

        int differenceX = (directionX - x);
        int differenceY = (directionY - y);

        dx = differenceX;
        dy = differenceY;

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

    public void update() {
        long elapsed = (System.nanoTime() - startTime)/1000000;
        if (elapsed > 100) {
            score++;
            startTime = System.nanoTime();
        }
        animation.update();

        if (y > 0 && y < GamePanel.HEIGHT - 25 && x > 0 && x < GamePanel.WIDTH) {
            if (up) {
                x += dx / 50;
                y += dy / 50;

            } else {
                x += dx / 50;
                y += dy / 50;

                dx = dx / 2;
                dy = dy / 2;

            }
        }
        else {


/*

            dy = 0;
            dx = 0;
            up = false;
*/

 /*           if (y < 0) { y += 5; dy = 0; }
            else {y -= 5; dy = 0; }
            if (x < 0) { x += 5; dx = 0;}
            else {x -= 5; dx = 0;}*/
        }
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
        Log.d("player DRAW","x/y: " + x +"/"+y);
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

    public void resetDYA() {
        dya = 0;
    }
    public void resetScore() {
        score = 0;
    }












}
