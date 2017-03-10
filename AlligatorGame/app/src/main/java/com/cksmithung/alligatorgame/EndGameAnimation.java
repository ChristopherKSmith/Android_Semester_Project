package com.cksmithung.alligatorgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Owner on 4/18/2016.
 */
public class EndGameAnimation {
    private int x;
    private int y;
    private int width;
    private int height;

    private Animation animation = new Animation();
    private Bitmap spritesheet;

    public EndGameAnimation(Bitmap res, int x, int y, int w, int h, int numFrames){
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
         Bitmap[] image = new Bitmap[numFrames];


        spritesheet = res;

        for (int i = 0; i<image.length; i++){
            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);

        }
        animation.setFrames(image);
        animation.setDelay(10);
    }

    public void draw(Canvas canvas){
        if(!animation.playedOne()){
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }
    }

    public void update(){

        if(!animation.playedOne()){
            animation.update();
        }
    }

    public int getHeight(){

        return height;
    }
    public void clear(){

    }
}
