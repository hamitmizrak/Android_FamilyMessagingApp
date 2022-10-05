package hamitmizrak.com.familymessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MessageActivity extends AppCompatActivity {

    //global variable
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    //gelen mail address
    //opponent: karşı
    String oppenentEmailAddress;
    String myEmailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        //start codes

        //Kullanıcı Bilgileri
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        //AdminActivity.java => onlyPerson.putExtra gelen veriyi almak
        Bundle bundle= getIntent().getExtras();
        oppenentEmailAddress=bundle.getString("userMailIndentData");
        //Toast.makeText(this, oppenentEmailAddress+"", Toast.LENGTH_SHORT).show();

        myEmailAddress=firebaseAuth.getCurrentUser().getEmail();
        //Toast.makeText(this, myEmailAddress+"", Toast.LENGTH_SHORT).show();

    }//end onCreate
}//end MessageActivity