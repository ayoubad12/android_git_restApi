package ma.enset.gitapi.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ma.enset.gitapi.R;

public class UsersListViewModel extends ArrayAdapter<GitUser> {
    private List<GitUser> users;
    private int resource;
    public UsersListViewModel(@NonNull Context context, int resource, List<GitUser> data){
        super(context, resource, data);
        this.users=data;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listViewItem = convertView;
        if(listViewItem==null){
            listViewItem = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }
        CircleImageView imageViewUser = listViewItem.findViewById(R.id.imageViewUser);
        TextView textViewLogin = listViewItem.findViewById(R.id.textViewLogin);
        TextView textViewScore = listViewItem.findViewById(R.id.textViewScore);

        textViewLogin.setText(getItem(position).login); //since we already passed data to the parent in our constructor. we don't need to use: users.get(position).score ; we can use instead: getItem(position).login ;
        textViewScore.setText(String.valueOf(getItem(position).score));


        //display profile image
        try {
            URL url = new URL(getItem(position).avatarUrl);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
            imageViewUser.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listViewItem;
    }
}
