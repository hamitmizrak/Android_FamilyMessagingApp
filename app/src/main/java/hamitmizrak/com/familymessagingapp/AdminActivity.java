package hamitmizrak.com.familymessagingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {
    //global variable

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
            case R.id.adminMenuSettingId:
                Toast.makeText(this, "Ayalar Seçildi", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //start

        //id almak Toolbar id
        myToolBarId=findViewById(R.id.myToolBarId);
        setSupportActionBar(myToolBarId);
        myToolBarId.setNavigationIcon(R.drawable.logo);



    } // end onCreate
}//end AdminActivity