package com.endive.easycredit.customViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.endive.easycredit.R;
import com.endive.easycredit.models.RecordItem;

/**
 * Created by MWaqas on 1/1/2017.
 */

public class RecordListAdapter extends BaseAdapter {

    private ArrayList<RecordItem> items;
    private Context mContext;
    public RecordListAdapter(ArrayList<RecordItem> itemList,Context context){
        this.items = itemList;
        mContext = context;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {
        TextView txtDate;
        TextView txtDebit;
        TextView txtCredit;
        TextView txtTotal;
        TextView tvDesc;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecordItem item = (RecordItem) getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){

            viewHolder = new ViewHolder();
            LayoutInflater inflater = ( LayoutInflater )mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.record_list_item,parent,false);
            viewHolder.txtCredit =  (TextView)convertView.findViewById(R.id.lblCredit);
            viewHolder.txtDebit = (TextView) convertView.findViewById(R.id.lblDebit);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.lblDate);
            viewHolder.txtTotal = (TextView) convertView.findViewById(R.id.lblTotal);
            viewHolder.tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        double credit = item.getCredit();
        double debit = item.getDebit();
        double total= item.getTotal();

        String creditString = Double.toString(credit);
        String debitString = Double.toString(debit);
        String totalString = Double.toString(total);
        String dateString = item.getDate();

        if(credit > 0){
            creditString = "+" + creditString;
        }

        if(debit > 0){
            debitString = "-"+debitString;
        }
        String[] dateparts = null;
        if(dateString != null) {
            dateparts= dateString.split(" ");
        }
        if(dateparts != null && dateparts.length > 1){
            dateString = dateparts[0];
        }

        viewHolder.txtCredit.setText(creditString);
        viewHolder.txtDebit.setText(debitString);
        viewHolder.txtTotal.setText(totalString);
        viewHolder.txtDate.setText(dateString);
        viewHolder.tvDesc.setText(item.getDescription());
        return convertView;

    }
}
