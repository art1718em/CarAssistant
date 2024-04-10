package com.example.carassistant.ui.view;

import android.os.Bundle;

public class CarBundle {

    private static CarBundle carBundle;

    private Bundle bundle;
    private CarBundle(){

    }

    public static CarBundle init(){
        if (carBundle == null){
            carBundle = new CarBundle();
        }
        return carBundle;

    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
