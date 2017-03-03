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
 * Created by MWaqas on 12/31/2016.
 */

public class RecordListActivity extends AppCompatActivity {

    private ListView recordsList;
    private SqlLiteHelper dbHelper;
    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_list_activity);
        this.getSupportActionBar().setTitle("Report");
        dbHelper = new SqlLiteHelper(this);
        initUI();

        View header = getLayoutInflater().inflate(R.layout.listview_header,null);

        ArrayList<RecordItem> items = null;
        try {
            items = (ArrayList<RecordItem>) dbHelper.getAllRecords();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RecordListAdapter adapter  = new RecordListAdapter(items,this);
        recordsList.addHeaderView(header);
        recordsList.setAdapter(adapter);
    }
    private void loadAdds() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
    }
    private void initUI() {

        recordsList = (ListView) findViewById(R.id.lvRecords);
        adView = (AdView) findViewById(R.id.adView);
        loadAdds();
    }
}
