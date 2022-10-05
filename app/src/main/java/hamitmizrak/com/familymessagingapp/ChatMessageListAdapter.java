package hamitmizrak.com.familymessagingapp;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class ChatMessageListAdapter extends BaseAdapter {

    //Global Variable
    private Context context;
    private List<ChatMessageAdapter> chatMessageAdapters;

    //parametreli constructor
    public ChatMessageListAdapter(Context context, List<ChatMessageAdapter> chatMessageAdapters) {
        this.context = context;
        this.chatMessageAdapters = chatMessageAdapters;
    }

    // BaseAdapter : Metotları
    //Liste eleman sayısı
    @Override
    public int getCount() {
        return chatMessageAdapters.size();
    }


    //Listedeki eleman indisi
    @Override
    public Object getItem(int i) {
        return chatMessageAdapters.get(i);
    }

    //listede eleman id
    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myViewData=View.inflate(context,R.layout.listview_message,null);
        TextView textView1=myViewData.findViewById(R.id.textViewMessageId);
        textView1.setText(chatMessageAdapters.get(i).getUserMessage());

        //validation: karşımdaki kullanıcının mesajlarını sağa almak
        if(chatMessageAdapters.get(i).getUsername()=="otherUserName"){
            textView1.setTextColor(Color.WHITE);
            textView1.setBackgroundColor(Color.BLACK);
            textView1.setTextAlignment(view.TEXT_ALIGNMENT_TEXT_END);
        }
        myViewData.setTag(chatMessageAdapters.get(i).getUserMessage());
        return myViewData;
    }
}
