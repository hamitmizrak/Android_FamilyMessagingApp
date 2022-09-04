package hamitmizrak.com.familymessagingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminActivity extends AppCompatActivity {
    //global variable

    // Resim Galeri işlemi için ekledim (Res55)
    private final static int PICTURE_CONST=44;

    // Firebase işlemleri
    private FirebaseAuth firebaseAuth;

    //Realtime Database için
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;
    private DatabaseReference imageReference;
    private DatabaseReference mailReference;

    // Firebase User
    private FirebaseUser firebaseUser;

    // Firebase Storage (Resim)
    private StorageReference storageReference;

    // Firebase kullanıcı giriş/çıkış işlemleri
    private FirebaseAuth.AuthStateListener authStateListener;

    // FirebaseAuth Kullanıcı Eklemek
    @Override
    protected void onStart() {
        super.onStart();
        //firebaseAuth kullanıcı eklemek
        firebaseAuth.addAuthStateListener(authStateListener);
    }// end onStart

    // FirebaseAuth Kullanıcı Çıkarmak
    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    } //end onStop

    //google Sign In
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView nameGoogleLoginId;
    TextView emailGoogleLoginId;
    Button signOutButtonId;

    //Toolbar
    private Toolbar myToolBarId;

    //menu çalışabilmesi için
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu tempMenu) {
        getMenuInflater().inflate(R.menu.admin_menu,tempMenu);
        return super.onCreateOptionsMenu(tempMenu);
    }

    //Resim Galerisi
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // start codes
        //Sabit sayımızla karşılaştırma
        if(requestCode==PICTURE_CONST && requestCode==RESULT_OK){

            //Resim galerinde resim seçtiğimizde veriyi almak
            // import android.net.Uri
            Uri uri=data.getData();

            // ###### Dikkat: Firebase sitesine gidip ==> Storage ==>  pictures adında bir klasor oluşturmalısın ########
            //Sistemdeki kullanıcını mail adresiyle Firebase eklesin
            StorageReference pictureDataPath=storageReference.child("pictures").child(firebaseAuth.getCurrentUser().getEmail());
            pictureDataPath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                //Eğer resim yükleme başarılı ise
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageReference.setValue(taskSnapshot.getStorage().getDownloadUrl().toString());
                    Toast.makeText(AdminActivity.this, "Resminiz Firebase Yüklendi", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminActivity.this, "!!! Resminiz Firebase Yüklenmedi", Toast.LENGTH_SHORT).show();
                }
            }); //addOnFailureListener
        } //end if
        // end codes
    }//onActivityResult


    //Menu itemlara tıkladğımda Çalışacak yer
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int chooise=item.getItemId();
        switch (chooise){

            case R.id.adminMenuPictureId:
                Toast.makeText(this, "Resim", Toast.LENGTH_SHORT).show();

                //resim için
                Intent allPictures=new Intent(Intent.ACTION_PICK);
                allPictures.setType("image/*");

                //const yapımızı buraya veriyoruz
                startActivityForResult(allPictures,PICTURE_CONST);

                //resimi yükledikten sonraki aşamada realtime database için
                //Sistemdeki kullanıcı UID oluştur
                userReference=databaseReference.child(firebaseAuth.getCurrentUser().getUid().toString());

                //UID altında olmasını istediğimiz veriler
                mailReference=userReference.child("mail_addresim");
                mailReference.setValue(firebaseAuth.getCurrentUser().getEmail());
                imageReference=userReference.child("resimim");
                break;

            case R.id.adminMenuRefleshId:
                Toast.makeText(this, "Reflesh Seçildi", Toast.LENGTH_SHORT).show();
                break;

            case R.id.adminMenuSettingId:
                Toast.makeText(this, "Ayarlar Seçildi", Toast.LENGTH_SHORT).show();
                break;

            case R.id.adminPersonId:
                Toast.makeText(this, "Kişi Seçildi", Toast.LENGTH_SHORT).show();
                break;

            case R.id.adminBackgroundColorId:
                Toast.makeText(this, "Arka Plan Seçildi", Toast.LENGTH_SHORT).show();
                break;

            case R.id.adminMenuLogoutId:
                Toast.makeText(this, "Çıkış Yapıldı", Toast.LENGTH_SHORT).show();
                break;
        }
        //end return
        return super.onOptionsItemSelected(item);
    }//end onOptionsItemSelected

    //Google Sign Auth Güvenli Çıkış
    private void signOutMethod(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                //güvenli çıkış yaptıkran sonra nere gidelim. ?
                startActivity(new Intent(AdminActivity.this,MainActivity.class));
            }
        });
    }

    //OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //start

        // FirebaseAuth Instance
        firebaseAuth=FirebaseAuth.getInstance();
        //Firebase kullanıcı giriş/çıkış işlemleri
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //sistemdeki Kullanıcı bilgisi almak için
                firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
            }
        };

        // Firebase Storage
        storageReference= FirebaseStorage.getInstance().getReference();

        // Realtime Database için
        databaseReference= FirebaseDatabase.getInstance().getReference("users");


        //Google Sign In Account
        signOutButtonId=findViewById(R.id.signOutButtonId);
        nameGoogleLoginId=findViewById(R.id.nameGoogleLoginId);
        emailGoogleLoginId=findViewById(R.id.emailGoogleLoginId);

        // gso ve gsc instance
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc=GoogleSignIn.getClient(this,gso);

        //button signOutButtonId
        signOutButtonId.setVisibility(View.INVISIBLE);

        //google SingInAccount
        GoogleSignInAccount signInAccount= GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount!=null){
            String name=signInAccount.getDisplayName();
            String email=signInAccount.getEmail();
            nameGoogleLoginId.setText(name);
            emailGoogleLoginId.setText(email);
            signOutButtonId.setVisibility(View.VISIBLE);
            signOutButtonId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signOutMethod();
                }
            });
        }

        //Navbar id almak Toolbar id
        myToolBarId=findViewById(R.id.myToolBarId);
        //Menu
        myToolBarId.setTitle("Admin");
        //myToolBarId.setSubtitle("Uygulama Alanı");
        myToolBarId.setLogo(R.drawable.logo);
        // myToolBarId.setNavigationIcon(R.drawable.logo);
        setSupportActionBar(myToolBarId);

    } // end onCreate
}//end AdminActivity