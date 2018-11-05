package fin.laakso.burlybugs;

import android.graphics.Canvas;

public class World {

    private int width, height;
    private int[][] tiles;

    public World(String path) {
        loadWorld(path);
    }

    public void update() {

    }

    public void draw(Canvas canvas) {

        for (int y = 0 ; y < height ; y++) {
            for (int x = 0 ; x < width ; x++) {
                if (tiles[y][x] != 0){
                    getTile(x, y).draw(canvas, x * Tile.TILE_HEIGHT, y * Tile.TILE_HEIGHT);
                }
            }
        }
    }

    public Tile getTile(int x, int y) {
        Tile t = Tile.tileTypes[tiles[y][x]];
        if (t == null) {
            return Tile.grassTile;
        }
        return t;
    }

    private void loadWorld(String path) {

        Map generateMap = new Map();
        tiles = generateMap.getRandomLevel();

        width = generateMap.getWidth();
        height = generateMap.getHeight();

    }
}
