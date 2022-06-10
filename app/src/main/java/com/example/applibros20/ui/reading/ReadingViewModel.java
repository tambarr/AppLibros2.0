package com.example.applibros20.ui.reading;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReadingViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ReadingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Aquí van los libros que se están leyendo");
    }

    public LiveData<String> getText() {
        return mText;
    }
}