package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

public class Item extends GameObject {

    //Rect armor;
    Paint armorPaint;

    // private GameCamera camera;
    Animation animation = new Animation();
    private Bitmap spritesheet;
    Random rng;
    // World gameWorld;
    GameCamera camera;

    public Item(World gameWorld, int x, int y, int armorAmount) {
        // this.radius= r;
        // this.velocityX = -velocityX;
        //this.velocityY = -velocityY;

        rng = new Random();

        if (x == 0 && y== 0) {
            this.x = rng.nextInt(gameWorld.getWorldWidth()-200)+100;
            this.y = rng.nextInt(gameWorld.getWorldHeight()-200)+100;
        }
        else {
            this.x = x;
            this.y = y;
        }

        width = 32;
        height = 32;

        spritesheet = Assets.redarmor;
        Bitmap[] image = new Bitmap[7];


        for (int i = 0; i < image.length ; i++) {
            image[i] = Bitmap.createBitmap(spritesheet,i*width,0,width,height);
        }


        animation.setFrames(image);
        animation.setDelay(100);
/*
        armorPaint = new Paint();
        if (armorAmount == 100) {
            armorPaint.setColor(Color.GREEN);
        }
        else if (armorAmount == 150 ) {
            armorPaint.setColor(Color.YELLOW);
        }
        else {
            armorPaint.setColor(Color.RED);
        }
        armorPaint.setStyle(Paint.Style.FILL);
        armorPaint.setAlpha(150);
*/

        camera = gameWorld.getCamera();
    }

    public void update() {
        // armor.set(this.x-camera.getxOffset(),this.y-camera.getyOffset(),50,50);
        animation.update();
    }

    public void draw(Canvas canvas)
    {

        canvas.drawBitmap(animation.getImage(),x-camera.getxOffset(),y-camera.getyOffset(),null);
/*
        canvas.drawRect(this.x-camera.getxOffset(),this.y-camera.getyOffset(),
                this.x-camera.getxOffset()+width,this.y-camera.getyOffset()+height,armorPaint);
*/

    }
}
