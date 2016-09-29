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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
        RecyclerView aimList = (RecyclerView)findViewById(R.id.aim_list);



//        ListView aimList = (ListView)findViewById(R.id.aim_list);
        try{
            SQLiteOpenHelper helper = new DBHelper(this);
            db = helper.getReadableDatabase();
            aimCursor = db.query("AIMS", new String[]{"_id", "NAME", "TIME"},
                    null, null,null,null, null);
            CardAdapter cardAdapter = new CardAdapter(aimCursor);
            aimList.setAdapter(cardAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            aimList.setLayoutManager(layoutManager);
        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Ошибка базы данных", Toast.LENGTH_SHORT);
            toast.show();
        }
//        aimList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView name = (TextView) view.findViewById(android.R.id.text1);
//                Intent intent = new Intent(view.getContext(), NewAimActivity.class);
//                intent.putExtra(NewAimActivity.EXTRA_NAME, name.getText());
//                startActivity(intent);
//            }
//        });

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

        aimCursor.close();
        db.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        try{
//            DBHelper helper = new DBHelper(this);
//            db = helper.getReadableDatabase();
//            Cursor newCursor = db.query("AIMS", new String[]{"_id", "NAME", "TIME"},
//                    null, null,null,null, null);
//            RecyclerView listAims = (RecyclerView) findViewById(R.id.aim_list); //список целей
//            CardAdapter adapter = (CardAdapter) listAims.getAdapter();
//            listAims.setAdapter(adapter);
//            aimCursor.close();
//            aimCursor = newCursor;
//        }catch (SQLiteException ex){
//            Toast.makeText(this, "Ошибка базы данных", Toast.LENGTH_SHORT).show();
//        }
    }
}
