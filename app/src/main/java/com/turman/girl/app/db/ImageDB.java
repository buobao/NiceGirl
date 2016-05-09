package com.turman.girl.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dqf on 2016/5/9.
 */
public class ImageDB extends SQLiteOpenHelper {
    public static final String DB_NAME = "IMAGES.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "images_table";
    public static final String IMAGE_ID = "image_id";
    public static final String IMAGE_TITLE = "image_title";
    public static final String IMAGE_URL = "image_url";

    public ImageDB(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + IMAGE_ID
                + " INTEGER primary key autoincrement, " + IMAGE_TITLE + " text, "+ IMAGE_URL +" text);";
        db.execSQL(sql);
    }

    //板凳更新时删除当前表并重建
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    //分页查询
    public Cursor select(int page_num, int page_size) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null,(page_num-1)*page_size+","+page_size);
        return cursor;
    }

    //查询所有
    public Cursor selectAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    //依据url查询数据是否存在
    public boolean queryByURL(String url){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, IMAGE_URL + " = ?", new String[]{url}, null, null, null);
        return cursor.getCount() > 0;
    }

    //增加操作
    public long insert(String title,String url)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        /* ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(IMAGE_TITLE, title);
        cv.put(IMAGE_URL, url);
        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }

    //删除操作
    public void delete(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = IMAGE_ID + " = ?";
        String[] whereValue ={ Integer.toString(id) };
        db.delete(TABLE_NAME, where, whereValue);
    }
    //修改操作
    public void update(int id, String title,String url)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = IMAGE_ID + " = ?";
        String[] whereValue = { Integer.toString(id) };

        ContentValues cv = new ContentValues();
        cv.put(IMAGE_TITLE, title);
        cv.put(IMAGE_URL, url);
        db.update(TABLE_NAME, cv, where, whereValue);
    }

    /**
     * 清空数据表
     */
    public void clear(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "delete from " + TABLE_NAME + ";" + "update sqlite_sequence SET seq = 0 where name = '"+TABLE_NAME+"';";
        db.execSQL(sql);
    }
}

































