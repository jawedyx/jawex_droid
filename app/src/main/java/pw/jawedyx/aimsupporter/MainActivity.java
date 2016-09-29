package pw.jawedyx.aimsupporter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends Activity {
    private SQLiteDatabase db;
    private Cursor aimCursor;
    private Calendar calendar;
    private boolean isTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView aimList = (ListView)findViewById(R.id.aim_list);
        try{
            SQLiteOpenHelper helper = new DBHelper(this);
            db = helper.getReadableDatabase();
            aimCursor = db.query("AIMS", new String[]{"_id", "NAME", "DESCRIPTION", "TIME","GOTTED"},
                    null, null,null,null, null);
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, aimCursor, new String[]{"NAME", "TIME"},
                    new int[]{android.R.id.text1}, 0);
            aimList.setAdapter(cursorAdapter);
        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Ошибка базы данных", Toast.LENGTH_SHORT);
            toast.show();
        }
        aimList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView name = (TextView) view.findViewById(android.R.id.text1);
                Intent intent = new Intent(view.getContext(), NewAimActivity.class);
                intent.putExtra(NewAimActivity.EXTRA_NAME, name.getText());
                startActivity(intent);
            }
        });

        if(isTime){
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 17);
            calendar.set(Calendar.MINUTE, 30);

            Intent intent = new Intent(this, NotificationReciever.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            isTime = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_setting:
                startActivity(new Intent(this,SettingsActivity.class));
                return true;
            case R.id.add_aim:
                Intent i = new Intent(this, NewAimActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            default:
               return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Код запуска отложенного уведомления

        aimCursor.close();
        db.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try{
            DBHelper helper = new DBHelper(this);
            db = helper.getReadableDatabase();
            Cursor newCursor = db.query("AIMS", new String[]{"_id", "NAME", "DESCRIPTION", "TIME","GOTTED"},
                    null, null,null,null, null);
            ListView listAims = (ListView)findViewById(R.id.aim_list); //список целей
            CursorAdapter adapter = (CursorAdapter)listAims.getAdapter();
            adapter.changeCursor(newCursor);
            aimCursor = newCursor;
        }catch (SQLiteException ex){
            Toast.makeText(this, "Ошибка базы данных", Toast.LENGTH_SHORT).show();
        }
    }
}
