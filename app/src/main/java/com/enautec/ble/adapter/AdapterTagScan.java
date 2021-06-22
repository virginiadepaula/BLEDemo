package com.enautec.ble.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.enautec.ble.R;
import com.enautecble.model.TagEnautec;

import java.util.List;

public class AdapterTagScan extends RecyclerView.Adapter {

    private List<TagEnautec> tagEnautecList;
    private Context context;
    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public AdapterTagScan(List<TagEnautec> tagEnautecList, Context context) {
        this.tagEnautecList = tagEnautecList;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tag_scan,parent,false);
        MyviewHolder holder = new MyviewHolder(itemLista);
        return holder;
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder viewHolder, int position) {
        MyviewHolder holder = (MyviewHolder) viewHolder;
        TagEnautec tagEnautec = tagEnautecList.get(position);
        String sTID = parse(tagEnautec.getTID());
        holder.tid.setText(sTID);
        holder.rssi.setText(String.valueOf(tagEnautec.getRSSI()));
    }

    @Override
    public int getItemCount() {
        return tagEnautecList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{
        TextView tid;
        TextView rssi;

        public MyviewHolder( View itemView) {
            super(itemView);
            tid = itemView.findViewById(R.id.tid);
            rssi = itemView.findViewById(R.id.rssi);
        }
    }

    public static String parse(final byte[] data) {
        if (data == null || data.length == 0)
            return "";

        final char[] out = new char[data.length * 3 - 1];
        for (int j = 0; j < data.length; j++) {
            int v = data[j] & 0xFF;
            out[j * 3] = HEX_ARRAY[v >>> 4];
            out[j * 3 + 1] = HEX_ARRAY[v & 0x0F];
            if (j != data.length - 1)
                out[j * 3 + 2] = '-';
        }
        return "(0x) " + new String(out);
    }
}
