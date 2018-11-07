package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class WeaponEffect extends GameObject {


    private Animation animation = new Animation();
    private Bitmap spritesheet;

    private GameCamera camera;
    private boolean knockBackApplied;

    public WeaponEffect(GameCamera camera, Bitmap res, int x, int y, int w, int h, int numFrames) {

        super.x = x-50; // x-camera.getxOffset();
        super.y = y-50; //y-camera.getyOffset();
        width = w;
        height = h;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        // Stupid stupid stupid
       for (int i = 0; i < 5 ; i++) {
           for (int j = 0 ; j < 5 ; j++) {
               image[i] = Bitmap.createBitmap(spritesheet, j * width, i * height, width, height);
           }
        }

        knockBackApplied = false;
        this.camera = camera;
        animation.setFrames(image);
        animation.setDelay(50);

    }


    public void update() {

        animation.update();

    }

    public boolean isKnockBackApplied() {
        return knockBackApplied;
    }

    public void setKnockBackApplied(boolean kb) {
        knockBackApplied = kb;
    }


    public boolean finished() {
        return animation.playedOnce();
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
        return width;
    }

}
