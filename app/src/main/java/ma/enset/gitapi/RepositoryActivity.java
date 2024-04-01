package ma.enset.gitapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ma.enset.gitapi.model.GitRepo;
import ma.enset.gitapi.service.GitRepoServiceApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepositoryActivity extends AppCompatActivity {
    List<String> data = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repository_layout);
        //retrieve the parameter "USER_LOGIN_PARAM" that was passed by the previous Intent
        Intent intent = getIntent();
        String login = intent.getStringExtra(MainActivity.USER_LOGIN_PARAM);
        Log.i("userLoginParam", login); //debug to see if the login is assigned
        setTitle("Repositories");

        //getting UI elements
        TextView textViewLogin = findViewById(R.id.textViewLogin);
        ListView listViewRepositories = findViewById(R.id.listViewRepositories);

        //setting the adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listViewRepositories.setAdapter(arrayAdapter);
        textViewLogin.setText(login);

        //setup retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //create a Call object to store the result from
        GitRepoServiceApi gitRepoServiceApi = retrofit.create(GitRepoServiceApi.class);
        Call<List<GitRepo>> reposCall = gitRepoServiceApi.userRepositories(login);
        reposCall.enqueue(new Callback<List<GitRepo>>() {
            @Override
            public void onResponse(Call<List<GitRepo>> call, Response<List<GitRepo>> response) {
                if(!response.isSuccessful()){
                    Log.e("error", String.valueOf(response.code()));
                    return ;
                }
                List<GitRepo> gitRepos = response.body();
                for(GitRepo gitRepo:gitRepos){
                    String content="";
                    content+="id:"+gitRepo.id+"\n";
                    content+="Repo name:"+gitRepo.name+"\n";
                    content+="language:"+gitRepo.language+"\n";
                    content+="size:"+gitRepo.size+"\n";
                    data.add(content);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<GitRepo>> call, Throwable t) {

            }
        });
    }
}