package hamitmizrak.com.familymessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class EmailSendActivity extends AppCompatActivity {
    //global variable
    Button buttonEmailSend;

    // kime
    EditText editTextWhoEmailId;

    // Konusu
    EditText editTextEmailSubjectId;

    // İçeriği
    EditText editTextContentId;

    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_send);
        //start codes

        //id almak EditText
        editTextWhoEmailId = findViewById(R.id.editTextWhoEmailId);
        editTextEmailSubjectId = findViewById(R.id.editTextEmailSubjectId);
        editTextContentId = findViewById(R.id.editTextContentId);

        //id almak Button
        buttonEmailSend = findViewById(R.id.buttonEmailSend);

        //button setOnClickListener: Lambda expression
        buttonEmailSend.setOnClickListener(view -> {
            //Kime
            String emailSend = editTextWhoEmailId.getText().toString();

            // Başlık
            String emailSubject = editTextEmailSubjectId.getText().toString();

            //İçerik
            String emailContent = editTextContentId.getText().toString();

            //Intent
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailSend});
            intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
            intent.putExtra(Intent.EXTRA_TEXT, emailContent);

            //set type (intent)
            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Seçim: Email Client"));
        }); //end  buttonEmailSend.setOnClickListener

        //end codes
    } //end onCreate
}//EmailSendActivity