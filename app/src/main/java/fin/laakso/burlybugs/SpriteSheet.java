package fin.laakso.burlybugs;

import android.graphics.Bitmap;

public class SpriteSheet {

    private Bitmap spritesheet;

    public SpriteSheet(Bitmap sp) {
        spritesheet = sp;
    }

    public Bitmap crop(int x,int y, int width, int height) {
        return spritesheet.createBitmap(spritesheet, x, y, width, height);

    }

    public Bitmap getSpritesheet() {
        return spritesheet;
    }
}
