package fin.laakso.burlybugs;

import android.graphics.Canvas;
import android.util.Log;

public class World {

    private int width, height;
    private int[][] tiles;
    private GameCamera camera;

    public World(GameCamera camera,String path) {
        this.camera = camera;
        loadWorld(path);
    }

    public void update() {

    }

    public void draw(Canvas canvas) {
        int xStart = Math.max(0,camera.getxOffset() / Tile.TILE_WIDTH);
        int xEnd = Math.min(width,(camera.getxOffset()+GamePanel.WIDTH) / Tile.TILE_WIDTH+1);
        int yStart = Math.max(0,camera.getyOffset() / Tile.TILE_HEIGHT);
        int yEnd = Math.min(height,(camera.getyOffset()+GamePanel.HEIGHT) / Tile.TILE_HEIGHT+1);

        for (int y = yStart ; y < yEnd ; y++) {
            for (int x = xStart ; x < xEnd ; x++) {
                if (tiles[y][x] != 0){
                    getTile(x, y).draw(canvas, x * Tile.TILE_HEIGHT - camera.getxOffset(), y * Tile.TILE_HEIGHT-camera.getyOffset());
                    //getTile(x, y).draw(canvas, x * Tile.TILE_HEIGHT , y * Tile.TILE_HEIGHT);
                }
            }
        }
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
           // Log.e("ERROR","getTile out of bounds: " + x + "/"+y);
            return Tile.grassTile;
        }

        Tile t = Tile.tileTypes[tiles[y][x]];
        if (t == null) {
            return Tile.grassTile;
        }
        return t;
    }

    public void setTile(int x, int y, int newTileId) {

        if (x < 0 || y < 0 || x >= width || y >= height) {
            Log.e("ERROR","getTile out of bounds: " + x + "/"+y);
            return;
        }
        else {
            tiles[y][x] = newTileId;
        }
    }

    private void loadWorld(String path) {

        Map generateMap = new Map();
         // tiles = generateMap.getLevel2();
        tiles = generateMap.getRandomLevel();
        width = generateMap.getWidth();
        height = generateMap.getHeight();

    }

    public GameCamera getCamera() {
        return camera;
    }

    public int getWorldHeight() {
        return height * Tile.TILE_HEIGHT;
    }

    public int getWorldWidth() {
        return width * Tile.TILE_WIDTH;
    }
}
