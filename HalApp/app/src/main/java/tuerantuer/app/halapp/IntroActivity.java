package tuerantuer.app.halapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class IntroActivity extends AppCompatActivity {
    private static final String KEY_EXISTS = "KEY_EXISTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    public void next(View view) {
        //Check if User already has account
        Class c = (value(KEY_EXISTS)) ? MainActivity.class : ProfileActivity.class;

        Intent intent = new Intent(this, c);
        startActivity(intent);
        finish();
    }

    private boolean value(String key) {
        return getSharedPreferences("P", MODE_PRIVATE).getBoolean(key, false);
    }
}
