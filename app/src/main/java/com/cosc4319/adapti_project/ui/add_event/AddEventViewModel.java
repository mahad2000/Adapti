package com.cosc4319.adapti_project.ui.add_event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddEventViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddEventViewModel() {
        mText = new MutableLiveData<>();
    }


    public LiveData<String> getText() {
        return mText;
    }

    public void updateText(String newText) {
        // Update the value of mText
        mText.setValue(newText);
    }
}
