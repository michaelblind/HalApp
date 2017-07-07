package tuerantuer.app.halapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {
    private static final String KEY_EXISTS = "KEY_EXISTS";

    private EditText em, ci;
    private TextView dt;

    private static String data_em, data_dt, data_co, data_ag, data_ge /*"m": Male, "f": Female*/, data_ci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        em = (EditText) findViewById(R.id.email);
        ci = (EditText) findViewById(R.id.city);
        dt = (TextView) findViewById(R.id.date);
        Spinner co = (Spinner) findViewById(R.id.country);
        Spinner ag = (Spinner) findViewById(R.id.age);

        Spinner[] spinners = new Spinner[]{co, ag};
        int[] arrays = new int[]{R.array.countries, R.array.ages};

        setUpSpinners(spinners, arrays);

        co.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int i, long id) {data_co = (String) p.getItemAtPosition(i);}
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
        ag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int i, long id) {data_ag = (String) p.getItemAtPosition(i);}
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        dt.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                DatePickerFragment d = new DatePickerFragment();
                d.setDT(dt);
                d.show(getSupportFragmentManager(), "timePicker");
            }
        });
    }

    private void setUpSpinners(Spinner[] spinners, int[] arrays) {
        for (int i=0; i < spinners.length; i++) {
            Spinner s = spinners[i];
            ArrayAdapter<CharSequence> a = adapter(arrays[i]);
            a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            s.setAdapter(a);
        }
    }

    private ArrayAdapter<CharSequence> adapter(int array) {
        return ArrayAdapter.createFromResource(this, array, android.R.layout.simple_spinner_item);
    }

    public void next(View view) {
        if (!checkValues()){
            Toast.makeText(this, R.string.data_incomplete, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences pref = getSharedPreferences("P", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(KEY_EXISTS, true);
        editor.putString("KEY_EM", data_em);
        editor.putString("KEY_DT", data_dt);
        editor.putString("KEY_CO", data_co);
        editor.putString("KEY_AG", data_ag);
        editor.putString("KEY_GE", data_ge);
        editor.putString("KEY_CI", data_ci);

        editor.commit();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private boolean checkValues() {
        return  valid(data_em = em.getText().toString())
                && ! (data_ci = ci.getText().toString()).equals("")
                && ! (data_dt = dt.getText().toString()).equals(getString(R.string.pick_date))
                && data_co != null
                && data_ag != null
                && data_ge != null;
    }

    /** Make sure Email has a valid format
     * @param em Email address to check
     * @return True, if em is a valid email. False, else */
    private boolean valid(String em) {
        String[] parts = em.split("@"); // Split at '@'
        if (parts.length < 2) return false;

        String pre = parts[0]; // Everything before '@'
        String[] reg = parts[1].split("."); //Split reg at '.'

        return reg.length >= 2 && !pre.equals("") && !reg[0].equals("") && !reg[1].equals("");
    }

    public void clickMale(View view)   {data_ge = "m";}
    public void clickFemale(View view) {data_ge = "f";}

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        TextView dt;

        public void setDT(TextView dt) {this.dt = dt;}

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, y, m, d);
        }

        @Override
        public void onDateSet(DatePicker view, int y, int m, int d) {
            data_dt = y + "-" + m + "-" + d;
            dt.setText(data_dt);
        }
    }
}
