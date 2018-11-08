package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.ImageView;

public class Animation {
    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean playedOnce;
    private boolean knockBackApplied;
    private boolean startAnimation;

    public void setFrames(Bitmap[] frames) {

        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
        // knockBackApplied = false;
        startAnimation = true;
    }

    public void setDelay(long d) {
        delay = d;
    }
    public void setFrames(int i) {
        currentFrame = i;
    }

    public void update() {

        if (startAnimation) {
            long elapsed = (System.nanoTime() - startTime) / 1000000L;

            if (elapsed > delay) {
                currentFrame++;
                startTime = System.nanoTime();
            }

         if (currentFrame == frames.length) {
             currentFrame = 0;
            playedOnce = true;
            }
        }
    }

    public void startAnimation(boolean sa) {
        startAnimation = sa;
    }

    public Bitmap getImage() {
        return frames[currentFrame];
    }
    public int getFrame(){
        return currentFrame;
    }
    public boolean playedOnce() {
        return playedOnce;
    }

/*

    public boolean isKnockBackApplied() {
        return knockBackApplied;
    }
    public void setKnowckBack(boolean kb) {
        knockBackApplied = kb;
    }
*/

    public static Bitmap rotateImage(Bitmap src, float degree)
    {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return bmp;
    }
/*
    public static Bitmap rotateBitmap(Bitmap bm,float x) {
        Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),R.drawable.tedd);

        int width = bitmapOrg.getWidth();

        int height = bitmapOrg.getHeight();


        int newWidth = 200;

        int newHeight  = 200;

        // calculate the scale - in this case = 0.4f

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(x);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,width, height, matrix, true);

        iv.setScaleType(ImageView.ScaleType.CENTER);
        iv.setImageBitmap(resizedBitmap);
    }*/

}
