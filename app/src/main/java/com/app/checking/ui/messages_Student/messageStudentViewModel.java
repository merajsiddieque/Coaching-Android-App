package com.app.checking.ui.messages_Student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class messageStudentViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public messageStudentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}