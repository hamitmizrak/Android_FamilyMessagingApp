package hamitmizrak.com.familymessagingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    // wifi
    WifiManager wifiManager = null;
    // wifi i??in saya??
    static int wifiCounter = 0;

    //admin giri?? yap??ld?????? hangi kullan??c?? oldu??unu anlamak i??in
    private TextView userEmailAddressId;

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

    // Resim Galeri i??lemi i??in ekledim (Res55)
    private final static int PICTURE_CONST = 44;

    //Realtime database i??in
    private DatabaseReference databaseReferances;
    private DatabaseReference userReferances;
    private DatabaseReference mailReferances;
    private DatabaseReference imageReferances;

    // Firebase ??zerinden Select sorgusu
    private String addPersonEmail;

    //Firebase i??lerimleri
    private FirebaseAuth firebaseAuth;

    //Firebase User
    private FirebaseUser firebaseUser;

    //Firebase Storage (Resim)
    private StorageReference storageReference;

    //Firebase kullan??c?? giri??/????k???? i??lemlerinde
    private FirebaseAuth.AuthStateListener authStateListener;

    //firebaseAuth kullan??c??y?? eklemek
    @Override
    protected void onStart() {
        super.onStart();
        //firebaseAuth kullan??c??y?? eklemek
        firebaseAuth.addAuthStateListener(authStateListener);
    }//end onStart

    //firebaseAuth kullan??c??y?? ????karmak
    @Override
    protected void onStop() {
        super.onStop();
        //firebaseAuth kullan??c??y?? ????karmak
        firebaseAuth.removeAuthStateListener(authStateListener);
    } //end onStop

    //Toolbar(Menu)
    private Toolbar myToolBarId;

    //menu ??al????abilmesi i??in
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu tempMenu) {
        getMenuInflater().inflate(R.menu.admin_menu, tempMenu);
        return super.onCreateOptionsMenu(tempMenu);
    }

    //Resim Galerisi
    //Ben ekledim Resim galerisi i??in
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //start codes
        //sabit say??m??z?? veriyoruz ki resimler galeriden gelsin
        if (requestCode == PICTURE_CONST && resultCode == RESULT_OK) {
            //import android.net.Uri;
            //Resim galerindeki se??ilen resmi tutmak i??in
            Uri uri = data.getData();

            //Firebase Sitesinde pictures ad??anda klasor olu??turdum.
            //Kullan??c??dan mail adresini ald??m Firebase y??klerken mail ad??yla y??kledim
            StorageReference picturePath = storageReference.child("pictures").child(firebaseAuth.getCurrentUser().getEmail());
            picturePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                //Ba??ar??l?? resim y??klenirse
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AdminActivity.this, "Resminiz firebase Y??klendi", Toast.LENGTH_SHORT).show();
                    //resim y??klendikten sonra database yani realtime ekliyoruz.
                    //imageReferances.setValue(taskSnapshot.getStorage().getDownloadUrl().toString());

                    //resime path alabilmek download link
                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            //image t??klanabiliri url ekledim
                            imageReferances.setValue(imageUrl);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() { //Resim y??kelmede s??k??nt?? ????karsa
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminActivity.this, "Resim y??klemede s??k??nt?? olu??tu.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    //switch case i??in methods
    //wifi a?? kapat
    private void wifiOpen() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        //e??er wifi kapat??lm????sa
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
            //kapal?? olan a??mak i??in
            wifiManager.setWifiEnabled(true);
            Toast.makeText(this, "Wifi A????ld??", Toast.LENGTH_LONG).show();
        } else if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            Toast.makeText(this, "Wifi Zaten a????lm????", Toast.LENGTH_LONG).show();
        }
    }

    private void wifiClose() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        //e??er wifi a????ksa
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            //a????k olan?? kapatmak  i??in
            wifiManager.setWifiEnabled(false);
            Toast.makeText(this, "Wifi kapat??ld??", Toast.LENGTH_LONG).show();
        } else if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
            Toast.makeText(this, "Wifi zaten kapal?? ", Toast.LENGTH_LONG).show();
        }
    }

    //system information mac Addres
    @SuppressLint("MissingPermission")
    private String getMacAddress() {
        try {
            Context cntxt = getApplicationContext();
            WifiManager wifi = (WifiManager) cntxt.getSystemService(Context.WIFI_SERVICE);
            if (wifi == null) return "Failed: WiFiManager is null";

            WifiInfo info = wifi.getConnectionInfo();
            if (info == null) return "Failed: WifiInfo is null";

            return info.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Nothing";
    }

    private String getMacAddress2() {
        //Mac Addres
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        String result = "";
        try {
            WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                return null;
            }
            return wifiInfo.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return "00:00:00";
        }// end macAddress
    }


    //Menu itemlara t??klad????mda ??al????acak yer
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //sistemde bir kullan??c?? var m?? ?
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        int chooise = item.getItemId();
        switch (chooise) {
            case R.id.adminMenuPictureId:
                //Resim Galerisi ayarlar??
                Toast.makeText(getApplicationContext(), "Resim se??ildi", Toast.LENGTH_SHORT).show();
                Intent allPictures = new Intent(Intent.ACTION_PICK);
                allPictures.setType("image/*");
                //Const yap??m??z?? buraya verdik
                startActivityForResult(allPictures, PICTURE_CONST);

                //resim y??klendikten sonra realtime database i??in
                //UID
                userReferances = databaseReferances.child(firebaseAuth.getCurrentUser().getUid().toString());
                //UID alt??nda ==> mail ve userImage
                mailReferances = userReferances.child("mail_addresim");
                mailReferances.setValue(firebaseAuth.getCurrentUser().getEmail());
                imageReferances = userReferances.child("resimim");
                break;//end Resim

            case R.id.adminMenuRefleshId:
                if (firebaseUser != null) {
                    Toast.makeText(this, "Reflesh Se??ildi", Toast.LENGTH_SHORT).show();
                    Intent refleshIndent = new Intent(AdminActivity.this, AdminActivity.class);
                    startActivity(refleshIndent);
                }
                break;

            case R.id.adminMenuSettingId:
                StringBuilder stringBuilder = new StringBuilder();
                //Model ve ??retici firma(MANUFACTURER)
                stringBuilder.append("Model: " + Build.MODEL).append(" ??retici Firma: " + Build.MANUFACTURER).append(" " + getMacAddress()).append(" " + getMacAddress2());
                String allInformation = stringBuilder.toString();
                Toast.makeText(this, "Sistem bilgileri: " + allInformation, Toast.LENGTH_SHORT).show();
                break;

            case R.id.adminPersonId:
                Toast.makeText(this, "Ki??i Se??ildi", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                //Custom Dialog object
                //Android View
                View viewDialog = getLayoutInflater().inflate(R.layout.add_person, null);
                alertDialogBuilder.setView(viewDialog);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                //add_person.xml Email input verisini eri??mek
                EditText editTextAddPersonMailId = viewDialog.findViewById(R.id.editTextAddPersonMailId);
                Button buttonAddPerson = viewDialog.findViewById(R.id.buttonAddPerson);

                //Custom Dialog Button T??klad????mda
                buttonAddPerson.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!editTextAddPersonMailId.getText().toString().isEmpty()) {
                            addPersonEmail = editTextAddPersonMailId.getText().toString();
                            editTextAddPersonMailId.setText("");
                            alertDialog.hide();
                            databaseReferencesParentRoot.orderByChild("mail_addresim").equalTo(addPersonEmail)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            //System.out.println("SORGU: "+snapshot.getValue().toString());
                                            for (DataSnapshot temp : snapshot.getChildren()) {
                                                Map<String, String> personDetail = (Map<String, String>) temp.getValue();
                                                System.out.println("Mail: " + personDetail.get("mail_addresim"));
                                                System.out.println("Resim: " + personDetail.get("resimim"));
                                                DatabaseReference searchDatabaseReferences = databaseReferances.child(firebaseAuth.getCurrentUser().getUid()).child("new_user");
                                                searchDatabaseReferences.push().setValue(personDetail.get("mail_addresim"));
                                                Toast.makeText(AdminActivity.this, "L??ST: " + mListAdapter, Toast.LENGTH_SHORT).show();
                                            }
                                            Toast.makeText(AdminActivity.this, "Ki??i Ba??ar??l?? olarak Eklendi", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Arka Plan Se??ildi", Toast.LENGTH_SHORT).show();
                break;


            case R.id.adminWifiId:
                Toast.makeText(this, "Wifi Se??ildi", Toast.LENGTH_SHORT).show();
                if (wifiCounter % 2 == 0) {
                    wifiOpen();
                } else {
                    wifiClose();
                }
                wifiCounter++;
                break;

            case R.id.adminMenuChronometerId:
                Toast.makeText(this, "Kronometre Se??ildi", Toast.LENGTH_SHORT).show();
                Intent chronometerPage = new Intent(AdminActivity.this, ChronometerActivity.class);
                startActivity(chronometerPage);
                break;

            case R.id.adminMailSendId:
                Toast.makeText(this, "Mail G??nderme Alan??", Toast.LENGTH_SHORT).show();
                Intent emailSendIntent = new Intent(AdminActivity.this, EmailSendActivity.class);
                startActivity(emailSendIntent);
                break;

            case R.id.adminMenuVkiId:
                Toast.makeText(this, "V??cut Kitle indesi", Toast.LENGTH_SHORT).show();
                Intent vkiIntent = new Intent(AdminActivity.this, VkiActivity.class);
                startActivity(vkiIntent);
                break;

            case R.id.adminMenuLogoutId:
                if (firebaseUser != null) {
                    firebaseAuth.signOut();
                    Toast.makeText(this, "????k???? Yap??ld??", Toast.LENGTH_SHORT).show();
                    Intent homePage = new Intent(AdminActivity.this, MainActivity.class);
                    startActivity(homePage);
                }
                break;
        }
        //end return
        return super.onOptionsItemSelected(item);
    }//end onOptionsItemSelected

    //Google Sign Auth G??venli ????k????
    private void signOutMethod() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                //g??venli ????k???? yapt??kran sonra nere gidelim. ?
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
        //Admin page email addres added
        userEmailAddressId = findViewById(R.id.userEmailAddressId);

        //picasso
        mListAdapter = new ArrayList<>();

        //Navbar id almak Toolbar id
        myToolBarId = findViewById(R.id.myToolBarId);
        myToolBarId.setTitle("Admin");
        myToolBarId.setLogo(R.drawable.logo);
        // myToolBarId.setNavigationIcon(R.drawable.logo);
        setSupportActionBar(myToolBarId);

        //Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        databaseReferencesParentRoot = FirebaseDatabase.getInstance().getReference("users");

        //id almak Google Sign In Account
        signOutButtonId = findViewById(R.id.signOutButtonId);
        //nameGoogleLoginId = findViewById(R.id.nameGoogleLoginId);
        // emailGoogleLoginId = findViewById(R.id.emailGoogleLoginId);

        //admin sayfas??nda Kullan??c?? emaili g??stermek
        if (firebaseUser != null) {
            String email = firebaseAuth.getCurrentUser().getEmail();
            //emailGoogleLoginId.setText(email);
            String name = firebaseAuth.getCurrentUser().getDisplayName();
            //nameGoogleLoginId.setText(name);
            userEmailAddressId.setText(email);
        }

        //Firebase Instance (resim)
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Sistemdeki kullan??c?? Bilgisini almak
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

        //list view
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid());
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                DataSnapshot dataSnapshot = snapshot.child("new_user");
                for (DataSnapshot temp : dataSnapshot.getChildren()) {
                    String personName = temp.getValue(String.class);
                    System.err.println("Arkada?? Listesi: " + personName);
                    Toast.makeText(AdminActivity.this, "Arkada?? Listesi: " + personName, Toast.LENGTH_LONG).show();
                    databaseReferencesParentRoot.orderByChild("mail_addresim").equalTo(personName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot tempDetail : snapshot.getChildren()) {
                                Map<String, String> personDetail = (Map<String, String>) tempDetail.getValue();
                                System.out.println("Resim Path: " + personDetail.get("resimim"));
                                mListAdapter.add(new AdminListViewAdapter(personDetail.get("mail_addresim"), personDetail.get("resimim")));
                                Toast.makeText(AdminActivity.this, mListAdapter + " ", Toast.LENGTH_LONG).show();
                            } //end for

                            //listeyi yenilemek
                            listView.invalidateViews();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AdminActivity.this, " Resim veri g??stermede bir s??k??nt?? meydana geldi", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            //onCancelled
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminActivity.this, "internetiniz veya ba??lant?? sorunu ya????yorsunuz sonra tekrar deneyiniz", Toast.LENGTH_LONG).show();
            }
        }); // end  ref1.addValueEventListener

        //listView g??stermek
        listAdapter = new AdminListAdapter(getApplicationContext(), mListAdapter);
        listView = findViewById(R.id.listView_person);
        listView.setAdapter(listAdapter);
        listView.invalidateViews();

        //MessageActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //activity_admin.xml
                TextView myEmailAddres=findViewById(R.id.textViewFriendEmail);
                Intent onlyPerson=new Intent(AdminActivity.this,MessageActivity.class);
                onlyPerson.putExtra("userMailIndentData",myEmailAddres.getText().toString());
                startActivity(onlyPerson);
            }
        });

    } // end onCreate
}//end AdminActivity