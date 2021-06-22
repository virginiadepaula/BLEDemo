package com.enautec.ble.adapter;

import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.enautec.ble.R;

import java.util.List;

public class AdapterDispositivos extends RecyclerView.Adapter{

    private List<ScanResult> scanResult;
    private Context context;
    public AdapterDispositivos(List<ScanResult> scanResults, Context context){
        this.scanResult = scanResults;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_lista, parent, false);

        MyviewHolder holder = new MyviewHolder(itemLista);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        MyviewHolder holder = (MyviewHolder) viewHolder;
        ScanResult result = scanResult.get(position);

        String nome = result.getDevice().getName();
        if(nome == null || nome.isEmpty() ){
            nome = "N/A";
        }
        holder.nome.setText(nome);
        holder.rssi.setText(String.valueOf(result.getRssi()));
        holder.mac.setText(result.getDevice().getAddress());
    }


    @Override
    public int getItemCount() {
        return scanResult.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{
        TextView nome;
        TextView rssi;
        TextView mac;

        public MyviewHolder(View itemView){
            super(itemView);
            nome = itemView.findViewById(R.id.nome);
            rssi = itemView.findViewById(R.id.rssi);
            mac = itemView.findViewById(R.id.mac);

        }
    }
}
