package hamitmizrak.com.familymessagingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class TelephoneLoginActivity extends AppCompatActivity {
    //global variable
    //Login
    EditText edit_text_phoneNumberId;
    EditText edit_text_phoneCodeId;
    Button confirmationButtonId;

    //Firebase Aut
    FirebaseAuth firebaseAuth;

    //Firebase User
    FirebaseUser firebaseUser;

    //Database işlemlerinde
    DatabaseReference databaseReference;

    //recognation
    String recognationId;

     //call back Data
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbackData;

    //Eğer Sistemde Doğrulama yapılmamışsa bu metot çalışsın
    public void verificationTelephone(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(edit_text_phoneNumberId.getText().toString(),60, TimeUnit.SECONDS,this,callbackData);
    }

    //ONCREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone_login);
        //start codes

        // id almak
         edit_text_phoneNumberId=findViewById(R.id.edit_text_phoneNumberId);
         edit_text_phoneCodeId=findViewById(R.id.edit_text_phoneCodeId);
         confirmationButtonId=findViewById(R.id.confirmationButtonId);

         //Başlangıçta sadece telefon numarası görünsün.
        edit_text_phoneCodeId.setVisibility(View.INVISIBLE);

        //firebase Aut Instance
        firebaseAuth=FirebaseAuth.getInstance();

        //callback
        callbackData=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //ALTTAKININ AYNISI (44)
                firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Girişimiz başarılı
                        if(task.isSuccessful()){
                            firebaseUser=firebaseAuth.getCurrentUser();
                            databaseReference= FirebaseDatabase.getInstance().getReference().child("persons").child(firebaseUser.getUid());

                            //Kullanıcı sisteme girmişse tekrar tekrar sisteme girmesini önlemek
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        //Kullanıcı sistemde ise Admin sayfasına yönlendirmek
                                        Intent adminIndent = new Intent(getApplicationContext(), AdminActivity.class);
                                        //Toast ==>  @string veri almak istiyorsak getString(R.string.stringAdi)
                                        Toast.makeText(TelephoneLoginActivity.this, getString(R.string.admin_redirect), Toast.LENGTH_SHORT).show();
                                        startActivity(adminIndent);
                                    }else {
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("Name", "Hamit");
                                        hashMap.put("Surname", "Mızrak");
                                        hashMap.put("Photo", "");
                                        hashMap.put("Status", "Merhabalar");
                                        hashMap.put("Phone", firebaseUser.getPhoneNumber());
                                        databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    //intent Ana activity geçiş
                                                    Intent homePageActivityIndent = new Intent(getApplicationContext(), AdminActivity.class);
                                                    startActivity(homePageActivityIndent);
                                                }
                                            }
                                        });
                                    }
                                }//end onDataChange

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(TelephoneLoginActivity.this, "Login Çıkış Yaptı", Toast.LENGTH_SHORT).show();
                                }//end onCancelled
                            });//end addListenerForSingleValueEvent
                        }//end isSuccessful
                    }// end onComplete
                }); //end signInWithCredential
            } //end onVerificationCompleted

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(TelephoneLoginActivity.this, "Doğrulama Hatası", Toast.LENGTH_SHORT).show();
                Log.e("DOĞRULAMA HATASI","Doğrulama Hatası");
            }// onVerificationFailed

            //Kod gönderdiikten sonra Verification Code çalışağı alan
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                recognationId=s;
                confirmationButtonId.setText("Onaylama yapabilirsiniz");
                //Onaylama işleminden sonra : Code göster Telephone Text gizle
                edit_text_phoneNumberId.setVisibility(View.INVISIBLE);
                edit_text_phoneCodeId.setVisibility(View.VISIBLE);
            }//onCodeSent
        };//end callbackData
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        confirmationButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recognationId!=null){
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(recognationId,edit_text_phoneCodeId.getText().toString());
                    //Kimlik sistemiyle login sistemine giriş
                    ///DİKKAT ÜSTEKİNİN AYNISI(44)
                    firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Girişimiz başarılı
                            if(task.isSuccessful()){
                                firebaseUser=firebaseAuth.getCurrentUser();
                                databaseReference= FirebaseDatabase.getInstance().getReference().child("persons").child(firebaseUser.getUid());

                                //Kullanıcı sisteme girmişse tekrar tekrar sisteme girmesini önlemek
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            //Kullanıcı sistemde ise Admin sayfasına yönlendirmek
                                            Intent adminIndent = new Intent(getApplicationContext(), AdminActivity.class);
                                            //Toast ==>  @string veri almak istiyorsak getString(R.string.stringAdi)
                                            Toast.makeText(TelephoneLoginActivity.this, getString(R.string.admin_redirect), Toast.LENGTH_SHORT).show();
                                            startActivity(adminIndent);
                                        }else {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("Name", "Hamit");
                                            hashMap.put("Surname", "Mızrak");
                                            hashMap.put("Photo", "");
                                            hashMap.put("Status", "Merhabalar");
                                            hashMap.put("Phone", firebaseUser.getPhoneNumber());
                                            databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        //intent Ana activity geçiş
                                                        Intent homePageActivityIndent = new Intent(getApplicationContext(), AdminActivity.class);
                                                        startActivity(homePageActivityIndent);
                                                    }
                                                }
                                            });
                                        }
                                    }//end onDataChange

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(TelephoneLoginActivity.this, "Login Çıkış Yaptı", Toast.LENGTH_SHORT).show();
                                    }//end onCancelled
                                });//end addListenerForSingleValueEvent
                            }//end isSuccessful
                        }// end onComplete
                    }); //end signInWithCredential
                }//end if
                else{
                    verificationTelephone();
                }//end else
            }//onClick
        }); //confirmationButtonId

    } //end onCreate
} //end TelephoneLoginActivity Class