package fin.laakso.burlybugs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class WeaponPanel extends GameObject {

    Rect weapons;
    Rect items;
    Paint weaponsPaint;
    Paint itemsPaint;
   // private GameCamera camera;

    public WeaponPanel() {
        // this.radius= r;
        // this.velocityX = -velocityX;
        //this.velocityY = -velocityY;

        weapons = new Rect(0,0,100,GamePanel.HEIGHT-150);
        items = new Rect(0,GamePanel.HEIGHT-150,150,GamePanel.HEIGHT);
        itemsPaint = new Paint();
        itemsPaint.setColor(Color.RED);
        itemsPaint.setStyle(Paint.Style.FILL);
        itemsPaint.setAlpha(50);

        weaponsPaint = new Paint();
        weaponsPaint.setColor(Color.GRAY);
        weaponsPaint.setStyle(Paint.Style.FILL);
        weaponsPaint.setAlpha(100);
        //super.x = x;
        //super.y = y;
        //this.camera = camera;
    }

    public void update() {

    }

    public void draw(Canvas canvas)
    {
        canvas.drawRect(items,itemsPaint);
        canvas.drawRect(weapons, weaponsPaint);
    }



}
