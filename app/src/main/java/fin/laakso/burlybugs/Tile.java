package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Tile {



    protected Bitmap texture;
    protected final int id;

    public Tile(Bitmap texture, int id) {

        this.texture = texture;
        this.id = id;
    }

    public void update() {

    }

    public void draw(Canvas canvas, int x, int y) {
        canvas.drawBitmap(texture,x,y,null);

    }

    public int getId() {
        return id;
    }
}
