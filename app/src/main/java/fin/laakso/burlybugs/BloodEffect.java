package fin.laakso.burlybugs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BloodEffect extends WeaponEffect {

    int radius;
    int update;
    Paint paint;

    public BloodEffect(GameCamera camera,int startX, int startY, int r) {


        this.radius = r;
        super.x = startX;
        super.y = startY;
        super.camera = camera;
        this.update = 0;

        super.dx = 0;
        super.dy = 0;

        knockBackApplied = true;

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(100+GamePanel.rng.nextInt(155));
    }


    @Override
    public boolean finished() {
        if (update > 30 ) {
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        x += dx;

        dy = dy - GamePanel.GRAVITY;
        y += dy;
        update++;

        int alpha = paint.getAlpha()-10;
        if (alpha > 10) {
            paint.setAlpha(alpha);
        }
    }

    @Override
    public void draw(Canvas canvas) {

        // canvas.drawBitmap(animation.getImage(),x-camera.getxOffset(),y-camera.getyOffset(),null);

        canvas.drawCircle(x-radius-camera.getxOffset(), y -radius -camera.getyOffset(), radius , paint);

         // canvas.drawCircle(x-radius+2, y-radius-2, radius ,paint);

         //  canvas.drawCircle(x-radius+4,y-radius+1,radius,paint);

         //canvas.drawLine((float)x-camera.getxOffset(),(float)y-camera.getyOffset(),(float)towardsX-camera.getxOffset(),(float)towardsY-camera.getyOffset(),paint);
    }
}
