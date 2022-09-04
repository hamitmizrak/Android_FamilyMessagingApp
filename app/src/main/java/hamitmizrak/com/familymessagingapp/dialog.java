package hamitmizrak.com.familymessagingapp;

import android.app.Activity;
import android.view.LayoutInflater;
import androidx.appcompat.app.AlertDialog;

public class dialog {

    private Activity activity;
    private AlertDialog dialog;

    public dialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_loading,null));
        builder.setCancelable(true);
        dialog= builder.create();
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
