package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;

import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Entity extends GameObject {
    protected int health;
    protected boolean jumping;

    protected Backpack backpack;
    protected World gameWorld;

    Entity() {
        health = 300;
        backpack = new Backpack();
    }

    public Weapon shoot() {

        return new Weapon();
    }

    public void shoot(ArrayList<Weapon> shootingWeapons,int towardsX, int towradsY) {

        long missileElapsed = (System.nanoTime() - backpack.getShotStartTime())/1000000;

        if (missileElapsed > (500)) {
            //Bitmap missileBM = Assets.missile;
           //Bitmap missileBM = BitmapFactory.decodeResource(getResources(),R.drawable.missile);

            //int directionX = (int ) playerX;// +camera.getxOffset() ;
           // int directionY = (int) playerY;//+ camera.getyOffset() ;
            //Log.d("values","x/y: "+ x + "/" + y + "   Dir: " +directionX + "/" + directionY);

            towardsX += gameWorld.getCamera().getxOffset() ;
            towradsY += gameWorld.getCamera().getyOffset() ;
            //Log.d("values","x/y: "+ x + "/" + y + "   Dir: " +directionX + "/" + directionY);

            float differenceX =  (x - towardsX);
            float differenceY = (y- towradsY);

            //Log.d("Diffs",""+differenceX+"/"+differenceY);
            double angle = atan(differenceY/differenceX);
            // angle = rng.nextGaussian()*0.5+angle;

            //Log.d("angle","deeg "+angle);

            //Log.d("angle","rad "+angle);

            int velX = (int) (50 * cos(angle));
            int velY = (int) (50 * sin(angle));
            //Log.d("velocity","velX/velY.  "+velX + "/" + velY);

            if (differenceX < 0) {
                velX = -1*velX;
                velY = -1*velY;
            }
            angle = Math.toDegrees(angle);


            //int playerAdditionX = 0,playerAdditionY = 0;
            if (differenceX < 0) {
                angle += 180;
                //playerAdditionX = 40;
            }
            if (differenceY < 0) {
                //playerAdditionY = 64;
            }

            Missile missileBM = new Missile(gameWorld.getCamera(),Assets.missile,x+16,y+16,45,15,1,13,(float)angle);
            missileBM.setVelocity(-velX/2,-velY/2);

            shootingWeapons.add(missileBM) ;
            backpack.setShotStartTime(System.nanoTime() );
        }

    }

    public void setKnockBack(int knockX, int knockY) {

        int differenceX = getCenterX() - knockX;
        int differenceY = getCenterY() - knockY;

        //Log.d("KNOCKBACK","center x/y: " + getCenterX() + "/"+getCenterY()+ " knox/y"+knockX + "/" +knockY + "  diff:" + differenceX+ "/" + differenceY);
        //Log.d("Difference","  diff:" + differenceX+ "/" + differenceY);

        health -= 100;
        if (differenceX > 0) {
            x += 10;
            dx = 20;
            health += differenceX;
        } else {
            x += -10;
            dx = -20;
            health -= differenceX;
        }

        // explosion over head, knockback downwards
        if (differenceY > 0) {
            y += 50;
            dy += 20;
            // this.jumping = true;
            health += differenceY;

        } // explosion under enemy, knockback upwards
        else {
            y += -50;
            dy += -20;
            // jumping = true;
            health -= differenceY;
        }

        // Entity dead, respawns 1200,100
        if (health <= 0) {
            x = 1200;
            y = 100;
            health = 300;
        }
    }
}
