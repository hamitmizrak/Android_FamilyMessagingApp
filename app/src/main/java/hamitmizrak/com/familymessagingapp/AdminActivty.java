package hamitmizrak.com.familymessagingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AdminActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_activty);
    }


    //ben ekledim ==> onCreateOptionsMenu
    //menumüzün çalışması için gerekli metot
    @Override
    public boolean onCreateOptionsMenu(Menu tempMenu) {
        //menu.xml dosyasınını inflate etmek
        getMenuInflater().inflate(R.menu.admin_menu,tempMenu);
        return super.onCreateOptionsMenu(tempMenu);
    }

    //ben ekledim ==> onOptionsItemSelected
    //menüdeki itemlara tıkladığımda çalışacak metot
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int choice=item.getItemId();
        switch (choice){
            case R.id.adminMenuSettingId:
                Toast.makeText(getApplicationContext(), "Ayarlar seçildi", Toast.LENGTH_SHORT).show();
                break;
            case R.id.adminMenuPictureId:
                Toast.makeText(getApplicationContext(), "Resim seçildi", Toast.LENGTH_SHORT).show();
                break;
            case R.id.adminMenuFrienfId:
                Toast.makeText(getApplicationContext(), "Kişiler seçildi", Toast.LENGTH_SHORT).show();
                break;
            case R.id.adminMenuLogoutId:
                Toast.makeText(getApplicationContext(), "Logout seçildi", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}