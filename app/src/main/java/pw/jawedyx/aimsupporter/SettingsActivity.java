package pw.jawedyx.aimsupporter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity {
    private Spinner spinner;
    private SharedPreferences sharedPreferences;
    private int bgColor = -1;
    public static final String APP_SETTINGS = "AimSupporterSettings";
    public static final String APP_SETTINGS_BG = "bg_color";
    public static final String APP_SETTINGS_NOTY = "notyfication_timer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE);
        spinner = (Spinner)findViewById(R.id.aim_notifier_spinner);
        if (sharedPreferences.contains(SettingsActivity.APP_SETTINGS_NOTY)){
            spinner.setSelection(sharedPreferences.getInt(APP_SETTINGS_NOTY, 0));
        }

        final GridView gridView = (GridView)findViewById(R.id.image_selector);
        gridView.setAdapter(new BGAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bgColor = (int)gridView.getAdapter().getItem(position);

            }
        });




    }

    public void goOnClick(View view) {
        int variant = spinner.getSelectedItemPosition();


        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (bgColor != -1){
            editor.putInt(APP_SETTINGS_BG, bgColor);
        }
        editor.putInt(APP_SETTINGS_NOTY, variant);
        editor.apply();

        Toast.makeText(this,"Требуется перезагрузка приложения, чтобы настройки вступили в силу", Toast.LENGTH_SHORT ).show();


    }
}
