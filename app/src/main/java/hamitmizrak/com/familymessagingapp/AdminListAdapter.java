package hamitmizrak.com.familymessagingapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminListAdapter extends BaseAdapter {

    private Context context;
    private List<AdminListViewAdapter> listViewAdapters;

    //parametresiz constructor
    public AdminListAdapter() {
    }

    //parametreli constructor
    public AdminListAdapter(Context context, List<AdminListViewAdapter> listViewAdapters) {
        this.context = context;
        this.listViewAdapters = listViewAdapters;
    }

    @Override
    public int getCount() {
        return listViewAdapters.size();
    }

    @Override
    public Object getItem(int i) {
        return listViewAdapters.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1=View.inflate(context,R.layout.listview_layout_person,null);
        TextView textView=view1.findViewById(R.id.textViewFriendEmail);
        CircleImageView circleImageView=view1.findViewById(R.id.imageViewFriend);

        //Email Addres
        textView.setText(listViewAdapters.get(i).getEmailAddres());

        //Resim
        Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context).load(listViewAdapters.get(i).getImage()).placeholder(R.drawable.avatar2).resize(30,40).into(circleImageView);
        return view1;
    }
}
