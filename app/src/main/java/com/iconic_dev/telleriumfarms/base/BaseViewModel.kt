package com.iconic_dev.telleriumfarms.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by manuelchris-ogar on 31/08/2020.
 */
open class BaseViewModel : ViewModel(){
    val _errors = MutableLiveData<String>()
    val error: LiveData<String> get() = _errors

    val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading


}