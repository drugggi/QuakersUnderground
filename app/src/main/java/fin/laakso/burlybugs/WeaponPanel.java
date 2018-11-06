package fin.laakso.burlybugs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class WeaponPanel extends GameObject {

    Rect wp;

    private GameCamera camera;

    public WeaponPanel(GameCamera camera) {
        // this.radius= r;
        // this.velocityX = -velocityX;
        //this.velocityY = -velocityY;

        wp = new Rect(0,GamePanel.HEIGHT-150,150,GamePanel.HEIGHT);
        //super.x = x;
        //super.y = y;
        this.camera = camera;
    }

    public void update() {

    }

    public void draw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(50);

        canvas.drawRect(wp, paint);

    }


}
