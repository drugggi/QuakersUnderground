package fin.laakso.burlybugs;

import android.util.Log;

public class Entity extends GameObject {
    protected int health;
    protected boolean jumping;

    Entity() {
        health = 300;
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

        if (health <= 0) {
            x = 1200;
            y = 100;
            health = 300;
        }

        Log.d("Health","HEALTH: " + health);


    }


}
