package fin.laakso.burlybugs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Shotgun extends Weapon {

    public int radius;
    //private int velocityX;
    //private int velocityY;

    public Shotgun(int x, int y, int r, int velocityX, int velocityY) {
        this.radius= r;
        super.velocityX = -velocityX;
        super.velocityY = -velocityY;
        super.x = x;
        super.y = y;
    }

    public void update() {
        // Log.d("vels",""+ velocityX +"/" + velocityY);
        x += velocityX;
        y += velocityY;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(255);

        canvas.drawCircle(x-radius, y -radius, radius , paint);

         canvas.drawCircle(x-radius+2, y-radius-2, radius ,paint);

        canvas.drawCircle(x-radius+4,y-radius+1,radius,paint);
    }
}
