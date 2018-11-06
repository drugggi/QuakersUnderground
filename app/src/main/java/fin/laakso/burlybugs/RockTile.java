package fin.laakso.burlybugs;

import android.graphics.Bitmap;


public class RockTile extends Tile {

    public RockTile( int id) {
        super(Assets.stone, id);
    }

    @Override
    public boolean isSolid() {
        return false;
    }

}
