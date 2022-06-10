package com.example.applibros20.ui.wish;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WishViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WishViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Aquí van los libros deseados");
    }

    public LiveData<String> getText() {
        return mText;
    }
}