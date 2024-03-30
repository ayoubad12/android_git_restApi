package ma.enset.gitapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ma.enset.gitapi.model.GitUser;
import ma.enset.gitapi.model.GitUsersResponse;
import ma.enset.gitapi.service.GitRepoServiceApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editTextQuery = findViewById(R.id.editTextQuery);
        Button buttonSearch = findViewById(R.id.buttonSearch);
        ListView listViewUsers = findViewById(R.id.listViewUsers);

        List<String> data =  new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, data);
        listViewUsers.setAdapter(arrayAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editTextQuery.getText().toString();
                Log.i("clicked", query);
                GitRepoServiceApi gitRepoServiceApi = retrofit.create(GitRepoServiceApi.class);
                Call<GitUsersResponse> callGitUsers = gitRepoServiceApi.searchUsers(query); //what is this datatype ?

                callGitUsers.enqueue(new Callback<GitUsersResponse>() {
                    @Override
                    public void onResponse(Call<GitUsersResponse> call, Response<GitUsersResponse> response) {
                        Log.i("info", call.request().url().toString());
                        if(!response.isSuccessful()){
                            Log.i("indo", String.valueOf(response.code()));
                            return ;
                        }
                        GitUsersResponse gitUsersResponse = response.body();
                        for(GitUser user:gitUsersResponse.users){
                            data.add(user.login);
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<GitUsersResponse> call, Throwable t) {
                        Log.e("error", "Error");
                    }
                });
            }
        });
    }
}