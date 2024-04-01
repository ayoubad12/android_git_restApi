package ma.enset.gitapi.service;

import java.util.List;

import ma.enset.gitapi.model.GitRepo;
import ma.enset.gitapi.model.GitUsersResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitRepoServiceApi {
    @GET("search/users") //retrofit va envoyer une requete vers le hostname. Et nous allons ajouter: search/users
    public Call<GitUsersResponse> searchUsers(@Query("q") String query); //this
    @GET("users/{u}/repos")
    public Call<List<GitRepo>> userRepositories(@Path("u") String login);
}
