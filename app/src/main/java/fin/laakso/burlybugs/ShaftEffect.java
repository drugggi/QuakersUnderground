package fin.laakso.burlybugs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

public class ShaftEffect extends WeaponEffect {


    private int towardsX;
    private int towardsY;
    private int update;
    private Entity whoShotThis;
    Paint paint;

    public ShaftEffect(GameCamera camera,int fromX,int fromY, int toX, int toY) {
        super.camera = camera;
        super.x = fromX;
        super.y = fromY;

        update = 0;

        this.towardsX = toX;
        this.towardsY = toY;

        super.knockBackApplied = false;

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(250);
    }


    public void setWhoShotThis(Entity ent) {
        whoShotThis = ent;

    }

    @Override
    public boolean finished() {
        if (update > 3 ) {
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
            int entY = 0, bloodDX = 0, bloodDY = 0,entDY = 0;
            int decreaseAmount = -10;

            if (x1 > maxX + 100) {
                bloodDX = -2;
            }
            else if (x1 < minX - 100){
                bloodDX = 2;

            }

            if (ent.getDY() > 0) {
                ent.setDY(0);
            }

            entY = -10;
            entDY = -5;
            ent.setDX(0);
            ent.setJumping(true);
            // Log.d("blood","entx/y" + ent.getX() + "/" + ent.getY() );
            BloodEffect newBlood = new BloodEffect(camera,ent.getX()+32,ent.getY()+16,GamePanel.rng.nextInt(7)+1);
            newBlood.setDX(5*bloodDX+GamePanel.rng.nextInt(5)-2);
            newBlood.setDY(5*bloodDY+GamePanel.rng.nextInt(5)-2);

            effects.add(newBlood );


            ent.setKnockback(0, entY, 0, entDY);
            ent.increaseHealth(decreaseAmount);

            if (ent.isDead() ) {
                bloodDX = GamePanel.rng.nextInt(10);
                bloodDY = -GamePanel.rng.nextInt(10);
                for (int i = 0 ; i < 9 ; i++) {
                    bloodDX -= 2;
                    bloodDY += 2;

                    newBlood = new BloodEffect(camera, ent.getX() + 32, ent.getY() + 16, GamePanel.rng.nextInt(7) + 1);
                    newBlood.setDX( 2* bloodDX );
                    newBlood.setDY(2*  bloodDY );
                    effects.add(newBlood );

                }
            }


        }


    }

}
