package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

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

    @Override
    public void calculateKnockback(Entity ent) {

        if (Rect.intersects(ent.getRectangle(), this.getRectangle())) {

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

        }
    }
}
