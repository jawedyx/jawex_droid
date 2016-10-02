package pw.jawedyx.aimsupporter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private SQLiteDatabase db;
    private Cursor aimCursor;
    private  RecyclerView aimList;
    public final static String ALARM_KEY = "alarmTag";


    private boolean isTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aimList = (RecyclerView)findViewById(R.id.aim_list);

        try{
            SQLiteOpenHelper helper = new DBHelper(this);
            db = helper.getReadableDatabase();
            aimCursor = db.query("AIMS", new String[]{"_id", "NAME", "TIME"},
                    null, null,null,null, null);
            final CardAdapter cardAdapter = new CardAdapter(aimCursor);
            aimList.setAdapter(cardAdapter);
            final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            aimList.setLayoutManager(layoutManager);
            cardAdapter.setListener(new CardAdapter.Listener() {
                @Override
                public void onClick(int position) {
                    View v = layoutManager.findViewByPosition(position);
                    TextView id = (TextView)v.findViewById(R.id.cursorId);
                    Intent intent = new Intent(v.getContext(), NewAimActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra(NewAimActivity.EXTRA_ID, id.getText());
                    startActivity(intent);

                }

                @Override
                public void onLongClick(int position) { // Удаление
                    View v = layoutManager.findViewByPosition(position);
                    TextView id = (TextView)v.findViewById(R.id.cursorId);
                    AlertDialog ad = new AlertDialog.Builder(v.getContext())
                            .setMessage(R.string.delete_message)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle(R.string.delete_title)
                            .setPositiveButton(R.string.delete_yes, new AimDialog(id.getText().toString()))
                            .setNeutralButton(R.string.delete_cancel, new AimDialog(id.getText().toString()))
                            .create();
                    ad.show();
                }
            });


        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Ошибка базы данных", Toast.LENGTH_SHORT);
            toast.show();
        }


//        if(isTime){ //some errors there
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.HOUR_OF_DAY, 17);
//            calendar.set(Calendar.MINUTE, 30);
//
//            Intent intent = new Intent(this, NotificationReciever.class);
//
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
//            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//            isTime = false;
//        }

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


        try{
            SQLiteOpenHelper helper = new DBHelper(this);
            db = helper.getReadableDatabase();
            Cursor newCursor = db.query("AIMS", new String[]{"_id", "NAME", "TIME"},
                    null, null,null,null, null);
            aimList = (RecyclerView) findViewById(R.id.aim_list); //список целей
            CardAdapter adapter = (CardAdapter) aimList.getAdapter();
            adapter.updateList(newCursor);
            aimCursor = newCursor;
        }catch (SQLiteException ex){
            Toast.makeText(this, "Ошибка базы данных", Toast.LENGTH_SHORT).show();
        }
    }


    private class AimDialog implements DialogInterface.OnClickListener{
        private String mPosition;

        public AimDialog(String position){
            this.mPosition = position;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    SQLiteOpenHelper helper = new DBHelper(getApplicationContext());
                    SQLiteDatabase db = helper.getWritableDatabase();
                    DBHelper.deleteAim(db, mPosition);
                    db.close();


                    SQLiteDatabase bd = helper.getReadableDatabase();
                    Cursor newCursor = bd.query("AIMS", new String[]{"_id", "NAME", "TIME"}, null, null,null,null, null);
                    CardAdapter adapter = (CardAdapter) aimList.getAdapter();
                    adapter.updateList(newCursor);
                    bd.close();

                    Toast.makeText(getApplicationContext(), R.string.delete_aim_hint, Toast.LENGTH_SHORT).show();
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    break;
            }
        }
    }

}
