package fin.laakso.burlybugs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Movepuff extends GameObject {

    public int radius;
    private int velocityX;
    private int velocityY;
    private GameCamera camera;

    public Movepuff(GameCamera camera, int x, int y, int r, int velocityX, int velocityY) {
        this.radius= r;
        this.velocityX = -velocityX;
        this.velocityY = -velocityY;
        super.x = x;
        super.y = y;
        this.camera = camera;
    }

    public void update() {
        //Log.d("vels",""+ velocityX +"/" + velocityY);
        //Log.d("UPDATE PUFF X/Y","" + x + "/" + y);
        x += velocityX/2;
        y += velocityY/2;
    }


    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(120);

        //Log.d("DRAW PUFF X/Y","" + x + "/" + y);

        canvas.drawCircle(x-camera.getxOffset()- radius, y -camera.getyOffset() - radius, radius , paint);

      //  canvas.drawCircle(x-radius+2, y-radius-2, radius ,paint);

        canvas.drawCircle(x-camera.getxOffset() -radius+4,y-camera.getyOffset()-radius+1,radius,paint);
    }
}
