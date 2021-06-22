package com.enautec.ble.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.enautec.ble.R;
import com.enautecble.model.AssetIdentification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class ActivityLeitura extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitura);

        AssetIdentification dados = (AssetIdentification) getIntent().getSerializableExtra("AssetIdentification");

        String sdatamodelversion = String.valueOf(dados.getDataModelVersion());
        TextView datamodelversion = findViewById(R.id.DataModelVersion);
        datamodelversion.setText(sdatamodelversion);

        String smanufacturingweek = String.valueOf(dados.getManufacturingWeek());
        TextView manufacturingweek = findViewById(R.id.ManufacturingWeek);
        manufacturingweek.setText(smanufacturingweek);

        String smanufacuringyear = Integer.toString(dados.getManufacuringYear());
        TextView manufacturingyear = findViewById(R.id.ManufacuringYear);
        manufacturingyear.setText(smanufacuringyear);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS zzz");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String slastwritedate = dateFormat.format(dados.getLastWriteDate());
        TextView lastwritedate = findViewById(R.id.LastWriteDate);
        lastwritedate.setText(slastwritedate);

        String sdomainidentifier = Integer.toString(dados.getDomainIdentifier());
        TextView domainidentifier = findViewById(R.id.DomainIdentifier);
        domainidentifier.setText(sdomainidentifier);

        String scompanynumber = (String.valueOf(dados.getCompanyNumber()));
        TextView companynumber = findViewById(R.id.CompanyNumber);
        companynumber.setText(scompanynumber);

        String sserialnumber = dados.getSerialNumber();
        TextView serialnumber = findViewById(R.id.SerialNumber);
        serialnumber.setText(sserialnumber);

        String sphasesquantity = Integer.toString(dados.getPhasesQuantity());
        TextView phasesquantity = findViewById(R.id.PhasesQuantity);
        phasesquantity.setText(sphasesquantity);

        String spowerrating = Float.toString(dados.getPowerRating());
        TextView powerrating = findViewById(R.id.PowerRating);
        powerrating.setText(spowerrating);
    }

}