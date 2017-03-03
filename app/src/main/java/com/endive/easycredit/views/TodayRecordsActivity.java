package com.endive.easycredit.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import com.endive.easycredit.R;
import com.endive.easycredit.customViews.RecordListAdapter;
import com.endive.easycredit.data.SqlLiteHelper;
import com.endive.easycredit.models.RecordItem;

/**
 * Created by MWaqas on 1/3/2017.
 */

public class TodayRecordsActivity extends AppCompatActivity {

    private ListView lvToday;
    private SqlLiteHelper dbHelper;
    private AdView mAdVeiw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_list_activity);
        dbHelper = new SqlLiteHelper(this);

        this.getSupportActionBar().setTitle("Today");
        initUI();
        ArrayList<RecordItem> items = null;
        try {
            items = (ArrayList<RecordItem>) dbHelper.getTodayRecords();
        } catch (Exception e) {
            e.printStackTrace();
        }
        View header = getLayoutInflater().inflate(R.layout.listview_header,null);
        RecordListAdapter adapter  = new RecordListAdapter(items,this);
        lvToday.addHeaderView(header);
        lvToday.setAdapter(adapter);

    }
    private void loadAdds() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdVeiw.loadAd(adRequest);
    }
    private void initUI() {

        lvToday = (ListView) findViewById(R.id.lvRecords);
        mAdVeiw = (AdView) findViewById(R.id.adView);
        loadAdds();
    }
}
