package com.example.andrea.littewhale;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.btnStartNavigate);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, EnterCoordinates.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        Button button2 = (Button) findViewById(R.id.btnStartEditLocations);
        button2.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent myIntent = new Intent(MainActivity.this, EditLocations.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }
}
