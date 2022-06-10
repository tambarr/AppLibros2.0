package com.example.applibros20.ui.delete;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DeleteViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public DeleteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Aquí van los libros para actualizar/borrar");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
