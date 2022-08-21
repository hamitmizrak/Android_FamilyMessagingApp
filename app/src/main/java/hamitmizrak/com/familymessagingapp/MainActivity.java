package hamitmizrak.com.familymessagingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //global variable

    //Login
    EditText editTextLoginMailAddress;
    EditText editTextLoginPassword;
    Button buttonLogin;

    //register

    //Google Sign In
    SignInButton buttonGoogleIndent;

    //user email ve password
    String userEmailAddress,userPassword;

    //Firebase işlerimleri
    private FirebaseAuth firebaseAuth;

    //Firebase User
    private FirebaseUser firebaseUser;

    //Firebase kullanıcı giriş/çıkış işlemlerinde
    private FirebaseAuth.AuthStateListener authStateListener;

    //firebaseAuth kullanıcıyı eklemek
    @Override
    protected void onStart() {
        super.onStart();
        //firebaseAuth kullanıcıyı eklemek
        firebaseAuth.addAuthStateListener(authStateListener);
    }//end onStart

    //firebaseAuth kullanıcıyı çıkarmak
    @Override
    protected void onStop() {
        super.onStop();
        //firebaseAuth kullanıcıyı çıkarmak
        firebaseAuth.removeAuthStateListener(authStateListener);
    } //end onStop


    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //onCreate Start Codes

        //id Almak
         editTextLoginMailAddress=findViewById(R.id.editTextLoginMailAddress);
         editTextLoginPassword=findViewById(R.id.editTextLoginPassword);
         buttonLogin=findViewById(R.id.buttonLogin);

         //google id almak
        buttonGoogleIndent=findViewById(R.id.buttonGoogleIndent);

        //Firebase Instance
        firebaseAuth=FirebaseAuth.getInstance();

        //Kullanıcının verilerini almak
        userEmailAddress=editTextLoginMailAddress.getText().toString();
        userPassword=editTextLoginPassword.getText().toString();

        //Kullanıcı sisteme giriş/çıkış yapmış mı ?
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //kullanıcı bilgisini almak
                firebaseUser=firebaseAuth.getCurrentUser();
                if(firebaseUser!=null){
                    Intent adminIndent=new Intent(getApplicationContext(),AdminActivty.class);
                    startActivity(adminIndent);
                }// end id
            }// end onAuthStateChanged
        };//end authStateListener


        //onCreate End Codes
    }//end onCreate
}//end MainActivity