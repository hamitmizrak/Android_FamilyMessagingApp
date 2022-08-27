package hamitmizrak.com.familymessagingapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    //global variable
    EditText editTextRegisterMailAddressId;
    EditText editTextRegisterPasswordId;
    Button buttonRegister;

    //Firebase işlerimleri
    private FirebaseAuth firebaseAuth;

    //Firebase User
    private FirebaseUser firebaseUser;

    //user email ve password
    String userEmailAddress, userPassword;

    //validation Email
    private Boolean validateEmail(String val) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            editTextRegisterMailAddressId.setError("Field cannot be empty");
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!val.matches(emailPattern)) {
            editTextRegisterMailAddressId.setError("Invalid Email Adres");
            Toast.makeText(this, "Invalid Email Adres", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            editTextRegisterMailAddressId.setError(null);
            return true;
        }
    }

    //validation Password
    private Boolean validatePassword(String val) {
        //String noWhiteSpace="(?=\\s+$)";   ==> else if(!val.matches(noWhiteSpace))
        //1 tane sayı 1 tane küçük harf ve1 tane büyük harf
        //Hm3611776/.
        //"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        //Hm123456@
        String passwordVal = "^" +
                "(?=.*[0-9])" +            // En az 1 tane sayı
                "(?=.*[a-z])" +            // en az 1 tane küçük har
                "(?=.*[@#$%^&+=])" +       // at least 1 special character
                "(?=\\S+$)" +              // no white spaces
                ".{4,}" +                  // at least 4 characters
                "$";
        if (val.isEmpty()) {
            editTextRegisterPasswordId.setError("Field cannot be empty");
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!val.matches(passwordVal)) {
            editTextRegisterPasswordId.setError("Password is too weak !!");
            Toast.makeText(this, "Password is too weak", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            editTextRegisterPasswordId.setError(null);
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //start codes

        //id almak
        editTextRegisterMailAddressId = findViewById(R.id.editTextRegisterMailAddressId);
        editTextRegisterPasswordId = findViewById(R.id.editTextRegisterPasswordId);
        buttonRegister = findViewById(R.id.buttonRegister);

        //Firebase Instance
        firebaseAuth = FirebaseAuth.getInstance();

        //button OnClickListener
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Kullanıcının verilerini almak
                userEmailAddress = editTextRegisterMailAddressId.getText().toString();
                userPassword = editTextRegisterPasswordId.getText().toString();
                //validation
                if(!validateEmail(userEmailAddress) || !validatePassword(userPassword)){
                    return;
                }
                //Sisteme yeni kullanıcı eklemek
                firebaseAuth.createUserWithEmailAndPassword(userEmailAddress,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                           //complete -1
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(RegisterActivity.this, "Login hesabıyla sisteme giriş yapıldı", Toast.LENGTH_SHORT).show();
                                //Register hesabıyla giriş için
                                firebaseAuth.signInWithEmailAndPassword(editTextRegisterMailAddressId.getText().toString(),
                                        editTextRegisterPasswordId.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                    //complete -2
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Intent adminIndent = new Intent(getApplicationContext(), AdminActivity.class);
                                        //Toast ==>  @string veri almak istiyorsak getString(R.string.stringAdi)
                                        Toast.makeText(RegisterActivity.this, getString(R.string.admin_redirect), Toast.LENGTH_SHORT).show();
                                        startActivity(adminIndent);
                                        // finish();
                                    } //onComplete
                                }).addOnFailureListener(new OnFailureListener() {
                                    //complete -2 Faile
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, getString(R.string.login_faile), Toast.LENGTH_SHORT).show();
                                    }//end onFailure
                                }); //end addOnFailureListener
                            }//end onClick

                            //eğer sisteme giriş yaparken hatalar hala varsa
                            //kullanıcı yok internet yok internet yavaş
                        })//end createUserWithEmailAndPassword
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "Üye Kayıt Yapılamadı", Toast.LENGTH_SHORT).show();
                            }
                        }); //end addOnFailureListener
            } //end onClick
        });
        //end codes
    }//end onCreate
}//end GoogleAutActivity