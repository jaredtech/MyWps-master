package com.example.ysl.mywps.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ysl.mywps.bean.TransportBean;
import com.example.ysl.mywps.utils.CommonUtil;

/**
 * Created by ysl on 2018/2/9.
 * 介绍: 存储下载列表数据
 */

public class DownLoadProvider extends ContentProvider {

    private static final int BEANS = 1;
    private static final int BEAN = 2;
    private static final String TABLE_NAME = "download_file";
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String AUTHORITY = "com.example.ysl.mywps.download.file";
    private static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + TABLE_NAME;
    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);
    private DownloadSqlite downloadSqlite;


    static {
        URI_MATCHER.addURI(AUTHORITY, TABLE_NAME, BEANS);
        URI_MATCHER.addURI(AUTHORITY, TABLE_NAME + "/#", BEAN);
    }


    @Override
    public boolean onCreate() {

        downloadSqlite = new DownloadSqlite(getContext(), TABLE_NAME, null, 1);

        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = downloadSqlite.getReadableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case BEANS:

                if (sortOrder == null) {
                    return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                }
                return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

            case BEAN:

                long id = ContentUris.parseId(uri);
                String where = TransportBean.NAME + " = " + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + "  and " + where;
                }
                return db.query(TABLE_NAME, projection, where, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Unkonwn Uri: " + uri.toString());
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (URI_MATCHER.match(uri)) {
            case BEANS:
                return "vnd.android.cursor.dir/" + TABLE_NAME;

            case BEAN:
                return "vnd.android.cursor.item/" + TABLE_NAME;

            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = downloadSqlite.getWritableDatabase();
        switch (URI_MATCHER.match(uri)){

            case BEANS:
                long rowId = db.insert(TABLE_NAME,TransportBean.NAME,values);
                return ContentUris.withAppendedId(uri,rowId);
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());

        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = downloadSqlite.getWritableDatabase();
        int count = 0;

        switch (URI_MATCHER.match(uri)){

            case BEANS:

                count = db.delete(TABLE_NAME,selection,selectionArgs);
                return count;

            case BEAN:

                long id = ContentUris.parseId(uri);
                String where = TransportBean.NAME + " = "+id;
                count = db.delete(TABLE_NAME,selection,selectionArgs);
                return count;
            default:
                throw new IllegalArgumentException("Unkown Uri:" + uri.toString());
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = downloadSqlite.getWritableDatabase();
        int count = 0;
        switch (URI_MATCHER.match(uri)){

            case BEANS:

                count = db.update(TABLE_NAME,values,selection,selectionArgs);
                return  count;
            case BEAN:

                long id = ContentUris.parseId(uri);
                String where = TransportBean.NAME + " = "+id;
                if(CommonUtil.isNotEmpty(selection)){
                    where = selection+" and "+where;
                }
                count = db.update(TABLE_NAME,values,where,selectionArgs);
                return count;
            default:
                throw new IllegalArgumentException("Unkowon Uri: " + uri.toString());
        }
    }


    private class DownloadSqlite extends SQLiteOpenHelper {

        public DownloadSqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "create table download_file(" + TransportBean.NAME + " text," + TransportBean.DATE + " VARCHAR(20)," + TransportBean.PATH + " text,"
                    + TransportBean.SIZE + " VARCHAR(10),myvalue text,mykey VARCHAR(30));";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }


    }

}
