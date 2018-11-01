package fin.laakso.burlybugs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Movepuff extends GameObject {

    public int radius;
    private int velocityX;
    private int velocityY;

    public Movepuff(int x, int y, int r, int velocityX, int velocityY) {
        this.radius= r;
        this.velocityX = -velocityX;
        this.velocityY = -velocityY;
        super.x = x;
        super.y = y;
    }

    public void update() {
        // Log.d("vels",""+ velocityX +"/" + velocityY);
        x += velocityX/50;
        y += velocityY/50;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(50);

        canvas.drawCircle(x-radius, y -radius, radius , paint);

      //  canvas.drawCircle(x-radius+2, y-radius-2, radius ,paint);

        canvas.drawCircle(x-radius+4,y-radius+1,radius,paint);
    }
}
