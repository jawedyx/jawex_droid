package pw.jawedyx.aimsupporter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class NewAimActivity extends Activity {
    private static Button mTime;
    private String mName;
    private String mDesc;
    private String mTimeText;
    private boolean isGotted;
    private ImageButton go;
    private EditText name;
    private EditText desc;
    private CheckBox gotted;
    private SQLiteOpenHelper helper;
    private static final String DIALOG_DATE = "date";
    public static final String EXTRA_ID = "id";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aim);

        mTime = (Button)findViewById(R.id.aim_time);
        go = (ImageButton)findViewById(R.id.go);
        name = (EditText)findViewById(R.id.aimName);
        desc = (EditText)findViewById(R.id.aimDesc);
        gotted = (CheckBox)findViewById(R.id.aimGotted);

        if(getIntent().getStringExtra(EXTRA_ID) != null){ //Редактирование
            if(getActionBar() != null){
                getActionBar().setTitle(R.string.aim_edit_title); //Заголовок
                getActionBar().setDisplayHomeAsUpEnabled(true);
            }

            try{
                helper = new DBHelper(this); //Заполнение остальных данных
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = db.query("AIMS", new String[]{"_id", "NAME", "DESCRIPTION", "TIME", "GOTTED"},
                        "_id = ?",
                        new String[]{getIntent().getStringExtra(EXTRA_ID)}, null, null, null);
                if(cursor.moveToFirst()) {
                     mName = cursor.getString(1);
                     mDesc = cursor.getString(2);
                     mTimeText = cursor.getString(3);
                     isGotted = (cursor.getInt(4) == 1);
                }
                cursor.close();
                db.close();
                name.setText(mName);
                desc.setText(mDesc);
                mTime.setText(mTimeText);
                gotted.setChecked(isGotted);

                go.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        DBHelper.updateAim(db,name.getText().toString(), desc.getText().toString(), mTime.getText().toString(), gotted.isChecked(), getIntent().getStringExtra(EXTRA_ID) );
                        db.close();
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.close_detail_animation, R.anim.alpha);
                    }
                });

            }catch (SQLiteException ex){
                Toast.makeText(this, "Ошибка обработки данных", Toast.LENGTH_SHORT).show();
            }
        }else {
            if(getActionBar() != null){
                getActionBar().setTitle(R.string.aim_create); //Заголовок
                getActionBar().setDisplayHomeAsUpEnabled(true);
            }

            go.setOnClickListener(new View.OnClickListener() { //При создании цели
                @Override
                public void onClick(View v) {

                    try{
                        helper = new DBHelper(v.getContext());
                        SQLiteDatabase db = helper.getWritableDatabase();
                        if(name.getText().toString().equals("")  || mTime.getText().toString().equals(getString(R.string.aim_time))){
                            Toast.makeText(v.getContext(), "Пожалуйста, укажите больше информации о цели", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        DBHelper.insertAim(db,name.getText().toString(), desc.getText().toString(), mTime.getText().toString(), gotted.isChecked() );
                        db.close();
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.close_detail_animation, R.anim.alpha);

                    }catch (SQLiteException ex){
                        Toast.makeText(v.getContext(), "База данных недоступна", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            gotted.setVisibility(View.GONE);

        }


    }

    public void setAimTime(View view){
        FragmentManager fm = getFragmentManager();
        DatePickerFragment dialog = new DatePickerFragment();
        dialog.show(fm,DIALOG_DATE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.close_detail_animation, R.anim.alpha);
    }
}
