package com.cksmithung.alligatorgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;


/**
 * Created by Owner on 3/1/2016.
 */
public class Player extends GameObject {
    private Bitmap spritesheet;
    private int score;
    private int dya;
    private boolean onGround = false;
         // Position of the character
    private float velocityX, velocityY;     // Velocity of the character
    private float gravity = 0.5f;           // How strong is gravity

    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;
    private int health= 1000;
    private int best;


    public Player(Bitmap res, int w, int h, int numFrames) {

        x = 150;
        y = 435;
        velocityX = 4;
        velocityY = 0;
        gravity = 0.50f;
        score = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;
        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);

        }
        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();

    }

    public void jump(){
        if(onGround)
        {
            velocityY = -12.0f;
            onGround = false;
        }

    }
    public void down(){
        if(velocityY < -6) {      // If character is still ascending in the jump
            velocityY = -6;      // Limit the speed of ascent
        }
    }



    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;

        if (elapsed > 100) {
            score++;

            startTime = System.nanoTime();
        }
        if (elapsed > 65) {
            health--;


        }




//        x += velocityX ;      // Apply horizontal velocity to X position
        y += velocityY ;      // Apply vertical velocity to X position
        velocityY += gravity ;        // Apply gravity to vertical velocity

        if(y >= 435)
        {
            y = 435;
            velocityY = 0;
            onGround = true;
        }

        if(x < 10 || x > 190) {
            velocityX *= -1;
        }
        animation.update();
    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(animation.getImage(), x, y, null);

    }
    public int getScore(){return score;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetDYA(){dya = 0;}
    public void resetScore(){score = 0;}
    public void addHealth(){health+=20;}
    public void resetHealth(){health=1000;}
    public int getHealth(){return health;}



}