package hamitmizrak.com.familymessagingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    //Forgot Password
    TextView buttonForgotPassword;

    //register
    TextView buttonLoginRegister;

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


    //validation Email
    private Boolean validateEmail(String val) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            editTextLoginMailAddress.setError("Field cannot be empty");
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!val.matches(emailPattern)) {
            editTextLoginMailAddress.setError("Invalid Email Adres");
            Toast.makeText(this, "Invalid Email Adres", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            editTextLoginMailAddress.setError(null);
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
            editTextLoginPassword.setError("Field cannot be empty");
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();

            return false;
        } else if (!val.matches(passwordVal)) {
            editTextLoginPassword.setError("Password is too weak !!");
            Toast.makeText(this, "Password is too weak", Toast.LENGTH_SHORT).show();

            return false;
        } else {
            editTextLoginPassword.setError(null);
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

            return true;
        }
    }


    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //onCreate Start Codes

        //forgot password id
        buttonForgotPassword = findViewById(R.id.buttonForgotPassword);

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
                    Intent adminIndent = new Intent(getApplicationContext(), AdminActivity.class);
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
                userEmailAddress = editTextLoginMailAddress.getText().toString();
                userPassword = editTextLoginPassword.getText().toString();

                //validation
                if (!validateEmail(userEmailAddress) || !validatePassword(userPassword)) {
                    return;
                }

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
            }//end buttonLoginRegister onClick
        }); // end buttonLoginRegister

        //Şifremi unuttum
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Mail Gönderilecek bilgiler
                EditText resetMail=new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog=new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Şifreyi Değiştirmek istiyor musunuz? ");
                passwordResetDialog.setMessage("Mail Adresinizi giriniz ");
                passwordResetDialog.setView(resetMail);

                //Evet Dialog
                passwordResetDialog.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail=resetMail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Mail Gönderildi", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Mail Gönderilmede bir hata meydana geldi "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }//end onFailure
                        });
                    }
                }); //end positiveButton

                passwordResetDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Mail Gönderilmedi", Toast.LENGTH_SHORT).show();
                    }
                });//end setNegativeButton
                passwordResetDialog.create().show();

            } //end onClick
        }); //end buttonForgotPassword

        //onCreate End Codes
    }//end onCreate
}//end MainActivity