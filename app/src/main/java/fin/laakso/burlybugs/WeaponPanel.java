package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class WeaponPanel extends GameObject {

    private Rect weaponsBackground;
    private Rect items;
    private Rect selectedItem;
    private Paint weaponsPaint;
    private Paint itemsPaint;
    private Paint selectedItemPaint;
    private int selectedItemPosition;

    Bitmap weapons;
   // private GameCamera camera;

    public WeaponPanel(Bitmap res) {
        // this.radius= r;
        // this.velocityX = -velocityX;
        //this.velocityY = -velocityY;

        weaponsBackground = new Rect(0,0,100,GamePanel.HEIGHT-150);
        items = new Rect(0,GamePanel.HEIGHT-150,150,GamePanel.HEIGHT);
        itemsPaint = new Paint();
        itemsPaint.setColor(Color.RED);
        itemsPaint.setStyle(Paint.Style.FILL);
        itemsPaint.setAlpha(50);

        selectedItemPosition = 0;
        selectedItem = new Rect(0,selectedItemPosition*95,100,selectedItemPosition*95+95);
        selectedItemPaint = new Paint();
        selectedItemPaint.setColor(Color.DKGRAY);
        selectedItemPaint.setStyle(Paint.Style.FILL);
        selectedItemPaint.setAlpha(150);

        weaponsPaint = new Paint();
        weaponsPaint.setColor(Color.GRAY);
        weaponsPaint.setStyle(Paint.Style.FILL);
        weaponsPaint.setAlpha(100);

        weapons = res;
        //super.x = x;
        //super.y = y;
        //this.camera = camera;
    }

    public void update() {
        // selectedItem = new Rect(0,selectedItemPosition*95,100,selectedItemPosition*95+95);
        selectedItem.set(0,selectedItemPosition*95,100,selectedItemPosition*95+95);
    }

    public void setSelectedItem(float positionX,float positionY) {

        //int position = ((int)(positionY + 5) ) / 100;
        selectedItemPosition = ((int)(positionY + 5) ) / 100;
        Log.d("selectedITEMPOS",positionX + "/"+positionY+"   ItemPos: " + selectedItemPosition);

    }

    public void draw(Canvas canvas)
    {

        //canvas.drawRect(items,itemsPaint);
        canvas.drawRect(weaponsBackground, weaponsPaint);
        canvas.drawRect(selectedItem,selectedItemPaint);

        canvas.drawBitmap(weapons,0,0,null);
    }



}
