package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class Background {

    private Bitmap image;
    private int x, y, dx , dy;

    public Background(Bitmap res) {
        image = res;
        dx = GamePanel.MOVESPEED;
    }

    public void update() {
        x += dx;
        y += dy;
        if (x < -GamePanel.WIDTH) {
            x = 0;
        }
        else if (x > GamePanel.WIDTH ){
            x = 0;
        }

        if (y < -GamePanel.HEIGHT) {
            y = 0;
        }
        else if (y > GamePanel.HEIGHT) {
            y = 0;
        }

    }

    public void draw(Canvas canvas) {

        canvas.drawBitmap(image,x,y,null);

       // Depending where the original imges we have to draw three more images either top bottom left or right
        // from the original image so that whole screen is filled
       if ( x <= 0 && y <= 0) {
            canvas.drawBitmap(image,x+GamePanel.WIDTH,y,null);
           canvas.drawBitmap(image,x,y+GamePanel.HEIGHT,null);
           canvas.drawBitmap(image,x+GamePanel.WIDTH,y+GamePanel.HEIGHT,null);
            // Log.d("XY NEW","x/y " + x + "/" + y);
        }
        else if ( x <= 0 ){
           canvas.drawBitmap(image,x+GamePanel.WIDTH,y,null);
           canvas.drawBitmap(image,x,y-GamePanel.HEIGHT,null);
           canvas.drawBitmap(image,x+GamePanel.WIDTH,y-GamePanel.HEIGHT,null);
       }

        else if ( y <= 0) {
           canvas.drawBitmap(image,x-GamePanel.WIDTH,y,null);
           canvas.drawBitmap(image,x,y+GamePanel.HEIGHT,null);
           canvas.drawBitmap(image,x-GamePanel.WIDTH,y+GamePanel.HEIGHT,null);
        }
        else  {
           canvas.drawBitmap(image,x-GamePanel.WIDTH,y,null);
           canvas.drawBitmap(image,x,y-GamePanel.HEIGHT,null);
           canvas.drawBitmap(image,x-GamePanel.WIDTH,y-GamePanel.HEIGHT,null);
       }

    }

    // Probably will never be used, sets the background moving in one direction all the time
    public void setVector(int dx, int dy) {
        this.dy = dx;
        this.dx = dy;

    }



}
