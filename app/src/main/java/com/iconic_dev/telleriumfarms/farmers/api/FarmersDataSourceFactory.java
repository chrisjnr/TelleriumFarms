package com.iconic_dev.telleriumfarms.farmers.api;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.iconic_dev.telleriumfarms.db.models.Farmer;
import com.iconic_dev.telleriumfarms.farmers.FarmersRepository;

import io.reactivex.disposables.CompositeDisposable;


/**
 * Created by manuelchris-ogar on 24/10/2020.
 */

public class FarmersDataSourceFactory extends DataSource.Factory<Integer, Farmer> {

    private MutableLiveData<FarmerDataSource> liveData;
    private FarmersRepository repository;
    private CompositeDisposable compositeDisposable;

    public FarmersDataSourceFactory(FarmersRepository repository, CompositeDisposable compositeDisposable) {
        this.repository = repository;
        this.compositeDisposable = compositeDisposable;
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<FarmerDataSource> getMutableLiveData() {
        return liveData;
    }


    @Override
    public DataSource<Integer, Farmer> create() {
        FarmerDataSource dataSourceClass = new FarmerDataSource(repository, compositeDisposable);
        liveData.postValue(dataSourceClass);
        return dataSourceClass;
    }
}