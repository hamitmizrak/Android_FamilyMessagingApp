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

public class GoogleAutActivity extends AppCompatActivity {
    //global variable
    EditText editTextGoogleMailAddressId;
    EditText editTextGooglePasswordId;
    Button buttonGoogle;

    //Firebase işlerimleri
    private FirebaseAuth firebaseAuth;

    //Firebase User
    private FirebaseUser firebaseUser;

    //user email ve password
    String userEmailAddress, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_aut);
        //start codes

        //id almak
        editTextGoogleMailAddressId = findViewById(R.id.editTextGoogleMailAddressId);
        editTextGooglePasswordId = findViewById(R.id.editTextGooglePasswordId);
        buttonGoogle = findViewById(R.id.buttonGoogle);

        //Firebase Instance
        firebaseAuth = FirebaseAuth.getInstance();

        //button OnClickListener
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Kullanıcının verilerini almak
                userEmailAddress = editTextGoogleMailAddressId.getText().toString();
                userPassword = editTextGooglePasswordId.getText().toString();
                //Sisteme yeni kullanıcı eklemek
                firebaseAuth.createUserWithEmailAndPassword(userEmailAddress, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(GoogleAutActivity.this, "Gogole hesabıyla sisteme giriş yapıldı", Toast.LENGTH_SHORT).show();
                                //Google hesabıyla giriş için
                                firebaseAuth.signInWithEmailAndPassword(userEmailAddress, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Intent adminIndent = new Intent(getApplicationContext(), AdminActivty.class);
                                        //Toast ==>  @string veri almak istiyorsak getString(R.string.stringAdi)
                                        Toast.makeText(GoogleAutActivity.this, getString(R.string.admin_redirect), Toast.LENGTH_SHORT).show();
                                        startActivity(adminIndent);
                                        // finish();
                                    } //onComplete
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(GoogleAutActivity.this, getString(R.string.login_google_faile), Toast.LENGTH_SHORT).show();
                                    }//end onFailure
                                }); //end addOnFailureListener
                            }//end onClick

                            //eğer sisteme giriş yaparken hatalar hala varsa
                            //kullanıcı yok internet yok internet yavaş
                        })//end createUserWithEmailAndPassword
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(GoogleAutActivity.this, "Google Malesef Giriş Yapılamadı", Toast.LENGTH_SHORT).show();
                            }
                        }); //end addOnFailureListener
            } //end onClick
        });
        //end codes
    }//end onCreate
}//end GoogleAutActivity