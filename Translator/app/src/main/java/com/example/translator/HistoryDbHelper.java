package com.example.translator;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HistoryDbHelper extends SQLiteOpenHelper {
    private static HistoryDbHelper sHelper;
    private static final String DB_NAME = "history.db";   //数据库名
    private static final int VERSION = 1;//版本号
    public void deleteEmptySrcRecords() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM history_table WHERE src IS NULL ");
        db.close();
    }
    //必须实现其中一个构方法
    public HistoryDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //创建单例，供使用调用该类里面的的增删改查的方法
    public synchronized static HistoryDbHelper getInstance(Context context) {
        if (null == sHelper) {
            sHelper = new HistoryDbHelper(context, DB_NAME, null, VERSION);
        }
        return sHelper;
    }
    /**
     * 判断是否搜索过该单词
     */
    @SuppressLint("Range")
    public boolean isHistory(String src) {
        //获取SQLiteDatabase实例
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select history_id,src,dst from history_table where src=?";
        String[] selectionArgs = {src};
        Cursor cursor = db.rawQuery(sql, new String[]{src});
//        cursor.close();
//        db.close();
        return cursor.moveToNext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table history_table(history_id integer primary key autoincrement, " +
                "src text," +
                "dst text"  +
                ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public int addHistory(String src,String dst) {
        //判断是否浏览过历史记录
        if(!isHistory(src)){

            //获取SQLiteDatabase实例
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            //填充占位符
            values.put("src", src);
            values.put("dst", dst);

            String nullColumnHack = "values(null,?,?)";
            //执行
            int insert = (int) db.insert("history_table", nullColumnHack, values);

            db.close();
            return insert;

        }

        return 0;
    }
    @SuppressLint("Range")
    public List<HistoryInfo> queryHistoryListData() {
        //获取SQLiteDatabase实例
        SQLiteDatabase db = getReadableDatabase();
        List<HistoryInfo> list = new ArrayList<>();
        String sql = "select history_id,src,dst from history_table";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int history_id = cursor.getInt(cursor.getColumnIndex("history_id"));
            String src = cursor.getString(cursor.getColumnIndex("src"));
            String dst = cursor.getString(cursor.getColumnIndex("dst"));
            list.add(new HistoryInfo(history_id,src,dst));

        }

       cursor.close();
        db.close();
        return list;
    }

}