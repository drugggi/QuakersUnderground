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
    private ArrayList<Item> items;

    private ArrayList<Weapon> weapons;
    private ArrayList<Entity> players;

    Random rng;

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

         bg = new Background(BitmapFactory.decodeResource(getResources(),R.drawable.dry_soil) );
         player = new Player(BitmapFactory.decodeResource(getResources(),R.drawable.heroart),64,64,6);
         enemy = new Enemy(BitmapFactory.decodeResource(getResources(),R.drawable.heroart),64,64,6);
         weaponEffects = new ArrayList<>();
         items = new ArrayList<>();
         weapons = new ArrayList<>();
         players = new ArrayList<>();

        puffs = new ArrayList<Movepuff>();

        Assets.init(BitmapFactory.decodeResource(getResources(),R.drawable.tilemaptest),getResources() );
        camera = new GameCamera(player,0,0);
        player.setCamera(camera);

        puffStartTime = System.nanoTime();

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();
        player.setPlaying(true);
        player.setJumping(true);

        rng = new Random();
        world = new World(camera,"");
        player.setWorldObject(world);
        enemy.setWorldObject(world);

        weaponPanel = new WeaponPanel(Assets.weaponpanel);

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
        float rawX = event.getRawX() *  WIDTH / getWidth();
        float rawY = event.getRawY() * HEIGHT / getHeight();

        if (player.isJumping() && !player.isParachute()) {
            player.keepDirection(rawX,rawY);
        }

        switch (event.getActionMasked() ) {

            // If second finger touches the screen, its always for shooting purposes
            case MotionEvent.ACTION_POINTER_DOWN:

                rawX = event.getX(rawIndex);
                rawY = event.getY(rawIndex);
                player.shoot(weapons,(int)rawX,(int)rawY);
                return true;

                // in future keep shooting as long as finger stays on screen
            case MotionEvent.ACTION_POINTER_UP:

                return true;

            case MotionEvent.ACTION_DOWN:

                // Check if player is touching weapon panel or not
                if (rawX < 100 && rawY <= GamePanel.HEIGHT-150) {
                    weaponPanel.setSelectedItem(rawX,rawY);
                    weaponPanel.update();
                    return true;
                }
                else {

                    // If player is jumping or anchored to ground finger touching the screen means shooting
                    if (player.isJumping() ) {
                        player.shoot(weapons,(int)rawX,(int)rawY);
                    }
                    else if (player.isAnchor() ) {
                        player.shoot(weapons,(int)rawX,(int)rawY);
                    }

                    // Otherwise set player to move to direction where touch happened
                    else {
                        player.setDirection(rawX,rawY);
                    }

                }


                return true;


            case MotionEvent.ACTION_UP:

                if (player.isPlaying() ) {
                    player.setMoving(false);
                }
                else {
                    player.setPlaying(true);
                }

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

            // weaponEffects are added when shots are hit, and with weaponeffect we calculate damage
            // knockback etc to entities
            for (int i = 0; i < weaponEffects.size() ; i++) {
                weaponEffects.get(i).update();

                // Weaponeffect animation is played and effects calculated
                if (weaponEffects.get(i).finished() ) {
                    weaponEffects.remove(i);
                    continue;
                }

                if (!weaponEffects.get(i).isKnockBackApplied() ) {

                    for (Entity ent: players) {
                        if (collision(weaponEffects.get(i),ent) ) {
                            int misX = weaponEffects.get(i).getCenterX();
                            int misY = weaponEffects.get(i).getCenterY();
                            ent.setKnockBack(misX, misY);
                        }
                    }
                    weaponEffects.get(i).setKnockBackApplied(true);

                }
            }

            // Adding items to world randomly
            if (items.size() == 0 || items.size() < 15 && rng.nextInt(30*30) == 0) {
                Log.d("SIZES",""+weapons.size() + " " + weaponEffects.size() + " " + items.size() + " " + puffs.size());
                items.add(new Item(world,0,0,150));
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
