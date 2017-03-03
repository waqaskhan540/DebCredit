package com.endive.easycredit.views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.endive.easycredit.R;
import com.endive.easycredit.data.SqlLiteHelper;
import com.endive.easycredit.models.RecordItem;

/**
 * Created by MWaqas on 12/31/2016.
 */

public class AddRecordActivity extends AppCompatActivity implements View.OnClickListener{

    SqlLiteHelper dbHelper ;
    EditText etBalance;
    EditText etDebit;
    EditText etCredit;
    EditText etDesc;
    EditText etDate;
    Button btnPickDate;
    private DatePickerDialog datePicker;
    Button btnAddRecord;
    private AdView mAdVeiw;
    private AdView mAdVeiw2;
    private TextView tvDebit;
    private TextView tvCredit;
    private SimpleDateFormat dateFormatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_record);

        this.getSupportActionBar().setTitle("Add Record");

        initUI();
        btnAddRecord.setOnClickListener(this);
        etDate.setEnabled(false);
        btnPickDate.setOnClickListener(this);
        dbHelper = new SqlLiteHelper(this);
        setDateTimeField();
        double currentBalance = dbHelper.getCurrentBalance();

        if(currentBalance < 1){
            etBalance.setEnabled(true);

            etDebit.setText("0");
            etDebit.setVisibility(View.INVISIBLE);
            tvDebit.setVisibility(View.INVISIBLE);

            etCredit.setText("0");
            etCredit.setVisibility(View.INVISIBLE);
            tvCredit.setVisibility(View.INVISIBLE);

           // etDesc.setText("Initial Balance");
           // etDesc.setEnabled(false);

        }else {
            etBalance.setEnabled(false);
            etBalance.setText(String.valueOf(currentBalance));
        }

    }

    private void initUI() {
        etBalance = (EditText) findViewById(R.id.balance);
        etDebit = (EditText) findViewById(R.id.etDebit);
        etCredit = (EditText) findViewById(R.id.etCredit);
        etDesc = (EditText) findViewById(R.id.etDescription);
        btnAddRecord = (Button) findViewById(R.id.btnSave);
        etDate = (EditText) findViewById(R.id.etDate);
        etDate.setInputType(InputType.TYPE_NULL);
        btnPickDate = (Button) findViewById(R.id.btnPickDate);
        mAdVeiw = (AdView) findViewById(R.id.adView);
        mAdVeiw2 = (AdView) findViewById(R.id.adView2);

        tvCredit = (TextView)findViewById(R.id.lblCredit);
        tvDebit = (TextView)findViewById(R.id.lblDebit);

        loadAdds();
    }
    private void loadAdds() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdVeiw.loadAd(adRequest);
        AdRequest adRequest2 = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdVeiw2.loadAd(adRequest2);

    }
    private void setDateTimeField() {

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
    private void addRecord() throws ParseException {

    RecordItem record = new RecordItem();

        if(etBalance.isEnabled()){

            if(validateInitialBalance()) {
                double balance = Double.valueOf(etBalance.getText().toString());
                record.setTotal(balance);

                record.setDebit(0);
                record.setCredit(0);
                record.setDescription(etDesc.getText().toString());
                record.setDate(etDate.getText().toString());
                dbHelper.addRecord(record);

                etBalance.setEnabled(false);

                etCredit.setVisibility(View.VISIBLE);
                tvCredit.setVisibility(View.VISIBLE);
                etCredit.setText("");

                etDebit.setVisibility(View.VISIBLE);
                tvDebit.setVisibility(View.VISIBLE);
                etDebit.setText("");

                etDesc.setText("");
                etDesc.setEnabled(true);

                Toast.makeText(this, "Transaction added successfully", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Balance must be greater than 0",Toast.LENGTH_LONG).show();
            }
        }else {


            double balance = dbHelper.getCurrentBalance();

            double credit = 0;
            if(etCredit.getText().toString().length() > 0)
                credit= Double.valueOf(etCredit.getText().toString());

            double debit = 0;
            if(etDebit.getText().toString().length() > 0)
                 debit=   Double.valueOf(etDebit.getText().toString());

            String date = etDate.getText().toString();
            String desc = etDesc.getText().toString();

            double total = 0;

            if(credit > 0){
                balance = balance + credit;
            }else{
                credit = 0;
            }

            if(debit > 0){
                balance = balance - debit;
            }else {
                debit = 0;
            }

            if(debit == 0 && credit == 0){
                Toast.makeText(getApplicationContext(),"Please provide valid debit or credit value",Toast.LENGTH_LONG).show();
                return;
            }
            total = balance;

            RecordItem item  = new RecordItem();
            item.setCredit(credit);
            item.setDebit(debit);
            item.setTotal(total);
            item.setDescription(desc);
            item.setDate(date);
            dbHelper.addRecord(item);

            etBalance.setText(String.valueOf(dbHelper.getCurrentBalance()));

            etCredit.setText("");
            etDebit.setText("");
            etDesc.setText("");
            etCredit.findFocus();

            Toast.makeText(this,"Transaction added successfully",Toast.LENGTH_SHORT).show();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSave:
                try {
                    if(validateDate()) {
                        addRecord();
                    }else {
                        Toast.makeText(this.getApplicationContext(),"Please pick a date",Toast.LENGTH_LONG).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnPickDate:
                datePicker.show();
                break;
        }
    }

    public boolean validateInitialBalance() {
       try{
           if(etBalance.getText().toString().length() < 1)
               return false;


           double balance = Double.parseDouble(etBalance.getText().toString());

           if(balance < 1)
               return false;

           return true;
       }catch (Exception ex){
            return false;
       }

    }

    private boolean validateDebit() {

        try{
            if(etDebit
                    .getText().toString().length() < 1)
                return false;


            double debit = Double.parseDouble(etDebit.getText().toString());

            if(debit < 1)
                return false;

            return true;
        }catch (Exception ex){
            return false;
        }
    }

    private boolean validateCredit() {

        try{
            if(etCredit
                    .getText().toString().length() < 1)
                return false;


            double credit = Double.parseDouble(etCredit.getText().toString());

            if(credit < 1)
                return false;

            return true;
        }catch (Exception ex){
            return false;
        }
    }
    public boolean validateDate() {
        if(etDate.getText().length() > 0){
            return true;
        }
        return false;
    }
}
