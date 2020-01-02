package in.aaaonlineservice.crimemukhbir;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by RACHIT GOYAL on 8/23/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public  static final String DATABASE_NAME="CrimeMukhbir.db";
    public  static final String TABLE_NAME="News_table";
    public  static final String COL_2="TITLE";
    public  static final String COL_3="LINK";
    public  static final String COL_4="DATE";
    public  static final String COL_5="CONTENT";
    public  static final String COL_6="IMAGE";
    public  static final String COL_7="INDI_ID";
    public  static final String COL_8="YOU_ID";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,LINK TEXT,DATE TEXT,CONTENT TEXT,IMAGE TEXT,INDI_ID TEXT,YOU_ID TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean inserdata(String title, String link, String date, String content, String image, String indi_id,String youid) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,title);
        contentValues.put(COL_3,link);
        contentValues.put(COL_4,date);
        contentValues.put(COL_5,content);
        contentValues.put(COL_6,image);
        contentValues.put(COL_7,indi_id);
        contentValues.put(COL_8,youid);

        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1){
            return false;
        }
        else{
            return true;
        }

    }
    public Cursor getAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getIndividualData(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" WHERE ID = " + id, null);
        return res;
    }
    public Integer deleteData(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID=?",new String[] {id});
    }
}
