package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Missile extends Weapon {


    private Animation animation = new Animation();
    private Bitmap spritesheet;


    // Maybe use this to blow up a missile after certain time
    //private int activationTime;

    public Missile(GameCamera camera,Bitmap res,int x, int y, int w, int h, int numFrames, float angle) {

        super.x = x;
        super.y = y;
        width = w;
        height = h;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;
        for( int i = 0 ; i < image.length ; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, 0 , i*height,width,height);

            image[i] = Animation.rotateImage(image[i],angle);
        }

        super.camera = camera;
        animation.setFrames(image);
        animation.setDelay(100);

       // activationTime = 2;

    }
/*

    @Override
    public boolean isActivated() {
        if (activationTime < 0) {
            return true;
        }
        return false;
    }
*/

    public void update() {
    //    activationTime--;

        x += velocityX;
        y += velocityY;

        animation.update();

    }


    public void draw(Canvas canvas) {
       //Log.d("missiles x/y",""+x+"/" + y + "  offsetxy " + camera.getxOffset() + "/" + camera.getyOffset());
      // Log.d("Missile drapos",(x-camera.getxOffset())+"/"+(y-camera.getyOffset()));
        try {
            canvas.drawBitmap(animation.getImage(),x-camera.getxOffset(),y-camera.getyOffset(),null);

        } catch (Exception e) {

        }

    }
    @Override
    public int getWidth() {
        // offset slightly for more realistic collision detection
        return width - 10;
    }

    @Override
    public Rect getRectangle() {
        return new Rect(x,y,x+45,y+15);
    }

    @Override
    public void collisionEntities(Weapon weapon, Entity ent, ArrayList<WeaponEffect> effects) {

        if (weapon.isHit() ) { return; }

        boolean intersect = Rect.intersects(weapon.getRectangle(),ent.getRectangle() );

        if (weapon.whoShot() != ent && intersect ){
            // Bitmap explosion = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
            effects.add(new WeaponEffect(camera, Assets.missileexplosion, weapon.getX(),weapon.getY(), 100, 100, 25));
            weapon.setHit(true);
        }


    }

    @Override
    public void collisionTiles(Weapon weapon, World gameWorld, ArrayList<WeaponEffect> effects) {

        if (weapon.isHit() ) {return; }
        int misX = weapon.getX();
        int misY = weapon.getY();

        int tileX = misX/Tile.TILE_WIDTH;
        int tileY = misY/Tile.TILE_HEIGHT;
        Tile missileTile = gameWorld.getTile(tileX,tileY);

        if (missileTile.isSolid() ) {

            effects.add(new WeaponEffect(camera, Assets.missileexplosion, weapon.getX(),weapon.getY(), 100, 100, 25));

            for (int yy = -1 ; yy < 2 ; yy++) {
                for (int xx = -1 ; xx < 2 ; xx++) {
                    gameWorld.setTile(tileX+xx,tileY+yy,0);
                }
            }
           weapon.setHit(true);
        }

    }

}
