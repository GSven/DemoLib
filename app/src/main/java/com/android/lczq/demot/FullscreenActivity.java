package com.android.lczq.demot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.lczq.viewlibrary.view.PieButton;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        PieButton pieButton = findViewById(R.id.pieButton);
        pieButton.setProgress(50);
        pieButton.setOnClickListener(new PieButton.IOnClickListener() {
            @Override
            public void onClickListener(boolean left) {
                Toast.makeText(FullscreenActivity.this, (left ? "左侧":"右侧"), Toast.LENGTH_LONG).show();
            }
        });
    }

}
