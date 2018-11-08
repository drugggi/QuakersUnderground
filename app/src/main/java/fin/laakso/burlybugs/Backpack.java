package fin.laakso.burlybugs;

public class Backpack {

    public static final int AXE = 0;
    public static final int SHOTGUN = 1;
    public static final int NAIL_GUN = 5;
    public static final int GRENADE_LAUNCHER = 6;
    public static final int ROCKET_LAUNCHER = 7;
    public static final int LIGHTNING_GUN = 8;

    private int shotgunAmmo;
    private int rockelaunderAmmo;

    private long shotStartTime;

    public Backpack() {
        shotgunAmmo = 25;
        rockelaunderAmmo = 0;

        shotStartTime = System.nanoTime();
    }

    public long getShotStartTime() {
        return shotStartTime;
    }

    public void setShotStartTime(long shotStartTime) {
        this.shotStartTime = shotStartTime;
    }

    public int getShotgunAmmo() {
        return shotgunAmmo;
    }

    public void setShotgunAmmo(int amount) {
        shotgunAmmo = amount;
    }

    public int getRockelaunderAmmo() {
        return rockelaunderAmmo;
    }

    public void setRockelaunderAmmo(int amount) {
        rockelaunderAmmo = amount;
    }

    public void setBackpackAmmo(int[] ammo) {
        if (ammo.length == 2) {
            shotgunAmmo = ammo[0];
            rockelaunderAmmo = ammo[1];
        }
    }

}
