package hamitmizrak.com.familymessagingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //global variable

    //Login
    EditText editTextLoginMailAddress;
    EditText editTextLoginPassword;
    Button buttonLogin;

    //register
    Button buttonLoginRegister;

    //user email ve password
    String userEmailAddress, userPassword;

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
        editTextLoginMailAddress = findViewById(R.id.editTextLoginMailAddress);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        //Register id almak
        buttonLoginRegister = findViewById(R.id.buttonLoginRegister);

        //Firebase Instance
        firebaseAuth = FirebaseAuth.getInstance();

        //Kullanıcının verilerini almak
       // userEmailAddress = editTextLoginMailAddress.getText().toString();
        //userPassword = editTextLoginPassword.getText().toString();

        //Kullanıcı sisteme giriş/çıkış yapmış mı ?
        //eğer sistemde Kullanıcı varsa Admin sayfasına yönlendir
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //kullanıcı bilgisini almak
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Intent adminIndent = new Intent(getApplicationContext(), AdminActivty.class);
                    //Toast ==>  @string veri almak istiyorsak getString(R.string.stringAdi)
                    Toast.makeText(MainActivity.this, getString(R.string.admin_redirect), Toast.LENGTH_SHORT).show();
                    startActivity(adminIndent);
                }// end id
            }// end onAuthStateChanged
        };//end authStateListener

        // +++++ +++ //
        //Login Button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //butona tıkladığımda inputlardan aldığım verilerle sisteme giriş yapmak
                userEmailAddress=editTextLoginMailAddress.getText().toString();
                userPassword=editTextLoginPassword.getText().toString();
                if(userPassword==null || userPassword.equals("") ||userEmailAddress==null || userEmailAddress.equals("") )
                {
                    Toast.makeText(MainActivity.this, "Lütfen boş girmeyiniz", Toast.LENGTH_SHORT).show();
                }else{
                    // addOnCompleteListener: sisteme giriş dinlemek
                    firebaseAuth.signInWithEmailAndPassword(userEmailAddress, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        //eğer sisteme giriş başarılıysa admin page yönlendirsin
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Intent adminIndent = new Intent(getApplicationContext(), AdminActivty.class);
                            //Toast ==>  @string veri almak istiyorsak getString(R.string.stringAdi)
                            Toast.makeText(MainActivity.this, getString(R.string.admin_redirect), Toast.LENGTH_SHORT).show();
                            // startActivity(adminIndent);
                        }
                        //eğer sisteme giriş yaparken herhangi bir hata alırsam examp: internet yok,kullanıcı yok
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, getString(R.string.login_faile), Toast.LENGTH_SHORT).show();
                        }//end onFailure
                    }); //end addOnFailureListener
                }

            }//end onClick
        });//end setOnClickListener

        // +++++ +++ //
        //Register Button
        buttonLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Google Sayfasına gitmek
                Intent registerIndent = new Intent(getApplicationContext(), RegisterActivity.class);
                //Toast ==>  @string veri almak istiyorsak getString(R.string.stringAdi)
                Toast.makeText(MainActivity.this, getString(R.string.register), Toast.LENGTH_SHORT).show();
                startActivity(registerIndent);
            }//end onClick
        }); // end buttonGoogleIndent

        //onCreate End Codes
    }//end onCreate
}//end MainActivity