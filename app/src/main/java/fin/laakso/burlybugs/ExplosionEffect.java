package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;

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
    public void calculateKnockback(Entity ent, ArrayList<WeaponEffect> effects) {

        if (Rect.intersects(ent.getRectangle(), this.getRectangle())) {

            ent.setJumping(true);
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

            for (int i = 0; i < 7 ; i ++) {
                BloodEffect newBlood = new BloodEffect(camera, ent.getX() + GamePanel.rng.nextInt(32)+12,
                        ent.getY() + GamePanel.rng.nextInt(64), GamePanel.rng.nextInt(7) + 1);
                newBlood.setDX(dx/2 + GamePanel.rng.nextInt(5) - 2);
                newBlood.setDY(dy/2 + GamePanel.rng.nextInt(5) - 2);
                effects.add(newBlood );
            }


            ent.setKnockback(x, y, dx, dy);
            ent.increaseHealth(decreaseAmount);

            if (ent.isDead() ) {
                for (int i = 0; i < 5 ; i ++) {
                    BloodEffect newBlood = new BloodEffect(camera, ent.getX() + GamePanel.rng.nextInt(32)+12,
                            ent.getY() + GamePanel.rng.nextInt(64), GamePanel.rng.nextInt(7) + 1);
                    newBlood.setDX( GamePanel.rng.nextInt(10) - 5);
                    newBlood.setDY( GamePanel.rng.nextInt(10) - 5);
                    effects.add(newBlood );
                }
            }


        }
    }
}
