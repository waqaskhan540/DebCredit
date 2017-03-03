package com.endive.easycredit.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.endive.easycredit.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by MWaqas on 1/12/2017.
 */

public class AboutActivity extends Activity {

    private AdView adview;
    private ViewGroup aboutLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.about);

        adview = (AdView)findViewById(R.id.adView);




        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adview.loadAd(adRequest);


        aboutLayout = (ViewGroup) findViewById(R.id.aboutLayout);


        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("EasyCredit helps you manage your credits and debits all at one place with simple and easy features.We will be looking forward to your feedback and suggesstions, please feel free to contact us using the information below.")

                .setImage(R.drawable.header)
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("Connect with us")
                .addEmail("waqaskhan@endivestudios.com")
                .addWebsite("http://www.endivestudios.com/")
                .addFacebook("https://web.facebook.com/endivestudios/")
                .addItem(getCopyRightsElement())
                .create();
        aboutLayout.addView(aboutPage);

    }
    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format("copyright 2017", "2017");
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIcon(R.drawable.copy);
        copyRightsElement.setColor(ContextCompat.getColor(this, mehdi.sakout.aboutpage.R.color.about_item_icon_color));
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }
}
