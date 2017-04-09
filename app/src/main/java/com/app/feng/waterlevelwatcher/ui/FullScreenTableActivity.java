package com.app.feng.waterlevelwatcher.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.feng.fixtablelayout.FixTableLayout;
import com.app.feng.waterlevelwatcher.Config;
import com.app.feng.waterlevelwatcher.R;
import com.app.feng.waterlevelwatcher.adapter.FixTableAdapter;

import java.util.ArrayList;

public class FullScreenTableActivity extends AppCompatActivity {

    FixTableLayout fixTableLayout;
    FixTableAdapter fixTableAdapter;

    String[] title;
    String[][] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_table);

        fixTableLayout = (FixTableLayout) findViewById(R.id.fixtablelayout);
        title = getIntent().getStringArrayExtra(Config.KEY.FIXTABLE_TITLE);
        ArrayList<String> tempList = getIntent().getStringArrayListExtra(Config.KEY.FIXTABLE_DATA);
        int len = getIntent().getIntExtra(Config.KEY.FIXTABLE_DATA_COLUMN,0);
        data = new String[tempList.size() / len][len];

        int col = 0;
        int row = 0;
        for (int i = 0; i < tempList.size(); i++) {
            String aData = tempList.get(i);
            data[row][col] = aData;

            if (col == len - 1) {
                col = 0;
                row++;
            } else {
                col++;
            }

        }

        fixTableAdapter = new FixTableAdapter(title,data);

        fixTableLayout.setAdapter(fixTableAdapter);
    }
}
