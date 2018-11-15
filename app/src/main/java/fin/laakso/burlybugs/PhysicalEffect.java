package fin.laakso.burlybugs;

import android.graphics.Canvas;

import java.util.ArrayList;

public class PhysicalEffect extends GameObject {

    protected World gameWorld;
    protected GameCamera camera;


    public PhysicalEffect() {


    }

    public void update() {

    }


    public boolean finished() {
        return true;
    }


    public void draw(Canvas canvas) {



    }

    protected boolean checkBottomTileCollision() {
        int tileY = (y+height) / Tile.TILE_HEIGHT;

        Tile leftLegTile = gameWorld.getTile((x) / Tile.TILE_WIDTH, (y+height) / Tile.TILE_HEIGHT);
        Tile rightLegTile = gameWorld.getTile((x+width)/Tile.TILE_WIDTH, (y+height) / Tile.TILE_HEIGHT);
        // Log.d("Tile", "x/y: " + (x+width)/Tile.TILE_WIDTH + "/" + (y+height) / Tile.TILE_HEIGHT);

        if (!leftLegTile.isDestructible() || !rightLegTile.isDestructible() ) {
            dy = 0;
            //Log.d("tile","x/y: " + (x+width)+ "/" + (y+height) );
            y = tileY * Tile.TILE_HEIGHT - height;
            return true;
        }
        else if ( leftLegTile.isSolid() || rightLegTile.isSolid()) {
            dy = 0;
            return true;
            // y =
        } else {
           return false;
        }
    }



}
