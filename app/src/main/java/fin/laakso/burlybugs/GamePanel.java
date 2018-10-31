package fin.laakso.burlybugs;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = 0;

    private MainThread thread;
    private Background bg;
    private Player player;

    public GamePanel(Context context) {
        super(context);

        // add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder() , this);

        // make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

         bg = new Background(BitmapFactory.decodeResource(getResources(),R.drawable.soilbg) );
         player = new Player(BitmapFactory.decodeResource(getResources(),R.drawable.firstburlybug),40,40,4);
        // bg.setVector(-5,-5);

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
           // retry = false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

/*// uusia suuntia MATH random
        Random rng = new Random();
        int newDX = rng.nextInt(20)-10;
        int newDY = rng.nextInt(20)-10;

         bg.setVector(newDX,newDY);*/


        int eventNumber = event.getAction();
        long downTime = event.getDownTime();
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        float precision = event.getXPrecision();


        Log.d("eventNumber",""+eventNumber);
        Log.d("downtime","" + downTime);
        Log.d("raw X/Y",rawX+ "/"+rawY);
        Log.d("precision",""+precision);

        Log.d("Event","onTouchEvent Method");


        switch (event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                // Log.d("action","down");

                if(player.isPlaying() ) {
                    player.setUp(true);
                    player.setDirection(rawX,rawY);
                }
                else  {
                    player.setPlaying(true);
                }

                return true;

            case MotionEvent.ACTION_UP:
                player.setUp(false);

                //Log.d("action","up");
                return true;
                //break;

            default:
               // Log.d("action","default");

        }

        return super.onTouchEvent(event);

    }

    public void update() {

        if (player.isPlaying() ) {
            bg.update();
            player.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        final float scaleFactorX = (float) getWidth()/WIDTH;
        final float scaleFactorY = (float) getHeight()/HEIGHT;

        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX,scaleFactorY);

            bg.draw(canvas);
            player.draw(canvas);

            canvas.restoreToCount(savedState);
        }
    }


}
