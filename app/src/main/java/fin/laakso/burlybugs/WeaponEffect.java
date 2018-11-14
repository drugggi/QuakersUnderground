package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class WeaponEffect extends GameObject {


    protected GameCamera camera;
    protected boolean knockBackApplied;

    public WeaponEffect() {


    }


    public void update() {

    }

    public boolean isKnockBackApplied() {
        return knockBackApplied;
    }

    public void setKnockBackApplied(boolean kb) {
        knockBackApplied = kb;
    }


    public boolean finished() {
        return true;
    }


    public void draw(Canvas canvas) {



    }

    @Override
    public int getWidth() {
        return width;
    }


    public void calculateKnockback(Entity ent, ArrayList<WeaponEffect> effects) {
        //Log.d("CAlc","weapon why are we here knockback");
    }

}
