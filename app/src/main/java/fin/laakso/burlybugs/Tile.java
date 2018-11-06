package fin.laakso.burlybugs;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Tile {

    public static final int TILE_WIDTH = 32,TILE_HEIGHT = 32;

    public static Tile[] tileTypes = new Tile[256];
    public static Tile rockTile = new RockTile(0);
    public static Tile dirtTile = new DirtTile(1);
    public static Tile grassTile = new GrassTile(2);

    protected Bitmap texture;
    protected final int id;

    public Tile(Bitmap texture, int id) {

        this.texture = texture;
        this.id = id;

        tileTypes[id] = this;
    }

    public void update() {

    }

    public void draw(Canvas canvas, int x, int y) {
        canvas.drawBitmap(texture,x,y,null);


    }

    public boolean isSolid() {
        return false;
    }

    public int getId() {
        return id;
    }


}
