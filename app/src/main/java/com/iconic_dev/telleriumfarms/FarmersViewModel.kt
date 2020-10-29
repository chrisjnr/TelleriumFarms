package com.iconic_dev.telleriumfarms

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.iconic_dev.telleriumfarms.api.config.PrefsUser
import com.iconic_dev.telleriumfarms.db.models.Farmer
import com.iconic_dev.telleriumfarms.farmers.FarmersRepository
import com.iconic_dev.telleriumfarms.farmers.api.FarmerDataSource
import com.iconic_dev.telleriumfarms.farmers.api.FarmersDataSourceFactory
import com.iconic_dev.telleriumfarms.ui.base.UserPrefs
import com.iconic_dev.telleriumfarms.utils.getCurrentDate
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.*

/**
 * Created by manuelchris-ogar on 26/10/2020.
 */
class FarmersViewModel(var repository: FarmersRepository, val userPrefs: UserPrefs) : ViewModel() {
    private val newsDataSourceFactory: FarmersDataSourceFactory
    var listLiveData: LiveData<PagedList<Farmer>>? = null
        private set
    var storedFarmers: LiveData<PagedList<Farmer>>? = null
    var progressLoadStatus: LiveData<String> = MutableLiveData()
        private set
    private val base_url: LiveData<String> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()
    private fun initializePaging() {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10)
            .setPageSize(10).build()
        storedFarmers =
            LivePagedListBuilder(repository.getAllFarmers(), pagedListConfig)
                .build()
        listLiveData = LivePagedListBuilder(newsDataSourceFactory, pagedListConfig)
            .build()
        progressLoadStatus = Transformations.switchMap(
            newsDataSourceFactory.mutableLiveData
        ) { obj: FarmerDataSource -> obj.progressLiveStatus }

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun updateFarmer(farmer: Farmer) {
        viewModelScope.launch {
            repository.updateFarmer(farmer)
        }
    }


    fun addFarmer(farmer: Farmer) {
        viewModelScope.launch {
            farmer.dateCreated = getCurrentDate()
            repository.addFarmer(farmer)
        }
    }



    init {
        newsDataSourceFactory = FarmersDataSourceFactory(repository, compositeDisposable)
        initializePaging()
    }
}