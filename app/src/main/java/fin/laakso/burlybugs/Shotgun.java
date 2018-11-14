package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Shotgun extends Weapon {

    private int radius;
    private int towardsX;
    private int towardsY;
    //private int velocityX;
    //private int velocityY;

    public Shotgun(GameCamera camera,int startX, int startY, int r, int towardsX, int towardsY) {
        this.radius= r;
        this.towardsX = towardsX;
        this.towardsY = towardsY;

        super.x = startX;
        super.y = startY;
        super.camera = camera;
    }

    public void update() {
        // Log.d("vels",""+ velocityX +"/" + velocityY);
        //x += velocityX;
       // y += velocityY;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(255);

       // canvas.drawBitmap(animation.getImage(),x-camera.getxOffset(),y-camera.getyOffset(),null);

        //canvas.drawCircle(x-radius-camera.getxOffset(), y -radius-camera.getyOffset(), radius , paint);

       //  canvas.drawCircle(x-radius+2, y-radius-2, radius ,paint);

      //  canvas.drawCircle(x-radius+4,y-radius+1,radius,paint);

        canvas.drawLine((float)x-camera.getxOffset(),(float)y-camera.getyOffset(),(float)towardsX-camera.getxOffset(),(float)towardsY-camera.getyOffset(),paint);
    }

    @Override
    public void collisionEntities(Weapon weapon, Entity ent, ArrayList<WeaponEffect> effects) {




        if (weapon.isHit() ) {return; }


    }

    @Override
    public void collisionTiles(Weapon weapon,World gameWorld, ArrayList<WeaponEffect> effects) {
        if (weapon.isHit() ) {return; }

        float differenceX = x - towardsX;
        float differenceY = y - towardsY;
        boolean increaseRandomlyX = true;

        final int incrementAccuracy = 16;

        //Log.d("DIFFERENCES","differenceX/Y: " + differenceX + "/"+ differenceY);
        float incrementX,incrementY;
        if (Math.abs(differenceX) > Math.abs(differenceY)) {
            increaseRandomlyX = false;
            if (differenceX < 0) {
                incrementX = -incrementAccuracy;
            }
            else {
                incrementX = incrementAccuracy;
            }

            if (differenceX == 0) {
               //Log.e("ERROR", "ZERO DIVISION");
                differenceX = 0.1f;
            }
            incrementY = differenceY * (incrementAccuracy / Math.abs(differenceX));
        }
        else {

            if (differenceY < 0) {
                incrementY = -incrementAccuracy;
            }
            else {
                incrementY = incrementAccuracy;
            }
            if (differenceY == 0) {
                //Log.e("ERROR", "ZERO DIVISION");
                differenceY = 0.1f;
            }
            incrementX = differenceX * (16 / Math.abs(differenceY));
        }

        // Log.d("INCREMENT","incrementx/y: " +incrementX + "/"+incrementY);
        Random rng = new Random();

        float oldIncrementX = incrementX;
        float oldIncrementY = incrementY;

        for (int i = 0 ; i < 5 ; i++) {

            if (increaseRandomlyX) {
                incrementX = oldIncrementX + 2*rng.nextFloat();
            }
            else {
                incrementY = oldIncrementY + 2*rng.nextFloat();
            }
            // Log.d("INCREMENT","RNG incrementx/y: " +incrementX + "/"+incrementY);
            float increasedX = (x - incrementX);
            float increasedY = (y - incrementY);

            int tileX = (int) increasedX / Tile.TILE_WIDTH;
            int tileY = (int) increasedY / Tile.TILE_HEIGHT;
            Tile missileTile = gameWorld.getTile(tileX, tileY);
            int breakPoint = 0;
            // Log.d("tileX/Y",tileX+"/"+tileY+ "  ID:  " + missileTile.getId());


            while (!missileTile.isSolid()) {
                increasedX -= incrementX;
                increasedY -= incrementY;

                tileX = (int) increasedX / Tile.TILE_WIDTH;
                tileY = (int) increasedY / Tile.TILE_HEIGHT;

                missileTile = gameWorld.getTile(tileX, tileY);

                // Log.d("tileX/Y",tileX+"/"+tileY+ "  ID:  " + missileTile.getId());

                breakPoint++;
                // Log.d("tileX/Y", tileX + "/" + tileY + "  ID:  " + missileTile.getId());
                if (breakPoint > 200) {

                    Log.d("BREAKPOINT", "NO COLLISION");
                    break;

                }

            }

            // Log.d("SHOTGUN EFFECT","ADDED Tilex/y: " + tileX+ "/"+tileY );
            ShotgunEffect tempEffect = new ShotgunEffect(camera, x + 16, y + 16,
                    tileX * Tile.TILE_WIDTH, tileY * Tile.TILE_HEIGHT);
            tempEffect.setWhoShotThis(whoShot() );

            effects.add(tempEffect);

            gameWorld.setTile(tileX,tileY,0);
        }
  /*
        effects.add(new ShotgunEffect(camera, x + 16, y + 16,
                tileX*Tile.TILE_WIDTH, (tileY+1)*Tile.TILE_HEIGHT) );
        effects.add(new ShotgunEffect(camera, x + 16, y + 16,
                (tileX+1)*Tile.TILE_WIDTH, tileY*Tile.TILE_HEIGHT) );
*/
        // camera, Assets.missile, x + 16, y + 16, 45, 15, 13, (float) angle



         weapon.setHit(true);

        // We have to check which tile the shotgun hits first

    }
}
