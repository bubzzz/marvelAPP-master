package com.example.MarvelAPP;

import android.util.Log;

import com.example.MarvelAPP.model.Hero;
import com.example.MarvelAPP.model.HeroRestResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainController {
    private final MainActivity mainActivity;

    private static MainController instance = null;

    //Exemple Singleton
    public static MainController getInstance(MainActivity mainActivity){
        if(instance == null){
            instance = new MainController(mainActivity);
        }
        return instance;

    }
    public MainController(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void onCreate() {
        mainActivity.showLoader();

        //La création de ces objets, ce serait bien de ne pas
        // les réinstancier plusieurs fois.
        //--> Voir le cours de Génie Logiciel : Singleton()
        //Pour ceux qui veulent aller plus loin -> Injection de Dépendances
        //On crée un objet Gson


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //On crée un objet retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gateway.marvel.com/v1/public/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //On crée notre interface PokemonRestApi
        superheroapi superheroapi = retrofit.create(superheroapi.class);

        //On récupére un objet call.
        Call<HeroRestResponse> call = superheroapi.getListHero("1","42ca50eee7746c262aad60b6e98974cc","0adf2a0bb106a0ec8b5d57ba5ea1788b");

        call.enqueue(new Callback<HeroRestResponse>() {
            @Override
            public void onResponse(Call<HeroRestResponse> call, Response<HeroRestResponse> response) {
                HeroRestResponse HerorestResponse = response.body();
                List<Hero> listHero = HerorestResponse.getData().getResults();
                mainActivity.showList(listHero);
                mainActivity.hideLoader();
            }

            @Override
            public void onFailure(Call<HeroRestResponse> call, Throwable t) {
                Log.d("Erreur", "API ERROR");
            }
        });
    }

    public void onItemClicked(Hero itemClicked){

    }
}
