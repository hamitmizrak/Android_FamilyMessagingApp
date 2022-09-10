package hamitmizrak.com.familymessagingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
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

        Chronometer chronometer=findViewById(R.id.chronometer2);

        //devam edelim
        //Menu eklenmesi
        //menu Hamburger Menu (White)


        //end code
    } // end onCreate
}//end ChronometerActivity