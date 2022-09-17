package hamitmizrak.com.familymessagingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    //global variable

    //ListView
    private DatabaseReference databaseReferencesParentRoot;
    private ListAdapter listAdapter;
    private List<AdminListViewAdapter> mListAdapter;
    private ListView listView;

    //google Sign In
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView nameGoogleLoginId;
    TextView emailGoogleLoginId;
    Button signOutButtonId;

    // Resim Galeri işlemi için ekledim (Res55)
    private final static int PICTURE_CONST = 44;


    //Realtime database için
    private DatabaseReference databaseReferances;
    private DatabaseReference userReferances;
    private DatabaseReference mailReferances;
    private DatabaseReference imageReferances;

    // Firebase üzerinden Select sorgusu
    private String addPersonEmail;

    //Firebase işlerimleri
    private FirebaseAuth firebaseAuth;

    //Firebase User
    private FirebaseUser firebaseUser;

    //Firebase Storage (Resim)
    private StorageReference storageReference;

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

    //Toolbar(Menu)
    private Toolbar myToolBarId;

    //menu çalışabilmesi için
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu tempMenu) {
        getMenuInflater().inflate(R.menu.admin_menu, tempMenu);
        return super.onCreateOptionsMenu(tempMenu);
    }

    //Resim Galerisi
    //Ben ekledim Resim galerisi için
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //start codes
        //sabit sayımızı veriyoruz ki resimler galeriden gelsin
        if (requestCode == PICTURE_CONST && resultCode == RESULT_OK) {
            //import android.net.Uri;
            //Resim galerindeki seçilen resmi tutmak için
            Uri uri = data.getData();

            //Firebase Sitesinde pictures adıanda klasor oluşturdum.
            //Kullanıcıdan mail adresini aldım Firebase yüklerken mail adıyla yükledim
            StorageReference picturePath = storageReference.child("pictures").child(firebaseAuth.getCurrentUser().getEmail());
            picturePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                //Başarılı resim yüklenirse
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AdminActivity.this, "Resminiz firebase Yüklendi", Toast.LENGTH_SHORT).show();
                    //resim yüklendikten sonra database yani realtime ekliyoruz.
                    imageReferances.setValue(taskSnapshot.getStorage().getDownloadUrl().toString());
                }
            }).addOnFailureListener(new OnFailureListener() { //Resim yükelmede sıkıntı çıkarsa
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminActivity.this, "Resim yüklemede sıkıntı oluştu.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //Menu itemlara tıkladğımda Çalışacak yer
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //sistemde bir kullanıcı var mı ?
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        int chooise = item.getItemId();
        switch (chooise) {
            case R.id.adminMenuPictureId:
                //Resim Galerisi ayarları
                Toast.makeText(getApplicationContext(), "Resim seçildi", Toast.LENGTH_SHORT).show();
                Intent allPictures = new Intent(Intent.ACTION_PICK);
                allPictures.setType("image/*");
                //Const yapımızı buraya verdik
                startActivityForResult(allPictures, PICTURE_CONST);

                //resim yüklendikten sonra realtime database için
                //UID
                userReferances = databaseReferances.child(firebaseAuth.getCurrentUser().getUid().toString());
                //UID altında ==> mail ve userImage
                mailReferances = userReferances.child("mail_addresim");
                mailReferances.setValue(firebaseAuth.getCurrentUser().getEmail());
                imageReferances = userReferances.child("resimim");
                break;//end Resim

            case R.id.adminMenuRefleshId:
                if (firebaseUser != null) {
                    Toast.makeText(this, "Reflesh Seçildi", Toast.LENGTH_SHORT).show();
                    Intent refleshIndent = new Intent(AdminActivity.this, AdminActivity.class);
                    startActivity(refleshIndent);
                }
                break;

            case R.id.adminMenuSettingId:
                Toast.makeText(this, "Ayarlar Seçildi", Toast.LENGTH_SHORT).show();
                break;

            case R.id.adminPersonId:
                Toast.makeText(this, "Kişi Seçildi", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                //Custom Dialog object
                //Android View
                View viewDialog = getLayoutInflater().inflate(R.layout.add_person, null);
                alertDialogBuilder.setView(viewDialog);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                //add_person.xml Email input verisini erişmek
                EditText editTextAddPersonMailId = viewDialog.findViewById(R.id.editTextAddPersonMailId);
                Button buttonAddPerson = viewDialog.findViewById(R.id.buttonAddPerson);

                //Custom Dialog Button Tıkladğımda
                buttonAddPerson.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!editTextAddPersonMailId.getText().toString().isEmpty()) {
                            addPersonEmail = editTextAddPersonMailId.getText().toString();
                            editTextAddPersonMailId.setText("");
                            alertDialog.hide();

                            databaseReferances.orderByChild("mail_addresim").equalTo(addPersonEmail)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            //System.out.println("SORGU: "+snapshot.getValue().toString());

                                            for (DataSnapshot temp : snapshot.getChildren()) {
                                                Map<String, String> personDetail = (Map<String, String>) temp.getValue();
                                                System.out.println("Mail: " + personDetail.get("mail_addresim"));
                                                System.out.println("Mail: " + personDetail.get("resimim"));
                                                DatabaseReference searchDatabaseReferences = databaseReferances.child(firebaseAuth.getCurrentUser().getUid()).child("new_user");
                                                searchDatabaseReferences.push().setValue(personDetail.get("mail_addresim"));

                                                //picasso
                                                mListAdapter.add(new AdminListViewAdapter(personDetail.get("mail_addresim"),personDetail.get("resimim")));
                                                Toast.makeText(AdminActivity.this, "LİST: "+mListAdapter, Toast.LENGTH_SHORT).show();
                                            }
                                            Toast.makeText(AdminActivity.this, "Kişi Başarılı olarak Eklendi", Toast.LENGTH_SHORT).show();

                                            //listleri tazelemek
                                            listView.invalidateViews();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(AdminActivity.this, "Sorgualamada hata meydana geldi", Toast.LENGTH_SHORT).show();
                                        }
                                    });//end addListenerForSingleValueEvent

                        } //end if
                    } //end onClick
                }); //end setOnClickListener

                break;

            case R.id.adminBackgroundColorId:
                Toast.makeText(this, "Arka Plan Seçildi", Toast.LENGTH_SHORT).show();
                break;

            case R.id.adminMenuChronometerId:
                Toast.makeText(this, "Kronometre Seçildi", Toast.LENGTH_SHORT).show();
                Intent chronometerPage = new Intent(AdminActivity.this, ChronometerActivity.class);
                startActivity(chronometerPage);
                break;


            case R.id.adminMenuLogoutId:
                if (firebaseUser != null) {
                    firebaseAuth.signOut();
                    Toast.makeText(this, "Çıkış Yapıldı", Toast.LENGTH_SHORT).show();
                    Intent homePage = new Intent(AdminActivity.this, MainActivity.class);
                    startActivity(homePage);
                }
                break;
        }
        //end return
        return super.onOptionsItemSelected(item);
    }//end onOptionsItemSelected

    //Google Sign Auth Güvenli Çıkış
    private void signOutMethod() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                //güvenli çıkış yaptıkran sonra nere gidelim. ?
                startActivity(new Intent(AdminActivity.this, MainActivity.class));
            }
        });
    }

    //OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //start

        //picasso
        mListAdapter=new ArrayList<>();

        //Navbar id almak Toolbar id
        myToolBarId = findViewById(R.id.myToolBarId);
        //Menu
        myToolBarId.setTitle("Admin");
        //myToolBarId.setSubtitle("Uygulama Alanı");
        myToolBarId.setLogo(R.drawable.logo);
        // myToolBarId.setNavigationIcon(R.drawable.logo);
        setSupportActionBar(myToolBarId);

        //Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        databaseReferencesParentRoot=FirebaseDatabase.getInstance().getReference("users");

        //id almak Google Sign In Account
        signOutButtonId = findViewById(R.id.signOutButtonId);
        //nameGoogleLoginId = findViewById(R.id.nameGoogleLoginId);
        emailGoogleLoginId = findViewById(R.id.emailGoogleLoginId);

        //admin sayfasında Kullanıcı emaili göstermek
        if (firebaseUser != null) {
            String email = firebaseAuth.getCurrentUser().getEmail();
            emailGoogleLoginId.setText(email);
            String name = firebaseAuth.getCurrentUser().getDisplayName();
            //nameGoogleLoginId.setText(name);
        }

        //Firebase Instance (resim)
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Sistemdeki kullanıcı Bilgisini almak
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            }
        };

        //Firebase Storage(Resim)
        storageReference = FirebaseStorage.getInstance().getReference();

        //Realtime database  (resim)
        databaseReferances = FirebaseDatabase.getInstance().getReference("users");


        // gso ve gsc instance
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        //button signOutButtonId
        signOutButtonId.setVisibility(View.INVISIBLE);

        //google SingInAccount
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            String name = signInAccount.getDisplayName();
            String email = signInAccount.getEmail();
            //nameGoogleLoginId.setText(name);
            emailGoogleLoginId.setText(email);
            signOutButtonId.setVisibility(View.VISIBLE);
            signOutButtonId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signOutMethod();
                }
            });
        }


    } // end onCreate
}//end AdminActivity