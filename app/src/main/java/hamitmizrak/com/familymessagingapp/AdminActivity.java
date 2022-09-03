package hamitmizrak.com.familymessagingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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
import com.google.android.gms.tasks.Task;

public class AdminActivity extends AppCompatActivity {
    //global variable

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

    //Menu itemlara tıkladğımda Çalışacak yer
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int chooise=item.getItemId();
        switch (chooise){

            case R.id.adminMenuRefleshId:
                Toast.makeText(this, "Reflesh Seçildi", Toast.LENGTH_SHORT).show();
                break;

            case R.id.adminMenuSettingId:
                Toast.makeText(this, "Ayarlar Seçildi", Toast.LENGTH_SHORT).show();
                break;

            case R.id.adminPersonId:
                Toast.makeText(this, "Kişi Seçildi", Toast.LENGTH_SHORT).show();
                break;

            case R.id.adminMenuPictureId:
                Toast.makeText(this, "Resim", Toast.LENGTH_SHORT).show();
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
    }


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //start

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


        //id almak Toolbar id
        myToolBarId=findViewById(R.id.myToolBarId);
        //Menu
        myToolBarId.setTitle("Admin");
        //myToolBarId.setSubtitle("Uygulama Alanı");
        myToolBarId.setLogo(R.drawable.logo);
        // myToolBarId.setNavigationIcon(R.drawable.logo);
        setSupportActionBar(myToolBarId);

    } // end onCreate
}//end AdminActivity