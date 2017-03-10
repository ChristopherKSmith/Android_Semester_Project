package com.cksmithung.alligatorgame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Owner on 3/1/2016.
 */


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;
    private MainThread thread;
    private Background bg;
    private Player player;

    private ArrayList<Fish> fish;
    private long fishStartTime;

    private long palmTreeStartTime;
    private ArrayList<PalmTree> palmTreeArray;

    private long rockStartTime;
    private ArrayList<Rock> rocks;


    private boolean newGameCreated;
    private EndGameAnimation endGameAnimation;
    private long startReset;

    private boolean reset;
    private boolean started;
    private boolean disappear;
    private int best;

    private Random rand = new Random();


    public GamePanel(Context context) {
        super(context);

        // add the callback to the surface holder to intercept events
        getHolder().addCallback(this);

        //make gamePanel Focusable so it can handle events
        setFocusable(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
            thread=null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background_02));
        player = new Player(BitmapFactory.decodeResource(getResources(),R.drawable.alligator_small),100,40,121);

        //fish = new ArrayList<Fish>();
        fish = new ArrayList<Fish>();
        fishStartTime = System.nanoTime();


        //Palm = new Array
        palmTreeArray = new ArrayList<PalmTree>();
        palmTreeStartTime= System.nanoTime();

        //rock new array
        rocks = new ArrayList<Rock>();
        rockStartTime=System.nanoTime();

        thread = new MainThread(getHolder(), this);

        // we can safely start the game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!player.getPlaying() /*&& newGameCreated && reset*/)
            {
                player.setPlaying(true);
            }
            if(player.getPlaying())
            {
                if(!started) started = true;
                reset = false;
                player.jump();

            }
        return true;
    }
        if(event.getAction()==MotionEvent.ACTION_UP){
            player.down();
            return true;
        }

        return super.onTouchEvent(event);
  }


    public void update(Canvas canvas) {
        if (player.getPlaying()) {
            bg.update();
            player.update();

            if(player.getScore()>best){
                best = player.getScore();
            }






//*****************add fish generation
        long fishElapsed = (System.nanoTime() - fishStartTime) / 1000000;

            if (fishElapsed > (500 + player.getScore())) {

                if (fish.size() == 0) {
                    // generates first fish in the middle of the screen
                    fish.add(new Fish(BitmapFactory.decodeResource(getResources(), R.drawable.fish_png), WIDTH + 10,
                            HEIGHT/2, 50, 25, player.getScore(), 41));
                } else {                 //randomly generates fish based on height
                    fish.add(new Fish(BitmapFactory.decodeResource(getResources(), R.drawable.fish_png),
                            WIDTH + 10, (int) (rand.nextDouble() * HEIGHT), 50, 25, player.getScore(), 41));


                    //reset Timer
                }

                    fishStartTime = System.nanoTime();
                }
//************************end fish generation


            //***************************************add PalmTree generation*************************************************
            long palmTreeElapsed = (System.nanoTime() - palmTreeStartTime) / 1000000;
          if (palmTreeElapsed > (2000 - player.getScore() / 4)) {

                if (palmTreeArray.size() == 0) {
                    // generates palm tree on bottom of the screen
                    palmTreeArray.add(new PalmTree(BitmapFactory.decodeResource(getResources(), R.drawable.palm_tree), WIDTH + 40,
                            (HEIGHT / 2) + 150, 65, 90, player.getScore(), 11));

                }
                     //reset Timer
                palmTreeStartTime = System.nanoTime();
             }//***************************************end Palm Tree generation**********************************************



            //***************************************add rock generation*************************************************
              long rockElapsed = (System.nanoTime() - rockStartTime) / 1000000;
            if (rockElapsed > (4000 - player.getScore() / 4)) {
                if (rocks.size() == 0) {
                    // generates rock on bottom of the screen
                    rocks.add(new Rock(BitmapFactory.decodeResource(getResources(), R.drawable.rock_png), WIDTH+40 ,
                            (HEIGHT / 2)+190 , 35, 50,player.getScore(), 11));

                }
                //reset Timer
                rockStartTime = System.nanoTime();
            }//***************************************end rock generation**********************************************





//********** when the player and fish collide the health + 5 and fish will be removed
            for (int i = 0; i < fish.size(); i++) {

                fish.get(i).update();
                if (collision(fish.get(i), player)) {
                    fish.remove(i);
                    player.addHealth();
                    break;
                }
                // removes fish if it is way off of the screen
                if (fish.get(i).getX() < -100) {
                    fish.remove(i);
                    break;
                }

            }//******************************end loop when fish hit by player

            //********** when the player and palm tree collide or health = 0 end game
            for (int i = 0; i < palmTreeArray.size(); i++) {

                palmTreeArray.get(i).update();
                if (collision(palmTreeArray.get(i), player)||player.getHealth()==0) {
                    palmTreeArray.remove(i);
                    player.resetScore();
                    player.resetHealth();
                    player.resetDYA();
                    player.setPlaying(false);
                    break;
                }
                // removes palm if it is way off of the screen
                if (palmTreeArray.get(i).getX() < -100) {
                    palmTreeArray.remove(i);
                    break;
                }

            }//******************************end loop when palm hit by player

            //********** when the player and rock collide or health = 0 end game
            for (int i = 0; i < rocks.size(); i++) {

                rocks.get(i).update();
                if (collision(rocks.get(i), player)||player.getHealth()==0) {
                    rocks.remove(i);
                    player.resetScore();
                    player.resetHealth();
                    player.resetDYA();
                    player.setPlaying(false);
                    break;
                }
                // removes palm if it is way off of the screen
                if (rocks.get(i).getX() < -100) {
                    rocks.remove(i);
                    break;
                }

            }//******************************end loop when rock hits by player



        }//**************************************end if player.getPlaying() ****************************************
            else{
//                    player.resetDYA();
                    if(!reset){
                        newGame();
                        newGameCreated = false;
                        startReset = System.nanoTime();
                        reset= true;
//                        disappear = true;
                        endGameAnimation = new EndGameAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.ouch),
                                player.getX(), player.getY()-200,250,250,11  );


                    }
                    endGameAnimation.update();



//            long resetElapsed = (System.nanoTime() - startReset)/1000000;
//
//                    if (resetElapsed> 2500 && !newGameCreated){
//                        newGame();
//                    }





                }//**************************end else in main loop




        }







    // collision method to determine if two objects collide
    public boolean collision(GameObject a, GameObject b){
        // rect method located inside of the gameobject class
        if(Rect.intersects(a.getRectangle(), b.getRectangle())){
            return true;
        }
        return false;

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);
        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            bg.draw(canvas);
            if (!disappear) {
                player.draw(canvas);
            }
            // draw fish
            for (Fish f : fish) {
                f.draw(canvas);
            }
            // draw palm
            for (PalmTree p : palmTreeArray) {
                p.draw(canvas);
            }

            // draw rock
            for (Rock r : rocks) {
                r.draw(canvas);
            }

            //Draw ouch animation
            if(started) {
                endGameAnimation.draw(canvas);
            }


            drawText(canvas);

            canvas.restoreToCount(savedState);
        }

        }
    public void newGame(){

        disappear = false;
        fish.clear();
        palmTreeArray.clear();
        player.resetScore();
        player.resetDYA();
        newGameCreated = true;
        rocks.clear();
        if(player.getScore()>best){
            best = (player.getScore());
        }



    }
    public void drawText(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Score: " + (player.getScore() ), 10, HEIGHT - 10, paint);
        canvas.drawText("Best: " + best, WIDTH - 215, HEIGHT - 10, paint);


        if(player.getPlaying()) {
            Paint paintH = new Paint();
            paintH.setColor(Color.GREEN);
            paintH.setTextSize(30);
            canvas.drawText("Health: " +player.getHealth(), (WIDTH / 2)-420, HEIGHT / 2 - 90, paintH);
        }

        if(!player.getPlaying()/*&& newGameCreated&&reset*/){
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            canvas.drawText("PRESS TO START", WIDTH / 2 - 50, HEIGHT / 2, paint1);
            paint1.setTextSize(20);

            canvas.drawText("Press to Jump.", WIDTH / 2 - 50, HEIGHT / 2 + 20, paint1);
            canvas.drawText("Eat as many Fish as Possible.", WIDTH / 2 - 50, HEIGHT / 2 + 40, paint1);
            canvas.drawText("Dodge Palm Trees and Rocks.", WIDTH/2-50, HEIGHT/2 +60, paint1);
        }

    }


}