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

import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 1280;
    //public  final float SCALEFACTOR_WIDTH = WIDTH/getWidth();
    public static final int HEIGHT = 720;
    // public final float SCALEFACTOR_HEIGHT = HEIGHT/getHeight();
    public static final int MOVESPEED = 0;
    public static final int GRAVITY = -1;

    private MainThread thread;
    private Background bg;
    private Player player;
    private Enemy enemy;

    private ArrayList<Movepuff> puffs;
    private long puffStartTime;

    private ArrayList<Shotgun> shotgunShots;

    private ArrayList<Missile> missiles;
    private long missileStartTime;

    private SpriteSheet testSheet;

    private World world;

    Random rng;

    private GameCamera camera;

    private WeaponPanel weapons;

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

         bg = new Background(BitmapFactory.decodeResource(getResources(),R.drawable.dry_soil) );
         player = new Player(BitmapFactory.decodeResource(getResources(),R.drawable.heroart),64,64,6);
         enemy = new Enemy(BitmapFactory.decodeResource(getResources(),R.drawable.heroart),64,64,6);

        testSheet = new SpriteSheet(BitmapFactory.decodeResource(getResources(),R.drawable.tilemaptest));
        //   bg.setVector(0,-1);
        puffs = new ArrayList<Movepuff>();
        shotgunShots = new ArrayList<Shotgun>();
        missiles = new ArrayList<Missile>();

        //Assets gameAssets = new Assets(BitmapFactory.decodeResource(getResources(),R.drawable.tilemaptest))
        Assets.init(BitmapFactory.decodeResource(getResources(),R.drawable.tilemaptest));
        camera = new GameCamera(player,0,0);
        player.setCamera(camera);

        puffStartTime = System.nanoTime();
        missileStartTime = System.nanoTime();

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();
        player.setPlaying(true);
        player.setJumping(true);

        rng = new Random();
        world = new World(camera,"");
        player.setWorldObject(world);
        enemy.setWorldObject(world);
        weapons = new WeaponPanel();
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
        float rawXX = event.getRawX();
        float rawYY = event.getRawY();
        float precision = event.getXPrecision();


        Log.d("eventNumber",""+eventNumber);
        Log.d("downtime","" + downTime);
        Log.d("raw X/Y",rawXX+ "/"+rawYY);
        Log.d("precision",""+precision);

        Log.d("Event","onTouchEvent Method");
*/


        // MOTIONEVENT IS TACKING FINGERMOTION EVENTWHOU ACTION DOWN OR UP IS NOT CALLED
        // CONSIDER UPDATING FINGER POSITION TO PLAYER SET DIRECTION

       //  Log.d("HEIGHT/WIDTH", getHeight()+"/" + getWidth());
        float rawX = event.getRawX() *  WIDTH / getWidth();
        float rawY = event.getRawY() * HEIGHT / getHeight();

        if (player.isJumping() ) {
            player.keepDirection(rawX,rawY);
        }
        switch (event.getAction() ) {
            case MotionEvent.ACTION_DOWN:

                if (rawX < 150 && rawY > GamePanel.HEIGHT-150 ) {

                    if (player.isJumping() ) {

                        if (player.isParachute()) {
                            player.setParachute(false);
                        } else {
                            player.setParachute(true);
                        }

                    }
                    else {
                        if (player.isAnchor() ) {
                            player.setAnchor(false);
                        }
                        else {
                            player.setAnchor(true);
                        }
                    }
                    return true;
                }

                else {
                    if (player.isJumping() ) {

                        long missileElapsed = (System.nanoTime() - missileStartTime)/1000000;

                        if (missileElapsed > (500)) {
                            Bitmap missileBM = BitmapFactory.decodeResource(getResources(),R.drawable.missile);
                            missiles.add(player.addMissile(rawX,rawY,missileBM)) ;
                            missileStartTime = System.nanoTime();
                        }
                    }
                    else if (player.isAnchor() ) {
                        long missileElapsed = (System.nanoTime() - missileStartTime)/1000000;

                        if (missileElapsed > (500)) {
                            Bitmap missileBM = BitmapFactory.decodeResource(getResources(),R.drawable.missile);
                            missiles.add(player.addMissile(rawX,rawY,missileBM)) ;
                            missileStartTime = System.nanoTime();
                        }
                    }
                    else {
                        player.setDirection(rawX,rawY);
                    }

                }


              /*
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
                    missiles.add(player.addMissile(rawX,rawY,missileBM)) ;*/

/*
                    shotgunShots.add(player.addShot(rawX,rawY) );

                    float rngFloatX = (float)rng.nextInt(20)-10;
                    float rngFloatY = (float)rng.nextInt(20)-10;
                    shotgunShots.add(player.addShot(rawX+rngFloatX,rawY+rngFloatY) );

                    rngFloatX = (float)rng.nextInt(20)-10;
                    rngFloatY = (float)rng.nextInt(20)-10;
                    shotgunShots.add(player.addShot(rawX+rngFloatX,rawY+rngFloatY) );

*/

/*                }
                else if (player.isAnchor() ) {
                    Bitmap missileBM = BitmapFactory.decodeResource(getResources(),R.drawable.missile);
                    missiles.add(player.addMissile(rawX,rawY,missileBM)) ;
                }

                else {
                    player.setDirection(rawX,rawY);
                }*/
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

            //camera.move(1,0);
            camera.centerOnGameObject();
            bg.update();
            world.update();
            player.update();
            enemy.update();
            if (enemy.isShootingTime() ) {
                Bitmap missileBM = BitmapFactory.decodeResource(getResources(),R.drawable.missile);

                double angle = 2*3.141*rng.nextFloat();
                int velX = (int) (50 * cos(angle));
                int velY = (int) (50 * sin(angle));

                Missile enemyMissile = new Missile(camera,missileBM,enemy.getX(),enemy.getY(),45,15,1,13,(float)angle);
                enemyMissile.setVelocity(-velX/2,-velY/2);
                missiles.add(enemyMissile);
                enemy.setShootingTime(false);
            }
            weapons.update();
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

                int misX = missiles.get(i).getX() - 5;
                int misY = missiles.get(i).getY() - 5;

                int tileX = misX/Tile.TILE_WIDTH;
                int tileY = misY/Tile.TILE_HEIGHT;

                if (missiles.get(i).isActivated() && collision(missiles.get(i),player) ) {
                    Log.e("WE HAVE A HIT","PLAUER YES");
                    player.setX(100);
                    player.setY(100);
                }
                if (missiles.get(i).isActivated() && collision(missiles.get(i),enemy)) {
                    Log.e("WE HAVE A HIT","ENEMY YES");
                    enemy.setX(100);
                    enemy.setY(100);
                }

                Tile missileTile = world.getTile(tileX,tileY);
                // Log.d("Tile", x+"/"+y+"  solid: " + missileTile.isSolid() + "  " + missileTile.toString());
                if (missileTile.isSolid() ) {
                    Log.e("MISSILEHIT","SOLID");

                    for (int yy = -1 ; yy < 2 ; yy++) {
                        for (int xx = -1 ; xx < 2 ; xx++) {
                            world.setTile(tileX+xx,tileY+yy,0);
                        }
                    }

                    missiles.remove(i);
                    continue;
                }
                // Log.d("Tile",""+ pointedTile.toString() );

                if (misX < 0 || misX > world.getWorldWidth() ||
                        misY < 0 || misY > world.getWorldHeight() ) {
                    missiles.remove(i);
                }
            }

            // add smoke puffs if moving and timer
            long elapsed = (System.nanoTime() - puffStartTime)/1000000;
            if (elapsed > 120  ) {

                if (player.isMoving() && !player.isJumping()) {
                    Log.d("add puff","yes");
                    puffs.add(new Movepuff(camera ,player.getX(), player.getY() + 64, 5, player.getDX(), player.getDY()));

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
/*
                int tempX = puffs.get(i).getX();
                int tempY = puffs.get(i).getY();

                if (tempX < 0 || tempX > GamePanel.WIDTH || tempY < 0 || tempY > GamePanel.HEIGHT) {
                    puffs.remove(i);
            }
*/
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

    public GameCamera getGameCamerera() {
        return camera;
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
            world.draw(canvas);
            player.draw(canvas);
            enemy.draw(canvas);
            weapons.draw(canvas);


            // Bitmap testCrop = testSheet.crop(64,64,32,32);
            // canvas.drawBitmap(Assets.grass,200,200,null);

            //canvas.drawBitmap(testSheet.getSpritesheet(),10,10,null);

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
