package pw.jawedyx.aimsupporter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class SettingsActivity extends Activity {
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        spinner = (Spinner)findViewById(R.id.aim_notifier_spinner);

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 30);
        Intent intent = new Intent(this, NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

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
