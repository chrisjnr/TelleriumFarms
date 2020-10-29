package com.iconic_dev.telleriumfarms.farmers.api;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.iconic_dev.telleriumfarms.Constant;
import com.iconic_dev.telleriumfarms.db.models.Farmer;
import com.iconic_dev.telleriumfarms.db.models.FarmerResponse;
import com.iconic_dev.telleriumfarms.farmers.FarmersRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by manuelchris-ogar on 26/10/2020.
 */
public class FarmerDataSource extends PageKeyedDataSource<Integer, Farmer> {

    private FarmersRepository repository;
    private Gson gson;
    private int sourceIndex=2;
    int lastIndex;
    private MutableLiveData<String> base_url;
    private MutableLiveData<String> progressLiveStatus;
    private CompositeDisposable compositeDisposable;

    FarmerDataSource(FarmersRepository repository, CompositeDisposable compositeDisposable) {
        this.repository = repository;
        this.compositeDisposable = compositeDisposable;
        progressLiveStatus = new MutableLiveData<>();
        GsonBuilder builder =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gson = builder.setLenient().create();
    }

    public MutableLiveData<String> getBase_url() {
        return progressLiveStatus;
    }

    public MutableLiveData<String> getProgressLiveStatus() {
        return progressLiveStatus;
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadInitial(@NonNull PageKeyedDataSource.LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Farmer> callback) {

        repository.executeNewsApi(sourceIndex)
                .doOnSubscribe(disposable ->
                {
                    compositeDisposable.add(disposable);
                    progressLiveStatus.postValue(Constant.LOADING);
                })
                .subscribe(
                        (FarmerResponse result) ->
                        {
                            progressLiveStatus.postValue(Constant.LOADED);
//                            base_url.postValue(result.getData().getImageBaseUrl());
                            lastIndex = sourceIndex;
                            sourceIndex+=2;
                            result.getData().getFarmers().get(0).base_url = result.getData().getImageBaseUrl();
                            callback.onResult(result.getData().getFarmers(), null, sourceIndex);
                        },
                        throwable ->{
                            progressLiveStatus.postValue(Constant.CHECK_NETWORK_ERROR);

                        }

                );

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Farmer> callback) {

    }

    @SuppressLint("CheckResult")
    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Farmer> callback) {

        repository.executeNewsApi(params.key)
                .doOnSubscribe(disposable ->
                {
                    compositeDisposable.add(disposable);
                    progressLiveStatus.postValue(Constant.LOADING);
                })
                .subscribe(
                        (FarmerResponse result) ->
                        {
                            progressLiveStatus.postValue(Constant.LOADED);
                            Log.e("KKKKK", lastIndex+"----");
                            List<Farmer> listToAdd = result.getData().getFarmers().subList(lastIndex,result.getData().getFarmers().size());

                            lastIndex = params.key;
                            callback.onResult(listToAdd, params.key == result.getData().getTotalRec().intValue() ? null : params.key + 2);

                        },
                        throwable ->
                                progressLiveStatus.postValue(Constant.CHECK_NETWORK_ERROR)
                );
    }
}

