package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class CorpseEffect extends PhysicalEffect {

    int update;
    Bitmap corpse;
    int angle;

    private Animation animation = new Animation();
    private Bitmap spritesheet;
    //private World gameWorld;

    public CorpseEffect(World world, Bitmap res, int startX, int startY, int width, int height,int numFrames) {

        x = startX;
        y = startY;

        this.width = width;
        this.height = height;

        spritesheet = res;
        Bitmap[] image = new Bitmap[numFrames];


        for (int i = 0; i < image.length ; i++) {
            image[i] = Bitmap.createBitmap(spritesheet,i*width,0,width,height);
        }


        animation.setFrames(image);
        animation.setDelay(100);

        super.gameWorld = world;
        update = 90;
        super.camera = world.getCamera();
    }

    @Override
    public void update() {
        update--;

        if ( !checkBottomTileCollision() ) {

            x += dx;

            dy = dy - GamePanel.GRAVITY;
            y += dy;

            animation.update();

            update = GamePanel.rng.nextInt(30)+10;
        }

    }


    @Override
    public boolean finished() {


        if (update < 0 ) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {

        //Log.d("NEW","CORPSE draw x/y: " + x + "/" + y);


        // canvas.drawBitmap(corpse,(float)x-camera.getxOffset(),(float)y-camera.getyOffset(),null);
        canvas.drawBitmap(animation.getImage(),x-camera.getxOffset(),y-camera.getyOffset(),null);

        //canvas.drawBitmap(corpse,(float)x,(float)y,null);
    }




}
