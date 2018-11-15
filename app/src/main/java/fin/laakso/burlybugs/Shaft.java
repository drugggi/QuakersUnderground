package fin.laakso.burlybugs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Shaft extends Weapon {

    public static final int SHAFT_COOLDOWNTIME = 100*1000000; //250ms

    private int towardsX;
    private int towardsY;

    public Shaft(GameCamera camera,int startX, int startY,int towardsX, int towardsY) {
        //this.radius= r;
        this.towardsX = towardsX;
        this.towardsY = towardsY;

        super.x = startX;
        super.y = startY;

        super.camera = camera;
    }

    public void update() {

    }

    public void draw(Canvas canvas) {
      // Paint paint = new Paint();
       // paint.setColor(Color.RED);
       // paint.setStyle(Paint.Style.FILL);
        //paint.setAlpha(255);

       // canvas.drawBitmap(animation.getImage(),x-camera.getxOffset(),y-camera.getyOffset(),null);

        //canvas.drawCircle(x-radius-camera.getxOffset(), y -radius-camera.getyOffset(), radius , paint);

       //  canvas.drawCircle(x-radius+2, y-radius-2, radius ,paint);

      //  canvas.drawCircle(x-radius+4,y-radius+1,radius,paint);


       // canvas.drawLine((float)x-camera.getxOffset(),(float)y-camera.getyOffset(),(float)towardsX-camera.getxOffset(),(float)towardsY-camera.getyOffset(),paint);



    }

    @Override
    public void collisionEntities(Weapon weapon, Entity ent, ArrayList<WeaponEffect> effects) {




        //  if (weapon.isHit() ) {return; }


    }

    @Override
    public void collisionTiles(Weapon weapon,World gameWorld, ArrayList<WeaponEffect> effects) {
        if (weapon.isHit() ) {return; }

        float differenceX = x - towardsX;
        float differenceY = y - towardsY;


        final int incrementAccuracy = 16;

        //Log.d("DIFFERENCES","differenceX/Y: " + differenceX + "/"+ differenceY);
        float incrementX,incrementY;
        if (Math.abs(differenceX) > Math.abs(differenceY)) {
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
                if (breakPoint > 40) {

                    Log.d("BREAKPOINT", "NO COLLISION");
                    break;

                }

            // gameWorld.setTile(tileX,tileY,0);
        }

        ShaftEffect tempEffect = new ShaftEffect(camera,x+16,y+16,
                tileX*Tile.TILE_WIDTH,tileY*Tile.TILE_HEIGHT);
            tempEffect.setWhoShotThis(whoShot() );
        effects.add(tempEffect);

        weapon.setHit(true);

    }
}
