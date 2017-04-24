package com.app.feng.waterlevelwatcher.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.feng.fixtablelayout.FixTableLayout;
import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.adapter.FixTableAdapter;
import com.app.feng.waterlevelwatcher.bean.DataBean;

import java.util.ArrayList;

public class FullScreenTableActivity extends AppCompatActivity {

    FixTableLayout fixTableLayout;
    FixTableAdapter fixTableAdapter;

    String[] title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_table);

        fixTableLayout = (FixTableLayout) findViewById(R.id.fixtablelayout);
        title = getIntent().getStringArrayExtra(Config.KEY.FIXTABLE_TITLE);
        ArrayList<DataBean> tempList = getIntent().getParcelableArrayListExtra(Config.KEY.FIXTABLE_DATA);

        fixTableAdapter = new FixTableAdapter(title,tempList);

        fixTableLayout.setAdapter(fixTableAdapter);
    }
}
