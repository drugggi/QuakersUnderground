package fin.laakso.burlybugs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ShotgunEffect extends WeaponEffect {

    private int towardsX;
    private int towardsY;
    private int update;

    public ShotgunEffect(GameCamera camera,int fromX,int fromY, int toX, int toY) {
        super.camera = camera;
        super.x = fromX;
        super.y = fromY;

        update = 0;

        this.towardsX = toX;
        this.towardsY = toY;

        super.knockBackApplied = true;
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
        update++;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(255);

     /*   float[] testFloat = new float[] {(float)x-camera.getxOffset(),(float)y-camera.getyOffset(),(float)towardsX-camera.getxOffset(),(float)towardsY-camera.getyOffset()};

        canvas.drawLines(testFloat,paint);*/
        //canvas.drawRect(x-camera.getxOffset(),y-camera.getyOffset(),(float)towardsX-camera.getxOffset(),(float)towardsY-camera.getyOffset(),paint);
        canvas.drawLine((float)x-camera.getxOffset(),(float)y-camera.getyOffset(),(float)towardsX-camera.getxOffset(),(float)towardsY-camera.getyOffset(),paint);
    }
}
