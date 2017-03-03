package com.endive.easycredit.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.elsinga.socialurlshare.channels.FacebookShare;
import com.elsinga.socialurlshare.channels.GooglePlusShare;
import com.elsinga.socialurlshare.channels.LinkedInShare;
import com.elsinga.socialurlshare.channels.TwitterShare;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import com.endive.easycredit.R;
import com.endive.easycredit.data.SqlLiteHelper;

/**
 * Created by MWaqas on 12/31/2016.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnAddRecord;
    private Button btnRecords;
    private Button btnTodayRecords;
    private Button btnDeleteAll;
    private Button btnAbout;
    ProgressDialog progressDoalog;
    private TextView tvLogo;
    private SqlLiteHelper dbHelper;
    private ShareActionProvider mShareActionProvider;
    InterstitialAd mInterstitialAd;
    private AdView mAdVeiw;
    private ImageView btnShare;
    private ImageView btnMoreApps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.inter));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
        dbHelper = new SqlLiteHelper(this);

        btnAddRecord = (Button)findViewById(R.id.btnAddRecord);
        btnRecords = (Button)findViewById(R.id.btnRecordsReport);
        btnTodayRecords = (Button) findViewById(R.id.btnTodayData);
        btnDeleteAll = (Button)  findViewById(R.id.btnDeleteData);
        btnAbout = (Button) findViewById(R.id.btnAbout);

        tvLogo = (TextView) findViewById(R.id.tvLogo);
        mAdVeiw = (AdView) findViewById(R.id.adView);
        btnShare = (ImageView) findViewById(R.id.share);
        btnMoreApps = (ImageView) findViewById(R.id.moreapps);
        loadAdds();

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/BalooChettan-Regular.ttf");
        tvLogo.setTypeface(custom_font);

        btnAddRecord.setOnClickListener(this);
        btnRecords.setOnClickListener(this);
        btnTodayRecords.setOnClickListener(this);
        btnDeleteAll.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnMoreApps.setOnClickListener(this);
       // setShareIntent(Intent shareIntent)

    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
    private void loadAdds() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdVeiw.loadAd(adRequest);
    }

    public void showYesNoDialog() {
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Deleting records");
        progressDoalog.setTitle("Please wait...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdVeiw.loadAd(adRequest);

       final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete All");
        builder.setMessage("This will delete all your previous records permanently, do you wish to continue?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                progressDoalog.show();
                dbHelper.deleteAll();
                Toast.makeText(MainActivity.this,"Records deleted",Toast.LENGTH_LONG).show();
                progressDoalog.dismiss();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });



        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnAddRecord:
            Intent addRecordActivity = new Intent(this,AddRecordActivity.class);
                startActivity(addRecordActivity);
                break;

            case R.id.btnRecordsReport:
                Intent recordListActivity = new Intent(this,RecordListActivity.class);
                startActivity(recordListActivity);
                break;

            case R.id.btnTodayData :
                Intent todayRecordsActivity = new Intent(this,TodayRecordsActivity.class);
                startActivity(todayRecordsActivity);
                break;

            case R.id.btnDeleteData:
                showYesNoDialog();
                break;

            case R.id.btnAbout:
                Intent aboutActivity = new Intent(this,AboutActivity.class);
                startActivity(aboutActivity);


                break;
            case R.id.share:
                showShareDialog();
                break;
            case R.id.moreapps:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getResources().getString(R.string.endive_link)));
                startActivity(intent);
                break;

        }
    }

    private void showShareDialog() {
        final String names[] ={"Facebook","Twitter","LinkedIn","Google+"};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.share_links, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Share via");
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    //facebook
                    FacebookShare sharer = new FacebookShare(MainActivity.this);
                    sharer.setLink(getString(R.string.app_link));
                    sharer.share();
                }else if(position == 1){
                    //twitter
                    TwitterShare sharer = new TwitterShare(MainActivity.this);
                    sharer.setLink(getString(R.string.app_link));
                    sharer.share();
                }else if(position == 2){
                    //linkedin
                    LinkedInShare sharer = new LinkedInShare(MainActivity.this);
                    sharer.setLink(getString(R.string.app_link));
                    sharer.share();
                }else if(position == 3){
                    //google+
                    GooglePlusShare sharer = new GooglePlusShare(MainActivity.this);
                    sharer.setLink(getString(R.string.app_link));
                    sharer.share();
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);

        lv.setAdapter(adapter);
        alertDialog.show();
    }
}
