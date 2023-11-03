package com.cosc4319.adapti_project.ui.add_event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddEventViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddEventViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}