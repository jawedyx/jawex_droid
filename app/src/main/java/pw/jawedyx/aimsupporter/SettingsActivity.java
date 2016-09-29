package pw.jawedyx.aimsupporter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity {
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        spinner = (Spinner)findViewById(R.id.aim_notifier_spinner);



    }

    public void goOnClick(View view) {
        int variant = spinner.getSelectedItemPosition();
        switch(variant){
            case 0:
                Toast.makeText(SettingsActivity.this, "Text1", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(SettingsActivity.this, "Text2", Toast.LENGTH_SHORT).show();
                break;

        }

    }
}
