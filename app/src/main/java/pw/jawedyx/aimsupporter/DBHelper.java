package pw.jawedyx.aimsupporter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 4;
    public static final String  DB_NAME = "jawedyxaims";

    public DBHelper(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyBase(db, 0, DB_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyBase(db, oldVersion, newVersion);
    }

    private void updateMyBase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion <= 4){

            db.execSQL("CREATE TABLE AIMS (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT," +
                    "DESCRIPTION TEXT," +
                    "TIME TEXT," +
                    "GOTTED BOOLEAN);");
        }

    }

    public static void insertAim(SQLiteDatabase db, String name, String desc, String time, Boolean gotted){
        ContentValues cv = new ContentValues();
        cv.put("NAME", name);
        cv.put("DESCRIPTION", desc);
        cv.put("TIME", time);
        cv.put("GOTTED", gotted);
        db.insert("AIMS", null, cv);
    }

    public static void updateAim(SQLiteDatabase db, String name, String desc, String time, Boolean gotted){
        ContentValues cv = new ContentValues();
        cv.put("NAME", name);
        cv.put("DESCRIPTION", desc);
        cv.put("TIME", time);
        cv.put("GOTTED", gotted);
        db.update("AIMS", cv, "NAME = ?", new String[]{name});
    }

    public static void deleteAim(SQLiteDatabase db, String id){
        db.delete("AIMS", "_id = ?", new String[]{id});
    }
}
