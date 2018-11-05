package fin.laakso.burlybugs;

import android.graphics.Bitmap;

import static fin.laakso.burlybugs.Tile.TILE_HEIGHT;
import static fin.laakso.burlybugs.Tile.TILE_WIDTH;

public class Assets {

    //public static final int TILE_WIDTH = 32,TILE_HEIGHT = 32;
    public static Bitmap stone,dirt, grass, sand, wind, empty, sky, tree, cloud, plant;

    public static void init(Bitmap spritesheet) {
        SpriteSheet testSheet = new SpriteSheet( spritesheet);

        stone = testSheet.crop(0,0,TILE_WIDTH,TILE_HEIGHT);
        dirt = testSheet.crop(TILE_WIDTH,0,TILE_WIDTH,TILE_HEIGHT);
        grass = testSheet.crop(2*TILE_WIDTH,0,TILE_WIDTH,TILE_HEIGHT);
        sand = testSheet.crop(3*TILE_WIDTH,0,TILE_WIDTH,TILE_HEIGHT);
        wind = testSheet.crop(0,TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
        empty = testSheet.crop(TILE_WIDTH,TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
        sky = testSheet.crop(2*TILE_WIDTH,TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
        tree = testSheet.crop(3*TILE_WIDTH,TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
        cloud = testSheet.crop(0,2*TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
        plant = testSheet.crop(TILE_WIDTH,2*TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);

    }
}
