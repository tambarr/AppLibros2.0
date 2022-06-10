package com.example.applibros20.ui.to_read;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ToReadViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public ToReadViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Aquí van los libros para leer");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
