package hamitmizrak.com.familymessagingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class ChronometerActivity extends AppCompatActivity {
    //globall variable
    //Toolbar(Menu)
    private Toolbar myToolBarId;

    Button chronometerStartId;
    Button chronometerPauseId;
    Button chronometerRestartId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);
        //start codes

        //Navbar id almak Toolbar myToolBarChronometerId id
        myToolBarId=findViewById(R.id.myToolBarChronometerId);
        myToolBarId.setTitle("Admin");
        myToolBarId.setLogo(R.drawable.logo);
        setSupportActionBar(myToolBarId);

        //button id almak
        chronometerStartId=findViewById(R.id.chronometerStartId);
        chronometerPauseId=findViewById(R.id.chronometerPauseId);
        chronometerRestartId=findViewById(R.id.chronometerRestartId);

        //Kronometre
        Chronometer chronometer=findViewById(R.id.chronometer2);

        chronometerStartId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.start();
            }
        });


        chronometerPauseId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.stop();
            }
        });

        chronometerRestartId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.setBase(SystemClock.elapsedRealtime());
            }
        });

        //devam edelim
        //Menu eklenmesi
        //menu Hamburger Menu (White)


        //end code
    } // end onCreate
}//end ChronometerActivity