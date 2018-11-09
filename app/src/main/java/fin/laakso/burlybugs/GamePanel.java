package fin.laakso.burlybugs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

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
    private ArrayList<WeaponEffect> weaponEffects;
    private ArrayList<Item> items;

    private ArrayList<Missile> missiles;
    private ArrayList<Weapon> weapons;
    private ArrayList<Entity> players;

    private long missileStartTime;

    private SpriteSheet testSheet;

    private World world;

    Random rng;

    private GameCamera camera;

    private WeaponPanel weaponPanel;

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
         weaponEffects = new ArrayList<>();
         items = new ArrayList<>();
         weapons = new ArrayList<>();
         players = new ArrayList<>();

        testSheet = new SpriteSheet(BitmapFactory.decodeResource(getResources(),R.drawable.tilemaptest));
        //   bg.setVector(0,-1);
        puffs = new ArrayList<Movepuff>();
        shotgunShots = new ArrayList<Shotgun>();
        missiles = new ArrayList<Missile>();

        //Assets gameAssets = new Assets(BitmapFactory.decodeResource(getResources(),R.drawable.tilemaptest))
        Assets.init(BitmapFactory.decodeResource(getResources(),R.drawable.tilemaptest),getResources() );
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

        Bitmap wp = BitmapFactory.decodeResource(getResources(),R.drawable.weaponpanel);
        weaponPanel = new WeaponPanel(wp);
        players.add(enemy);
        players.add(player);
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

        int index = event.getActionIndex();
        int pointerId = event.getPointerId(index);
        int action = event.getActionMasked();


        int eventNumber = event.getAction();
        long downTime = event.getDownTime();
        float rawXX = event.getRawX();
        float rawYY = event.getRawY();
        float precision = event.getXPrecision();


        Log.d("eventNumber",""+eventNumber);
        Log.d("downtime","" + downTime);
        Log.d("raw X/Y",rawXX+ "/"+rawYY);
        Log.d("precision",""+precision);
        Log.d("index",""+index);
        Log.d("pointerID",""+pointerId);
        Log.d("action",""+action);

        Log.d("Event","onTouchEvent Method");

*/


        // MOTIONEVENT IS TACKING FINGERMOTION EVENTWHOU ACTION DOWN OR UP IS NOT CALLED
        // CONSIDER UPDATING FINGER POSITION TO PLAYER SET DIRECTION

       //  Log.d("HEIGHT/WIDTH", getHeight()+"/" + getWidth());
        int rawIndex = event.getActionIndex();
        float rawX = event.getRawX() *  WIDTH / getWidth();
        float rawY = event.getRawY() * HEIGHT / getHeight();

        if (player.isJumping() && !player.isParachute()) {
            player.keepDirection(rawX,rawY);
        }

        // event.getPointerId()
        switch (event.getActionMasked() ) {
/*

            case MotionEvent.ACTION_POINTER_INDEX_MASK:
                Log.d("LOL,","ACTION_POINTER_INDEX_MASK");
                return true;
            case MotionEvent.ACTION_POINTER_INDEX_SHIFT:
                Log.d("LOL,","ACTION_POINTER_INDEX_SHIFT");
                return true;

*/


            case MotionEvent.ACTION_POINTER_DOWN:
                //Log.d("YES,","YES SECOND POINTER DOWN");
                rawX = event.getX(rawIndex);
                rawY = event.getY(rawIndex);
                player.shoot(weapons,(int)rawX,(int)rawY);
/*
                long missileElapsed2 = (System.nanoTime() - missileStartTime)/1000000;

                if (missileElapsed2 > (500)) {

                    rawX = event.getX(rawIndex);
                    rawY = event.getY(rawIndex);
                    Bitmap missileBM = BitmapFactory.decodeResource(getResources(),R.drawable.missile);
                    missiles.add(player.addMissile(rawX,rawY,missileBM)) ;
                    missileStartTime = System.nanoTime();
                }*/
                return true;

            case MotionEvent.ACTION_POINTER_UP:
                //Log.d("YES,","YES SECOND POINTER UP");
              //  Bitmap missileBM = BitmapFactory.decodeResource(getResources(),R.drawable.missile);
               // missiles.add(player.addMissile(rawX,rawY,missileBM)) ;
                return true;

            case MotionEvent.ACTION_DOWN:


                if (rawX < 100 && rawY <= GamePanel.HEIGHT-150) {
                    weaponPanel.setSelectedItem(rawX,rawY);
                    weaponPanel.update();
                    return true;
                }
/*

                if (rawX < 150 && rawY > GamePanel.HEIGHT-150 ) {
                    items.add(new Item(world,0,0,150));
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
*/

                else {
                    if (player.isJumping() ) {

                        player.shoot(weapons,(int)rawX,(int)rawY);
/*
                        long missileElapsed = (System.nanoTime() - missileStartTime)/1000000;

                        if (missileElapsed > (500)) {
                            Bitmap missileBM = BitmapFactory.decodeResource(getResources(),R.drawable.missile);
                            missiles.add(player.addMissile(rawX,rawY,missileBM)) ;
                            missileStartTime = System.nanoTime();
                        }*/
                    }
                    else if (player.isAnchor() ) {

                        player.shoot(weapons,(int)rawX,(int)rawY);
      /*                  long missileElapsed = (System.nanoTime() - missileStartTime)/1000000;

                        if (missileElapsed > (500)) {
                            Bitmap missileBM = BitmapFactory.decodeResource(getResources(),R.drawable.missile);
                            missiles.add(player.addMissile(rawX,rawY,missileBM)) ;
                            missileStartTime = System.nanoTime();
                        }*/
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
                 //   Log.e("ARRAY SIZES","mis: " + missiles.size() + "  puff: " + puffs.size() + " sg: " + shotgunShots.size() );
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

            camera.centerOnGameObject();
            bg.update();
            world.update();
            player.update();
            enemy.update();
            enemy.shoot(weapons,player.getX(),player.getY() );

            for (Entity ent: players) {

                for (int i = 0 ; i < items.size() ; i++) {
                    if (collision(items.get(i),ent) ) {
                        ent.giveItem(items.get(i) );
                        items.remove(i);
                        continue;
                    }
                    items.get(i).update();
                }
            }

            for (Entity ent: players) {
                for (int i = 0 ; i < weapons.size() ; i++) {

                    int misX = weapons.get(i).getX();
                    int misY = weapons.get(i).getY();

                    int tileX = misX/Tile.TILE_WIDTH;
                    int tileY = misY/Tile.TILE_HEIGHT;
                    Tile missileTile = world.getTile(tileX,tileY);

                    if (missileTile.isSolid() ) {
                        Bitmap explosion = BitmapFactory.decodeResource(getResources(),R.drawable.explosion);
                        weaponEffects.add(new WeaponEffect(camera,explosion,misX,misY,100,100,25));

                        for (int yy = -1 ; yy < 2 ; yy++) {
                            for (int xx = -1 ; xx < 2 ; xx++) {
                                world.setTile(tileX+xx,tileY+yy,0);
                            }
                        }
                        weapons.remove(i);
                        continue;
                    }

                    // If it is Entityts own shot we dont check collision
                    if (weapons.get(i).whoShot() == ent) {
             //           Log.d("ENTITYS","OWN SHOT");
                    }
                    if (weapons.get(i).whoShot() != ent && collision(weapons.get(i), ent)) {

                        Bitmap explosion = BitmapFactory.decodeResource(getResources(),R.drawable.explosion);
                        weaponEffects.add(new WeaponEffect(camera,explosion,misX,misY,100,100,25));

                        weapons.remove(i);
                        continue;
                    }

                    if (misX < 0 || misX > world.getWorldWidth() ||
                            misY < 0 || misY > world.getWorldHeight() ) {
                        weapons.remove(i);
                        continue;
                    }

                    weapons.get(i).update();

                }
            }



/*

            for(Weapon shot:weapons) {
                shot.update();
            }
*/

/*            if (enemy.isShootingTime() ) {

                Bitmap missileBM = BitmapFactory.decodeResource(getResources(),R.drawable.missile);
                missiles.add(enemy.addMissile(player.getX(),player.getY(),missileBM) );
                enemy.setShootingTime(false);

            }*/

/*


            for (int i = 0 ; i < weapons.size() ; i++) {
                weapons.get(i).update();

                int misX = weapons.get(i).getX();
                int misY = weapons.get(i).getY();

                if (weapons.get(i).isActivated() ) {
                    for (Entity ent: players) {

                        if (collision(weapons.get(i),ent) ) {
                            Bitmap explosion = BitmapFactory.decodeResource(getResources(),R.drawable.explosion);
                            weaponEffects.add(new WeaponEffect(camera,explosion,misX,misY,100,100,25));

                            // We have to remove weapon ja apply damaga etc
                            continue;
                        }
                    }
                }
            }
*/

       /*     for (int i = 0 ; i < missiles.size() ; i++) {
                missiles.get(i).update();

                // Log.d("missiles","size: " +missiles.size() );

                // Maybe this should be center of the missile
                int misX = missiles.get(i).getX();
                int misY = missiles.get(i).getY();

                int tileX = misX/Tile.TILE_WIDTH;
                int tileY = misY/Tile.TILE_HEIGHT;

                if (missiles.get(i).isActivated() && collision(missiles.get(i),player) ) {
                   // Log.e("WE HAVE A HIT","PLAUER YES");
                    //player.setX(100);
                    //player.setY(100);
                    Bitmap explosion = BitmapFactory.decodeResource(getResources(),R.drawable.explosion);
                    weaponEffects.add(new WeaponEffect(camera,explosion,misX,misY,100,100,25));
                    missiles.remove(i);
                    continue;

                }
                if (missiles.get(i).isActivated() && collision(missiles.get(i),enemy)) {
                   // Log.e("WE HAVE A HIT","ENEMY YES");
                    //enemy.setX(100);
                   // enemy.setY(100);
                    Bitmap explosion = BitmapFactory.decodeResource(getResources(),R.drawable.explosion);
                    weaponEffects.add(new WeaponEffect(camera,explosion,misX,misY,100,100,25));
                    missiles.remove(i);
                    continue;

                }


                // CREATE RECTANGLES
                Tile missileTile = world.getTile(tileX,tileY);
                // Log.d("Tile", x+"/"+y+"  solid: " + missileTile.isSolid() + "  " + missileTile.toString());
                if (missileTile.isSolid() ) {
                   // Log.e("MISSILEHIT","SOLID");

                    Bitmap explosion = BitmapFactory.decodeResource(getResources(),R.drawable.explosion);
                    weaponEffects.add(new WeaponEffect(camera,explosion,misX,misY,100,100,25));

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
*/
            // add smoke puffs if moving and timer



            // Some puff stuff feel free to remove
            long elapsed = (System.nanoTime() - puffStartTime)/1000000;
            if (elapsed > 120  ) {

                if (player.isMoving() && !player.isJumping()) {
                    //Log.d("add puff","yes");
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

            }
/*
            for (int i = 0; i< shotgunShots.size() ; i++) {
                int tempX = shotgunShots.get(i).getX();
                int tempY = shotgunShots.get(i).getY();
                if (tempX < 0 || tempX > GamePanel.WIDTH || tempY < 0 || tempY > GamePanel.HEIGHT) {
                    shotgunShots.remove(i);
                }
            }
            for (Shotgun shot: shotgunShots) {
                shot.update();
            }*/
           // Log.d("weap size",""+weaponEffects.size());




            for (int i = 0; i < weaponEffects.size() ; i++) {
                weaponEffects.get(i).update();

                if (weaponEffects.get(i).finished() ) {
                    weaponEffects.remove(i);
                    continue;
                }

                if (!weaponEffects.get(i).isKnockBackApplied() ) {

                    for (Entity ent: players) {
                        if (collision(weaponEffects.get(i),ent)) {
                            //   Log.d("kertakerta","taas kerta");
                            int misX = weaponEffects.get(i).getCenterX();
                            int misY = weaponEffects.get(i).getCenterY();
                            ent.setKnockBack(misX, misY);
                        }
                    }
                    weaponEffects.get(i).setKnockBackApplied(true);
                    //  player.setKnockBack(misX,misY);

                }
            }

            if (items.size() == 0 || items.size() < 15 && rng.nextInt(30*30) == 0) {
                Log.d("SIZES",""+weapons.size() + " " + weaponEffects.size() + " " + items.size() + " " + puffs.size());
               // Log.d("LUCKY NUMBER","ITEM ADDED");
                items.add(new Item(world,0,0,150));
            }


/*
            for (WeaponEffect effect: weaponEffects) {
                effect.update();
            }
*/

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
           // player.draw(canvas);
            //enemy.draw(canvas);
            weaponPanel.draw(canvas);

            for (Entity ent: players) {
                ent.draw(canvas);
            }
            for (Movepuff mp: puffs) {

                mp.draw(canvas);
            }
/*

            for (Shotgun shot: shotgunShots) {
                shot.draw(canvas);
            }
            for (Missile m: missiles) {
                m.draw(canvas);
            }
*/

            for (Weapon wp: weapons) {
                wp.draw(canvas);
               // Log.d("weapons","SIZE: " + weapons.size());
            }
            for (WeaponEffect effect: weaponEffects) {
                effect.draw(canvas);
            }
            for (Item item: items) {
                item.draw(canvas);
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
