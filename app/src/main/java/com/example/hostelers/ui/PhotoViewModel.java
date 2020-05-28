package com.example.hostelers.ui;



import android.graphics.Bitmap;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;



public class PhotoViewModel extends ViewModel {
    private MutableLiveData<Bitmap> image = new MutableLiveData<>();
    public LiveData<Bitmap> getImage(){
        return image;
    }

    public void setImage(Bitmap img)  {
        image.setValue(img);
    }
}