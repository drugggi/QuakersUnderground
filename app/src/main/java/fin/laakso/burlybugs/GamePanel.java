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
    public static final int HEIGHT = 720;
    public static final int MOVESPEED = 0;
    public static final int GRAVITY = -1;

    private MainThread thread;
    private Background bg;
    private Player player;
    private Enemy enemy;

    private ArrayList<Movepuff> puffs;
    private long puffStartTime;

    private ArrayList<WeaponEffect> weaponEffects;
    private ArrayList<PhysicalEffect> physicalEffects;
    private ArrayList<Item> items;

    private ArrayList<Weapon> weapons;
    private ArrayList<Entity> players;

    public final static Random rng = new Random();

    private World world;
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


        Assets.init(BitmapFactory.decodeResource(getResources(),R.drawable.tilemaptest),getResources() );

        weaponPanel = new WeaponPanel(Assets.weaponpanel);

         bg = new Background(BitmapFactory.decodeResource(getResources(),R.drawable.dry_soil) );
         player = new Player(BitmapFactory.decodeResource(getResources(),R.drawable.heroart),weaponPanel,64,64,6);
         enemy = new Enemy(BitmapFactory.decodeResource(getResources(),R.drawable.heroart),64,64,6);
         weaponEffects = new ArrayList<>();
         items = new ArrayList<>();
         weapons = new ArrayList<>();
         players = new ArrayList<>();
         physicalEffects = new ArrayList<>();

        puffs = new ArrayList<Movepuff>();
        camera = new GameCamera(player,0,0);
        player.setCamera(camera);

        puffStartTime = System.nanoTime();

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();
        player.setPlaying(true);
        player.setJumping(true);

        //rng = new Random();
        world = new World(camera,"");
        player.setWorldObject(world);
        enemy.setWorldObject(world);

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

        int rawIndex = event.getActionIndex();
       // int actionMasked = event.getActionMasked();
       // int pointerId = event.getPointerId(rawIndex);
        float rawX = event.getRawX() *  WIDTH / getWidth();
        float rawY = event.getRawY() * HEIGHT / getHeight();
        float actionRawX = event.getX(rawIndex);
        float actionRawY = event.getY(rawIndex);




        //Log.d("RAW INDEX","" + rawIndex+" actMask: " + actionMasked + "   poinerID: " +pointerId);
       // Log.d("RAWX/Y",event.getRawX() + "/" + event.getRawY());
        //Log.d("ACTIONRAWX/Y",event.getX(rawIndex) + "/" + event.getY(rawIndex));

        int count = event.getPointerCount();
        float[] x = new float[count];
        float[] y = new float[count];

        for (int i = 0; i < count; i++) {
            x[i] = event.getX(i);
            y[i] = event.getY(i);
           // Log.d("count X/y",": " + i + "   X/Y:" + x[i] +"/"+ y[i]);
        }

        if (player.isShooting() && count == 2) {
            player.shoot(weapons,(int)x[1],(int)y[1]);
        }
/*

        if (player.isJumping() && player.isShooting() && count == 1) {
            player.shoot(weapons,(int)x[0],(int)y[0]);
        }
*/

        if ( count > 0 ) {
            player.keepDirection(x[0],y[0]);
        }

        switch (event.getActionMasked() ) {

  /*          case MotionEvent.ACTION_MOVE:
                int count = event.getPointerCount();
                float[] x = new float[count];
                float[] y = new float[count];

                for (int i = 0; i < count; i++) {
                    x[i] = event.getX(i);
                    y[i] = event.getY(i);
                    Log.d("count X/y",": " + i + "   X/Y:" + x[i] +"/"+ y[i]);
                }
                return true;
*/
            // If second finger touches the screen, its always for shooting purposes
            case MotionEvent.ACTION_POINTER_DOWN:

                //rawX = event.getX(rawIndex);
                //rawY = event.getY(rawIndex);
                player.shoot(weapons,(int)actionRawX,(int)actionRawY);
                player.setShooting(true);
                return true;

                // in future keep shooting as long as finger stays on screen
            case MotionEvent.ACTION_POINTER_UP:
                player.setShooting(false);

                return true;

            case MotionEvent.ACTION_DOWN:

                // Check if player is touching weapon panel or not
                if (rawX < 100 && rawY <= GamePanel.HEIGHT-150) {
                    weaponPanel.setSelectedItem(rawX,rawY);
                    weaponPanel.update();
                    return true;
                }
                else {

                    // If player is jumping it is shooting time
                    if (player.isJumping() ) {
                        player.shoot(weapons,(int)rawX,(int)rawY);
                        player.setShooting(true);

                    }


                    // Otherwise set player to move to direction where touch happened
                    else {
                        player.setDirection(rawX,rawY);
                        player.setMoving(true);
                        player.setShooting(false);
                    }

                }


                return true;


            case MotionEvent.ACTION_UP:

                player.setMoving(false);

        /*        if (player.isPlaying() ) {
                    player.setMoving(false);
                    // player.setShooting(false);
                }
                else {
                    player.setPlaying(true);
                }*/

                return true;


            default:


        }

        return super.onTouchEvent(event);

    }

    public void update() {

        if (player.isPlaying() ) {

            // Camera follows the player
            camera.centerOnGameObject();
           // bg.update();
            world.update();

            // We have to do entitys.update() in future
            player.update();
            enemy.update();

            for (int i = 0 ; i < physicalEffects.size() ; i++ ) {
                if (physicalEffects.get(i).finished() ) {
                    physicalEffects.remove(i);
                }
                else {
                    physicalEffects.get(i).update();
                }
            }
            enemy.shoot(weapons,player.getX(),player.getY() );

            // Are items collected
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

            // Checking where shots flying around are hit, entitys or tiles atm
            for (Entity ent: players) {
                for (int i = 0 ; i < weapons.size() ; i++) {

                    weapons.get(i).collisionTiles(weapons.get(i),world, weaponEffects);
                    weapons.get(i).collisionEntities(weapons.get(i),ent, weaponEffects);

                    if (weapons.get(i).isHit() ) {
                        weapons.remove(i);
                    }
                    else {
                        weapons.get(i).update();
                    }

                    /*int misX = weapons.get(i).getX();
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


                        Bitmap explosion = BitmapFactory.decodeResource(getResources(),R.drawable.explosion);
                        weaponEffects.add(new WeaponEffect(camera,explosion,misX,misY,100,100,25));
*/


/*

                    if (misX < 0 || misX > world.getWorldWidth() ||
                            misY < 0 || misY > world.getWorldHeight() ) {
                        weapons.remove(i);
                        continue;
                    }
*/

                }
            }


/*

            // Some move puff stuff feel free to remove
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
*/

            // weaponEffects are added when shots are hit, and with weaponeffect we calculate damage
            // knockback etc to entities
            for (int i = 0; i < weaponEffects.size() ; i++) {

                weaponEffects.get(i).update();

                // Weaponeffect animation is played and effects calculated
                if (weaponEffects.get(i).finished() ) {
                    weaponEffects.remove(i);
                    continue;
                }

                // Log.d("WP","size: " + weaponEffects.size() );
                if (!weaponEffects.get(i).isKnockBackApplied() ) {

                    for (Entity ent: players) {
                        // Log.d("WP EFFECTS","SIZE: " + weaponEffects.size() + ent.getRectangle().toShortString() );
                        weaponEffects.get(i).calculateKnockback(ent,weaponEffects);

      /*
                        if (collision(weaponEffects.get(i),ent) ) {
                            int misX = weaponEffects.get(i).getCenterX();
                            int misY = weaponEffects.get(i).getCenterY();
                            ent.setKnockBack(misX, misY);
                            // ent.applyKnockBack(weaponEffects.get(i) );
                        }
                        */
                    }

                    weaponEffects.get(i).setKnockBackApplied(true);

                }
            }

            // Adding items to world randomly
            if (items.size() == 0 || items.size() < 15 && rng.nextInt(30*30) == 0) {
                Log.d("SIZES","pe: " + physicalEffects.size() + " w: "+weapons.size() + " we: " + weaponEffects.size() + " i: " + items.size() + " puf: " + puffs.size());
                items.add(new Item(world,0,0,150));
            }

            if (puffs.size() > 30) {
                puffs.clear();
            }
            for (int i = 0 ; i < weapons.size() ; i++) {
                if (weapons.get(i).isHit() ) {
                    weapons.remove(i);
                }
            }

            for (Entity ent: players) {
                if (ent.isDead() ) {

                   // Log.d("ent is ", "dead pe size: " + physicalEffects.size());

                    ent.deathAnimation(physicalEffects);
                    ent.setHealth(300);
                    ent.setX(1200);
                    ent.setY(100);
                }
            }

            if (weapons.size() > 15) {
                Log.d("WEAPONS", "SIZE: " + weapons.size());
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

           //bg.draw(canvas);
            world.draw(canvas);
            weaponPanel.draw(canvas);

            for (Entity ent: players) {
                ent.draw(canvas);
            }
            for (Movepuff mp: puffs) {
                mp.draw(canvas);
            }

            for (Weapon wp: weapons) {
                wp.draw(canvas);
            }
            for (WeaponEffect effect: weaponEffects) {
                effect.draw(canvas);
            }
            for (Item item: items) {
                item.draw(canvas);
            }

            for (PhysicalEffect pe: physicalEffects) {
                pe.draw(canvas);
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
