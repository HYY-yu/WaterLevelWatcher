package com.app.feng.waterlevelwatcher.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.feng.fixtablelayout.FixTableLayout;
import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.adapter.FixTableAdapter;
import com.app.feng.waterlevelwatcher.utils.ClassUtil;
import com.app.feng.waterlevelwatcher.utils.RealmUtil;
import com.app.feng.waterlevelwatcher.utils.Utils;

import io.realm.Realm;
import io.realm.RealmResults;

public class FullScreenTableActivity extends AppCompatActivity {

    FixTableLayout fixTableLayout;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_table);

        realm = Realm.getDefaultInstance();

        fixTableLayout = (FixTableLayout) findViewById(R.id.fixtablelayout);

        int pos = getIntent().getIntExtra(Config.KEY.QUERY_POS,0);
        String time = getIntent().getStringExtra(Config.KEY.QUERY_TIME);

        Class clazz = ClassUtil.jugeClass(pos);


        final RealmResults beanList;
        if (pos == 3 || pos == 4) {
            beanList = RealmUtil.loadAllAllLineBeanByTimeString(
                    clazz,realm,time);
        }else{
            beanList = RealmUtil.loadAllAllLineBeanByTime(
                    clazz,realm,Utils.parse(time));
        }

        setToAdapter(clazz,beanList);

    }

    private void setToAdapter(
            Class clazz,RealmResults beanList) {
        FixTableAdapter fixTableAdapter = ClassUtil.genFixTableAdapter(clazz,beanList);
        fixTableLayout.setAdapter(fixTableAdapter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}
