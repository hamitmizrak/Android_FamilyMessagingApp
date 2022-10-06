package hamitmizrak.com.familymessagingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageActivity extends AppCompatActivity {

    //global variable

    //ListAdapter
    private ChatMessageListAdapter chatMessageListAdapter;
    private List<ChatMessageAdapter> chatMessageAdapters;
    EditText editTextMessageId;
    ImageView imageViewmessageId;
    String message;

    //Database
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //gelen mail address
    //opponent: karşı
    String oppenentEmailAddress;
    String myEmailAddress;

    //ListView
    ListView listView;
    ArrayList<String> listMessage;
    ArrayAdapter<String> arrayAdapter;
    String messageData;

    //message: veri göndermek
    String myMessage;
    String otherMessage;

    //getNowDate
    public String getNowDate(){
        Locale locale=new Locale("tr","TR");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy",locale);
        String format=simpleDateFormat.format(new Date());
        return format;
    }


    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        //start codes

        //Kullanıcı Bilgileri
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("chatMessage");

        //AdminActivity.java => onlyPerson.putExtra gelen veriyi almak
        Bundle bundle= getIntent().getExtras();
        oppenentEmailAddress=bundle.getString("userMailIndentData");
        //Toast.makeText(this, oppenentEmailAddress+"", Toast.LENGTH_SHORT).show();
        myEmailAddress=firebaseAuth.getCurrentUser().getEmail();
        //Toast.makeText(this, myEmailAddress+"", Toast.LENGTH_SHORT).show();

        //mail adresinden @' işaretinden önceki isim almak
        myMessage=myEmailAddress.substring(0,myEmailAddress.indexOf("@"));
        otherMessage=oppenentEmailAddress.substring(0,oppenentEmailAddress.indexOf("@"));

        editTextMessageId=findViewById(R.id.editTextMessageId);
        imageViewmessageId=findViewById(R.id.imageViewmessageId);
        imageViewmessageId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message=editTextMessageId.getText().toString();
                //Benim messajımsa => database kaydet
                if(myMessage.compareTo(otherMessage)>0){
                    messageData=myMessage+"&"+otherMessage;

                }else{//Karşının messajıysa => database kaydet
                    messageData=otherMessage+"&"+myMessage;
                }
                databaseReference.child(messageData).push().setValue(myEmailAddress.toLowerCase()+"\n"+message+"\n"+getNowDate());
                editTextMessageId.setText("");
            }
        }); //end  imageViewmessageId.setOnClickListener

        //list: eğer benim messajımsa sol tarafa değilse sağ tarafa ekle
        chatMessageAdapters=new ArrayList<>();
        chatMessageListAdapter=new ChatMessageListAdapter(getApplicationContext(),chatMessageAdapters);
        //list: eğer benim messajımsa sol tarafa değilse sağ tarafa ekle
        if(myMessage.compareTo(otherMessage)>0){
            messageData=myMessage+"&"+otherMessage;
        }else{//Karşının messajıysa => database kaydet
            messageData=otherMessage+"&"+myMessage;
        }

        listMessage=new ArrayList<>();
        arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listMessage);

        //sayfada messajlaştığım kişi email yukarıda olsun
        TextView textViewMessage=findViewById(R.id.textViewMailAddresMessageId);
        textViewMessage.setText(oppenentEmailAddress);

        listView=findViewById(R.id.listViewMessageId);
      /*  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView myEmailAddres=findViewById(R.id.textViewFriendEmail);
                Intent justPerson=new Intent(AdminActivity.this,  MessageActivity.class);
                justPerson.putExtra("userMailIndentData",myEmailAddres.getText().toString());
                startActivity(justPerson);
            }
        });*/

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMessage.clear();
                chatMessageAdapters.clear();

                //konuşamaları listelemek
                DataSnapshot dataSnapshot=snapshot.child(messageData);
                for(DataSnapshot temp: dataSnapshot.getChildren()){
                    String chat=temp.getValue(String.class);
                    System.out.println("Chatlers: "+chat);
                    String person=chat.substring(0,chat.indexOf("@")).trim();

                    //ben yazığım mesajlar
                    if(person.compareTo(myEmailAddress)==0){
                        chatMessageAdapters.add(new ChatMessageAdapter("myUserName",chat));
                    }else{
                        chatMessageAdapters.add(new ChatMessageAdapter("otherUserName",chat));
                    }
                } // end for
                listView.setAdapter(chatMessageListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MessageActivity.this, "Messag göstermede sıkıntı oldu", Toast.LENGTH_SHORT).show();
            }//end onCancelled
        }); //end databaseReference.addValueEventListener



    }//end onCreate
}//end MessageActivity