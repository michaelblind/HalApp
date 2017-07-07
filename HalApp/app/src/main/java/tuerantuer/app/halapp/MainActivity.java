package tuerantuer.app.halapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_EXISTS = "KEY_EXISTS";

    private EditText me;
    private Spinner  ca;

    //Data Strings
    private String data_me, data_ca, data_em, data_dt, data_co, data_ag, data_ge, data_ci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check to see if the users has an account
        boolean exists = getSharedPreferences("P", MODE_PRIVATE).getBoolean(KEY_EXISTS, false);
        if (!exists) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            finish();
        }

        me = (EditText) findViewById(R.id.message);
        ca = (Spinner)   findViewById(R.id.category);
        //Register Spinner
        ArrayAdapter<CharSequence> a = adapter(R.array.categories);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ca.setAdapter(a);

        ca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int i, long id) {data_ca = (String) p.getItemAtPosition(i);}
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    private ArrayAdapter<CharSequence> adapter(int array) {
        return ArrayAdapter.createFromResource(this, array, android.R.layout.simple_spinner_item);
    }

    public void send(View view) {
        data_me = me.getText().toString();

        // Get Preferences
        SharedPreferences pref = getSharedPreferences("P", MODE_PRIVATE);
        data_ci = pref.getString("KEY_CI", "");
        data_ag = pref.getString("KEY_AG", "");
        data_co = pref.getString("KEY_CO", "");
        data_dt = pref.getString("KEY_DT", "");
        data_em = pref.getString("KEY_EM", "");
        data_ge = pref.getString("KEY_GE", "");

        uploadRequest();
        startActivityConfirmation();
    }

    private void uploadRequest() {

    }

    private void startActivityConfirmation() {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        startActivity(intent);
    }
}
