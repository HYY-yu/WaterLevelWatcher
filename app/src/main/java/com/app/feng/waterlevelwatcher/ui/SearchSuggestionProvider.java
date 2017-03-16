package com.app.feng.waterlevelwatcher.ui;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.app.feng.waterlevelwatcher.bean.MonitoringStationBean;
import com.app.feng.waterlevelwatcher.utils.RealmUtils;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by feng on 2017/3/16.
 */
public class SearchSuggestionProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(
            @NonNull Uri uri,@Nullable String[] projection,@Nullable String selection,
            @Nullable String[] selectionArgs,@Nullable String sortOrder) {
        String queryString = uri.getLastPathSegment();

        Realm realm = Realm.getDefaultInstance();

        MatrixCursor matrixCursor = new MatrixCursor(
                new String[]{BaseColumns._ID,SearchManager.SUGGEST_COLUMN_TEXT_1,
                             SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                             SearchManager.SUGGEST_COLUMN_QUERY});
        RealmResults<MonitoringStationBean> realmResults;
        //首先查看其是否为数字
        try {
            int id = Integer.parseInt(queryString);
            MonitoringStationBean bean = RealmUtils.loadStationDataById(realm,id);
            if (bean != null) {
                matrixCursor.addRow(new Object[]{bean.getSluiceID(),
                                                 bean.getName() + "(" + bean.getSluiceID() + ")",
                                                 bean.getSluiceID(),bean.getSluiceID()});
            }
        } catch (NumberFormatException e) {
            realmResults = RealmUtils.queryStationByName(realm,queryString);
            for (MonitoringStationBean bean : realmResults) {
                matrixCursor.addRow(new Object[]{bean.getSluiceID(),
                                                 bean.getName() + "(" + bean.getSluiceID() + ")",
                                                 bean.getSluiceID(),bean.getSluiceID()});
            }
        } finally {
            realm.close();
        }
        return matrixCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(
            @NonNull Uri uri,@Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(
            @NonNull Uri uri,@Nullable String selection,@Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(
            @NonNull Uri uri,@Nullable ContentValues values,@Nullable String selection,
            @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }
}
