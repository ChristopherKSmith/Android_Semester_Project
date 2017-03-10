package com.cksmithung.alligatorgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Owner on 3/22/2016.
 */
public class PalmTree extends GameObject {

    private int score;
    private int speed;
    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;


    public PalmTree(Bitmap res, int x, int y, int w,int h, int s, int numFrames){
        super.x = x;
        super.y = y;

        height = h;
        width = w;
        score = s;
        speed = 7;

        if(score>500){speed=10;
        }else if(score>1000){
            speed=20;
        }else if(score>1500){
            speed=30;
        }else if(score>2000){speed =40;}


        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i<image.length; i++){

            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(100);


    }



    public void update(){

        x-=speed;
        animation.update();
    }
    public void draw(Canvas canvas){

        try{
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }catch(Exception e){}
    }

    @Override
    public int getWidth(){

        return width-40;
    }
}

