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
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_EXISTS = "KEY_EXISTS";
    private static final String PUBLIC_KEY_FILE = "/";
    private static final String DOMAIN = "TODO";

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
            @Override public void onItemSelected(AdapterView<?> p, View v, int i, long id)
            {data_ca = (String) p.getItemAtPosition(i);}
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    private ArrayAdapter<CharSequence> adapter(int array) {
        return ArrayAdapter.createFromResource(this, array, android.R.layout.simple_spinner_item);
    }

    public void send(View view) {
        updateValues();

        try {
            String message = csvString();
            byte[] encrypted = encrypt(message);
            send(new String(encrypted));

            showConfirmation();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.sending_error), Toast.LENGTH_SHORT);
        }
    }

    private void updateValues() {
        data_me = me.getText().toString();

        SharedPreferences pref = getSharedPreferences("P", MODE_PRIVATE);
        data_em = pref.getString("KEY_EM", "");
        data_co = pref.getString("KEY_CO", "");
        data_ci = pref.getString("KEY_CI", "");
        data_ag = pref.getString("KEY_AG", "");
        data_dt = pref.getString("KEY_DT", "");
    }

    private String csvString(){
        StringBuilder builder = new StringBuilder();
        builder.append(data_me);
        builder.append(",");
        builder.append(data_ca);
        builder.append(",");
        builder.append(data_em);
        builder.append(",");
        builder.append(data_co);
        builder.append(",");
        builder.append(data_ci);
        builder.append(",");
        builder.append(data_ag);
        builder.append(",");
        builder.append(data_dt);

        return builder.toString();
    }

    private byte[] encrypt(String message) throws Exception{
        byte[] encrypted = null;
        PublicKey public_key = getPublicKey();
        try {
            final Cipher cipher = Cipher.getInstance("RSA");
            // Encrypt using public_key
            cipher.init(Cipher.ENCRYPT_MODE, public_key);
            encrypted = cipher.doFinal(message.getBytes());
        } catch (Exception e) {e.printStackTrace();}
        return encrypted;
    }

    private PublicKey getPublicKey() throws Exception {
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
        return (PublicKey) stream.readObject();
    }

    private void send(String message) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(DOMAIN);

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("param-1", "12345"));
        params.add(new BasicNameValuePair("param-2", "Hello!"));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        //Execute and get the response.
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

    }

    private void showConfirmation() {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        startActivity(intent);
    }
}
