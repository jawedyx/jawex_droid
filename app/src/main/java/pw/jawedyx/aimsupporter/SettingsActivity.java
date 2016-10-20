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
    private int lastClickPosition = -1;
    public static final String APP_SETTINGS = "AimSupporterSettings";
    public static final String APP_SETTINGS_BG = "bg_color";
    public static final String APP_SETTINGS_NOTY = "notyfication_timer";
    public static final String INT_POSITION = "position";
    public static final String INT_BGCOLOR = "color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinner = (Spinner)findViewById(R.id.aim_notifier_spinner);
        final GridView gridView = (GridView)findViewById(R.id.image_selector);
        final BGAdapter bgAdapter = new BGAdapter(this);
        if(savedInstanceState != null){
            bgColor = savedInstanceState.getInt(INT_BGCOLOR);
            bgAdapter.setSelectedPosition(savedInstanceState.getInt(INT_POSITION));
            bgAdapter.notifyDataSetChanged();
        }
        gridView.setAdapter(bgAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bgColor = (int)gridView.getAdapter().getItem(position);
                lastClickPosition = position;
                bgAdapter.setSelectedPosition(position);
                bgAdapter.notifyDataSetChanged();
           }
        });

        sharedPreferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE);
        if (sharedPreferences.contains(SettingsActivity.APP_SETTINGS_NOTY)){
            spinner.setSelection(sharedPreferences.getInt(APP_SETTINGS_NOTY, 0));
        }

    }

    public void goOnClick(View view) {
        int variant = spinner.getSelectedItemPosition();


        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (bgColor != -1){
            editor.putInt(APP_SETTINGS_BG, bgColor);
        }
        editor.putInt(APP_SETTINGS_NOTY, variant);
        editor.apply();

        Toast.makeText(this, "Настройки успешно сохранены", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INT_BGCOLOR, bgColor);
        outState.putInt(INT_POSITION, lastClickPosition);
        super.onSaveInstanceState(outState);
    }
}
