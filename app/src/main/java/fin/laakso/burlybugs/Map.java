package fin.laakso.burlybugs;

import android.util.Log;

import java.util.Random;

public class Map {

    private int[][] tiles;
    Random rng;

    public Map() {
        rng = new Random();
    }

    public int[][] getMap() {

        int width = 5;
        int height = 5;
        tiles = new int[width][height];

        for (int x = 0 ; x < width ; x++) {
            for (int y = 0 ; y < height ; y++) {
                tiles[x][y] = 1;
            }
        }

        return tiles;
    }

    public int[][] getLevel1() {
        int width = 18;
        int height = 18;
        tiles = new int[18][];
        tiles[0] = new int[] {0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[1] = new int[] {0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[2] = new int[] {0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[3] = new int[] {0,0,0,0,0,0,0,2,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[4] = new int[] {0,0,0,0,0,0,0,2,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[5] = new int[] {0,0,0,0,0,0,0,2,0,0,0, 0,0,0,1,1,1,1,0,0,0, 0,0,0,0,0,0,3,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[6] = new int[] {0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,1,1,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[7] = new int[] {0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,1,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[8] = new int[] {0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,1,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[9] = new int[] {0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,2,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[10] = new int[] {0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,2,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[11] = new int[] {0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,2,2,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[12] = new int[] {0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,2,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[13] = new int[] {0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[14] = new int[] {0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,3,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[15] = new int[] {0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,3,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[16] = new int[] {0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0};
        tiles[17] = new int[] {0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0};

        return tiles;

    }

    public int[][] getRandomLevel() {
        int width = rng.nextInt(20)+10;
        int height = rng.nextInt(10)+10;

        tiles = new int[height][width];

        for (int y = 0; y < height ; y++) {
            for (int x = 0 ; x < width ; x++) {
                tiles[y][x] = 0;
            }
        }

        for (int floors = rng.nextInt(10); floors > 0 ; floors--) {
            int floor_y = rng.nextInt(height);
            int floor_x = rng.nextInt(width-5);
            for (int floor_length = rng.nextInt(width-floor_x); floor_length > 0; floor_length--) {
                tiles[floor_y][floor_x+floor_length]=2;
            }
        }

       tiles[2][5] = 2;
       tiles[6][10] = 1;

       return tiles;

    }

    public int getWidth() {

        Log.d("length","tile.l: " + tiles.length +"  and tile[0].leng:: " + tiles[0].length);

        return tiles[0].length;
    }

    public int getHeight() {

        return tiles.length;
    }
}
