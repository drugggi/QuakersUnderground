package fin.laakso.burlybugs;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

    private int FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000/FPS;

        long totalWaitTime = 0; // STATS
        int seconds_passed = 0; // STATS

        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            // try locking the canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);

                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime)/1000000L;
            waitTime = targetTime - timeMillis;

            try {
                totalWaitTime += waitTime;
                this.sleep(waitTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == FPS) {
                averageFPS = 1000/((totalTime/frameCount)/1000000L);
                frameCount = 0;
                totalTime = 0;

                // printing some stuff for every ten second
                seconds_passed++;
                if (seconds_passed %10 == 0) {
                    Log.d("GAMELOOPSTATS", seconds_passed + "s  avgFPS: " + averageFPS + "  avg waittime/s: " + totalWaitTime/seconds_passed + "ms");
                }
            }

        }


    }

    public void setRunning(boolean b) {
        running = b;
    }



}
