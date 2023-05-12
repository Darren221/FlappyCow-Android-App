package edu.ucsd.flappycow.view;

import android.widget.Toast;

import edu.ucsd.flappycow.util.IObserver;
import edu.ucsd.flappycow.util.MyToastData;

public class ToastHandler implements IObserver<MyToastData> {
    public ToastHandler() {

    }

    @Override
    public void onUpdate(MyToastData data) {
        Toast.makeText(data.gameActivity, data.arg, Toast.LENGTH_SHORT).show();
    }
}