package fin.laakso.burlybugs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

public class GameItem extends GameObject {

    //Rect armor;
    Paint armorPaint;

    // private GameCamera camera;

    Random rng;
    // World gameWorld;
    GameCamera camera;

    public GameItem(World gameWorld,int x, int y, int armorAmount) {
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
        width = 50;
        height = 50;

        // armor = new Rect(this.x,this.y,50,50);

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

        camera = gameWorld.getCamera();
    }

    public void update() {
        // armor.set(this.x-camera.getxOffset(),this.y-camera.getyOffset(),50,50);

    }

    public void draw(Canvas canvas)
    {
        canvas.drawRect(this.x-camera.getxOffset(),this.y-camera.getyOffset(),
                this.x-camera.getxOffset()+width,this.y-camera.getyOffset()+height,armorPaint);

    }
}
