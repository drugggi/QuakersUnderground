package fin.laakso.burlybugs;

import android.graphics.Bitmap;

public class DirtTile extends Tile {
    public DirtTile( int id) {
        super(Assets.dirt, id);
    }

    @Override
    public boolean isSolid() {return true;}

    @Override
    public boolean isDestructible() {
        return false;
    }
}
