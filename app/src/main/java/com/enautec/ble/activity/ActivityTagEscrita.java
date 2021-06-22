package com.enautec.ble.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.enautec.ble.R;
import com.enautecble.model.TagEnautec;

public class ActivityTagEscrita extends AppCompatActivity {

    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_escrita);

        TagEnautec dados = (TagEnautec) getIntent().getSerializableExtra("TAG");

        byte[] tid = dados.getTID();
        String sTID = parse(tid);
        TextView textTID = findViewById(R.id.TID);
        textTID.setText(sTID);

        short rssi = dados.getRSSI();
        String sRSSI = Short.toString(rssi);
        TextView textRSSI = findViewById(R.id.RSSI);
        textRSSI.setText(sRSSI);
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