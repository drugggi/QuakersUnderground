package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

public class Missile extends GameObject {

    private int score;
    private int speed;
    private Random rng = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;

    private int velocityX;
    private int velocityY;

    private GameCamera camera;

    public Missile(GameCamera camera,Bitmap res,int x, int y, int w, int h, int s, int numFrames, float angle) {

        super.x = x; // x-camera.getxOffset();
        super.y = y; //y-camera.getyOffset();
        width = w;
        height = h;
        score = s;

        speed = 1 + rng.nextInt(3);

        // cap missile speed
        if (speed >= 40) {
            speed = 40;
        }

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;
        for( int i = 0 ; i < image.length ; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, 0 , i*height,width,height);

            image[i] = Animation.rotateImage(image[i],angle);
        }

        this.camera = camera;
        animation.setFrames(image);
        animation.setDelay(100-speed);
    }

    public void update() {
       //  x -= speed;

        Log.d("MISSILE X/y",""+ x +"/" + y);
        x += velocityX;
        y += velocityY;


        //velocityX++;
        //velocityY++;

        animation.update();

    }

    public void setVelocity(int velX, int velY) {
        velocityX = velX;
        velocityY = velY;
    }

    public void draw(Canvas canvas) {
       //Log.d("missiles x/y",""+x+"/" + y + "  offsetxy " + camera.getxOffset() + "/" + camera.getyOffset());
      // Log.d("Missile drapos",(x-camera.getxOffset())+"/"+(y-camera.getyOffset()));
        try {
            canvas.drawBitmap(animation.getImage(),x-camera.getxOffset(),y-camera.getyOffset(),null);

        } catch (Exception e) {

        }

    }
    @Override
    public int getWidth() {
        // offset slightly for more realistic collision detection
        return width - 10;
    }


}
