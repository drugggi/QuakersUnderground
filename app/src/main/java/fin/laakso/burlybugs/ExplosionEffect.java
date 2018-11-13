package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class ExplosionEffect extends WeaponEffect {

    private Animation animation = new Animation();
    private Bitmap spritesheet;

    public ExplosionEffect(GameCamera camera, Bitmap res, int x, int y, int w, int h, int numFrames) {

            super.x = x-50;
            super.y = y-50;
            width = w;
            height = h;

             Bitmap[] image = new Bitmap[numFrames];

              spritesheet = res;


                    // Stupid stupid stupid
                            for (int i = 0; i < 5 ; i++) {
                               for (int j = 0 ; j < 5 ; j++) {
                                       image[i] = Bitmap.createBitmap(spritesheet, j * width, i * height, width, height);
                                   }
                            }
                            knockBackApplied = false;
                       this.camera = camera;
                        animation.setFrames(image);
                        animation.setDelay(50);


        }

    @Override
    public boolean finished() {
        return animation.playedOnce();
    }

    @Override
    public void update() {
        animation.update();
    }

    @Override
    public void draw(Canvas canvas) {
        try {
                        canvas.drawBitmap(animation.getImage(),x-camera.getxOffset(),y-camera.getyOffset(),null);

                    } catch (Exception e) {

                           }

    }
}
