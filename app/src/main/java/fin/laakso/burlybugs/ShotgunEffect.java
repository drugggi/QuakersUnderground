package fin.laakso.burlybugs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class ShotgunEffect extends WeaponEffect {

    private int towardsX;
    private int towardsY;
    private int update;
    private Entity whoShotThis;
    Paint paint;

    public ShotgunEffect(GameCamera camera,int fromX,int fromY, int toX, int toY) {
        super.camera = camera;
        super.x = fromX;
        super.y = fromY;

        update = 0;

        this.towardsX = toX;
        this.towardsY = toY;

        super.knockBackApplied = false;

        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(100);
    }


    public void setWhoShotThis(Entity ent) {
        whoShotThis = ent;

    }

    @Override
    public boolean finished() {
        if (update > 10 ) {
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        update++;
    }

    @Override
    public void draw(Canvas canvas) {


         canvas.drawLine((float)x-camera.getxOffset(),(float)y-camera.getyOffset(),(float)towardsX-camera.getxOffset(),(float)towardsY-camera.getyOffset(),paint);
    }

    @Override
    public void calculateKnockback(Entity ent, ArrayList<WeaponEffect> effects) {

        //Log.d("CAlc","shotgun knockback");

        if (ent == whoShotThis) {
            // Log.d("OWN SHOT","EXIT EARLY");
            return;
        }

        int x1 = x;
        int y1 = y;
        int x2 = towardsX;
        int y2 = towardsY;

       // Log.d("X1/Y1  X2/Y2", x1 + "/" + y1 + "     " + x2 + "/" + y2);

        Rect entityRect = ent.getRectangle();

        int minX = entityRect.left;
        int maxX = entityRect.right;
        int minY = entityRect.top;
        int maxY = entityRect.bottom;
      //  Log.d("minX/maxX  minY/maxY", minX+ "/" + maxX + "     " + minY + "/" + maxY);

        if (x2 - x1 == 0) {
           // Log.e("ERROR ", "ZERO DIVISOR");
        }
        float slope = (float) (y2 - y1) / (float) (x2 - x1);

        if ((x1 <= minX && x2 <= minX) || (y1 <= minY && y2 <= minY) || (x1 >= maxX && x2 >= maxX) || (y1 >= maxY && y2 >= maxY)) {
           // Log.d("no intersect", "Fast exit");
            return;
        }



        boolean weHaveAHit = false;
        float y = slope * (minX - x1) + y1;
        if (y > minY && y < maxY) {
           // Log.d("INTERSECTS", "WE HAVE A HIT first exit y: " + y);
            weHaveAHit = true;
        }

        y = slope * (maxX - x1) + y1;
        if (y > minY && y < maxY) {
            //Log.d("INTERSECTS", "WE HAVE A HIT second exit y: " + y);
            weHaveAHit = true;
        }

        float x = (minY - y1) / slope + x1;
        if (x > minX && x < maxX) {
            //Log.d("INTERSECTS", "WE HAVE A HIT third exit x: " + x);
            weHaveAHit = true;
        }

        x = (maxY - y1) / slope + x1;
        if (x > minX && x < maxX) {
            //Log.d("INTERSECTS", "WE HAVE A HIT fourth exit x: " + x);
            weHaveAHit = true;
            // return;
        }

        if (weHaveAHit ) {
            int entX = 0, entY = 0, entDX = 0, entDY = 0;
            int decreaseAmount = -10;

            if (x1 > maxX + 100) {
                entX = -2;
                entDX = -2;
            }
            else if (x1 < minX - 100){
                entX = 2;
                entDX = 2;

            }

            if (y1 > maxY ) {
                entY = -2;
                entDY = -2;
                ent.setJumping(true);
            }
            else if (y1 < minY  ) {
                entY = 2;
                entDY = 2;
                ent.setJumping(true);
            }


            // Log.d("blood","entx/y" + ent.getX() + "/" + ent.getY() );
            BloodEffect newBlood = new BloodEffect(camera,ent.getX()+32,ent.getY()+16,GamePanel.rng.nextInt(7)+1);
            newBlood.setDX(5*entDX+GamePanel.rng.nextInt(5)-2);
            newBlood.setDY(5*entDY+GamePanel.rng.nextInt(5)-2);

            effects.add(newBlood );


            ent.setKnockback(entX, entY, entDX, entDY);
            ent.increaseHealth(decreaseAmount);

            // Entity dead, respawns 1200,100
   /*         if (ent.getHealth() <= 0) {

                CorpseEffect newCorpse = new CorpseEffect(camera,Assets.torsoanim,ent.getX(),ent.getY() );
                newCorpse.setDX(5*entDX+GamePanel.rng.nextInt(5)-2);
                newCorpse.setDY(5*entDY+GamePanel.rng.nextInt(5)-2);
                effects.add(newCorpse);
               // effects.add(new CorpseEffect(camera,Assets.torsocorpse,1200,100 ));
                ent.setX(1200);
                ent.setY(100);
                ent.setHealth(100);
            }*/
        }

/*
            int differenceX = ent.getCenterX() - this.getCenterX();
            int differenceY = ent.getCenterY() - this.getCenterY();

            int decreaseAmount = -100;
            int x = 0, y = 0, dx = 0, dy = 0;
            if (differenceX > 0) {
                x += 10;
                dx = 20;
                decreaseAmount += differenceX;
            } else {
                x += -10;
                dx = -20;
                decreaseAmount -= differenceX;
            }

            // explosion over head, knockback downwards
            if (differenceY > 0) {
                y += 50;
                dy += 20;
                // this.jumping = true;
                decreaseAmount += differenceY;

            } // explosion under enemy, knockback upwards
            else {
                y += -50;
                dy += -20;
                // jumping = true;
                decreaseAmount -= differenceY;
            }

            ent.setKnockback(x, y, dx, dy);
            ent.increaseHealth(decreaseAmount);

            // Entity dead, respawns 1200,100
            if (ent.getHealth() <= 0) {
                ent.setX(1200);
                ent.setY(100);
                ent.setHealth(300);
            }

        }*/
    }
    }

