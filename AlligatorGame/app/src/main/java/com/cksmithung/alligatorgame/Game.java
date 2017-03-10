package com.cksmithung.alligatorgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Game extends Activity {
    Button startButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//       //TURN TITLE OFF
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //set to full screen
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(new GamePanel(this));
        setContentView(R.layout.start_layout);
        startButton = (Button) findViewById(R.id.startButton);

        startButton.setOnClickListener(start);






    }

    View.OnClickListener start = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            Intent goToGame = new Intent(getApplicationContext(), StartMenu.class);

            startActivity(goToGame);
        }
    };

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_game, menu);
//        return true;
//    }
//
//
//
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
