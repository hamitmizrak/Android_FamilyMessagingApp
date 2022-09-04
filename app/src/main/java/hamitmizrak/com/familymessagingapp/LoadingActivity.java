package hamitmizrak.com.familymessagingapp;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadingActivity extends AppCompatActivity {
    //object variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        //start codes

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
           startActivity(new Intent(LoadingActivity.this,MainActivity.class));
            }
        },4000);

        //end codes
    }//end onCreate
}//end LoadingActivity