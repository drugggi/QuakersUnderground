package fin.laakso.burlybugs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 856;
    //public  final float SCALEFACTOR_WIDTH = WIDTH/getWidth();
    public static final int HEIGHT = 480;
    // public final float SCALEFACTOR_HEIGHT = HEIGHT/getHeight();
    public static final int MOVESPEED = 0;
    public static final int GRAVITY = -1;

    private MainThread thread;
    private Background bg;
    private Player player;

    private ArrayList<Movepuff> puffs;
    private long puffStartTime;

    private ArrayList<Shotgun> shotgunShots;

    private ArrayList<Missile> missiles;
    private long missileStartTime;

    Random rng;

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
        puffs = new ArrayList<Movepuff>();
        shotgunShots = new ArrayList<Shotgun>();
        missiles = new ArrayList<Missile>();

        puffStartTime = System.nanoTime();
        missileStartTime = System.nanoTime();

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();
        player.setPlaying(true);
        player.setJumping(true);

        rng = new Random();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        int counter = 0;
        while (retry && counter < 1000) {
            counter++;
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

/*

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

*/

       //  Log.d("HEIGHT/WIDTH", getHeight()+"/" + getWidth());
        float rawX = event.getRawX() *  WIDTH / getWidth();
        float rawY = event.getRawY() * HEIGHT / getHeight();

        switch (event.getAction() ) {
            case MotionEvent.ACTION_DOWN:

                if (player.isJumping() ) {

                    if (rawX < 100 && rawY > GamePanel.HEIGHT-100 ) {
                        if (player.isParachute() ) {
                            player.setParachute(false);
                        }
                        else {
                            player.setParachute(true);
                        }
                        return true;
                    }

                    Bitmap missileBM = BitmapFactory.decodeResource(getResources(),R.drawable.missile);

                    missiles.add(player.addMissile(rawX,rawY,missileBM)) ;
/*

                    shotgunShots.add(player.addShot(rawX,rawY) );

                    float rngFloatX = (float)rng.nextInt(20)-10;
                    float rngFloatY = (float)rng.nextInt(20)-10;
                    shotgunShots.add(player.addShot(rawX+rngFloatX,rawY+rngFloatY) );

                    rngFloatX = (float)rng.nextInt(20)-10;
                    rngFloatY = (float)rng.nextInt(20)-10;
                    shotgunShots.add(player.addShot(rawX+rngFloatX,rawY+rngFloatY) );

*/

                } else {
                    player.setDirection(rawX,rawY);
                }
                // Log.d("action","down");
/*

                if(player.isPlaying() ) {
                    //player.setMoving(true);
                    player.setDirection(rawX,rawY);
                }
                else  {
                    player.setPlaying(true);
                }
*/

                return true;

            case MotionEvent.ACTION_UP:
//                player.setMoving(false);

                if (player.isPlaying() ) {
                    player.setMoving(false);
                }
                else {
                    Log.e("ARRAY SIZES","mis: " + missiles.size() + "  puff: " + puffs.size() + " sg: " + shotgunShots.size() );
                    player.setPlaying(true);
                }

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
            //add missiles on timer
  /*
    long missileElapsed = (System.nanoTime() - missileStartTime)/1000000;
            if (missileElapsed > (2000-player.getScore()/4)) {

                //first missile always goes down the middle
                if (missiles.size() == 0) {
                    missiles.add(new Missile(BitmapFactory.decodeResource(
                            getResources(),R.drawable.missile), WIDTH + 10, HEIGHT/2,45,15,player.getScore(),13));
                }
                else {
                    missiles.add(new Missile(BitmapFactory.decodeResource(
                            getResources(),R.drawable.missile), WIDTH + 10,
                            rng.nextInt(HEIGHT),45,15,player.getScore(),13));
                }

                missileStartTime = System.nanoTime();
            }

            for (int i = 0; i < missiles.size() ; i++) {

                missiles.get(i).update();

                if (collision(missiles.get(i),player)) {
                    missiles.remove(i);
                    player.setPlaying(false);
                    break;
                }

                if (missiles.get(i).getX() <- 100) {
                    missiles.remove(i);
                    continue;
                }

                for (Shotgun sg: shotgunShots) {
                    if (collision(sg,missiles.get(i))) {
                        missiles.remove(i);
                        break;
                    }
                }



            }

*/
            for (int i = 0 ; i < missiles.size() ; i++) {
                missiles.get(i).update();
                if (missiles.get(i).getX() < -WIDTH || missiles.get(i).getX() > 2*WIDTH ||
                        missiles.get(i).getY() < -HEIGHT || missiles.get(i).getY() > 2*HEIGHT) {
                    missiles.remove(i);
                }
            }

            // add smoke puffs if moving and timer
            long elapsed = (System.nanoTime() - puffStartTime)/1000000;
            if (elapsed > 120  ) {

                if (player.isMoving() && !player.isJumping()) {
                    //Log.d("add puff","yes");
                    puffs.add(new Movepuff(player.getX(), player.getY() + 40, 5, player.getDX(), player.getDY()));
                    puffStartTime = System.nanoTime();
                }
                else {
                    if (puffs.size() != 0 ) {
                        puffs.remove(0);
                    }
                }
            }
            for (int i = 0; i < puffs.size() ; i++) {
                puffs.get(i).update();

                int tempX = puffs.get(i).getX();
                int tempY = puffs.get(i).getY();
                if (tempX < 0 || tempX > GamePanel.WIDTH || tempY < 0 || tempY > GamePanel.HEIGHT) {
                    puffs.remove(i);
                }

            }
            for (int i = 0; i< shotgunShots.size() ; i++) {
                int tempX = shotgunShots.get(i).getX();
                int tempY = shotgunShots.get(i).getY();
                if (tempX < 0 || tempX > GamePanel.WIDTH || tempY < 0 || tempY > GamePanel.HEIGHT) {
                    shotgunShots.remove(i);
                }
            }
            for (Shotgun shot: shotgunShots) {
                shot.update();
            }


            if (puffs.size() > 30) {
                puffs.clear();
            }

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

            for (Movepuff mp: puffs) {
                mp.draw(canvas);
            }
            for (Shotgun shot: shotgunShots) {
                shot.draw(canvas);
            }
            for (Missile m: missiles) {
                m.draw(canvas);
            }
            canvas.restoreToCount(savedState);
        }
    }

    public boolean collision(GameObject a, GameObject b) {

        if (Rect.intersects(a.getRectangle(),b.getRectangle() )) {
            return true;
        }
        return false;
    }

}
