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
    private boolean startAnimation;

    public void setFrames(Bitmap[] frames) {

        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
        startAnimation = true;
    }

    public void setDelay(long d) {
        delay = d;
    }
    public void setFrames(int i) {
        currentFrame = i;
    }

    public void update() {

        // Would it be better to count frames rather than system nanotime
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

    public static Bitmap rotateImage(Bitmap src, float degree)
    {

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return bmp;
    }

}
