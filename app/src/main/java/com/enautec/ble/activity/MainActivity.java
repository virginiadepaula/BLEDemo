package com.enautec.ble.activity;

import androidx.appcompat.app.AppCompatActivity;;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.enautec.ble.R;
import com.enautec.ble.RecyclerItemClickListener;
import com.enautec.ble.adapter.AdapterDispositivos;
import com.enautecble.EnautecBLE;
import com.enautecble.EnautecBLEInterface;
import com.enautecble.model.AssetIdentification;
import com.enautecble.model.TagEnautec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EnautecBLEInterface {


    private Handler handler = new Handler();
    private BluetoothGatt bluetoothGatt;
    private BluetoothAdapter btAdapter;
    private Button scan_button;
    private Button btnBusca;
    private Button btnLeitura;
    private Button btnEscrita;
    private RecyclerView recyclerView;
    private List<ScanResult> scanResults;
    private AdapterDispositivos scanResultAdapter;
    private boolean isScanning = false;
    EnautecBLE enautecBLE = new EnautecBLE(this, this);
    List<byte[]> writeBlock = new ArrayList<byte[]>();

   private byte[] A = new byte[]{0, 0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,00,0,0};
    private byte[] B = new byte[]{20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39};
    private byte[] C = new byte[]{24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 20, 21, 22};
    private byte[] D = new byte[]{0,1,0,1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAdapter = enautecBLE.setup();

        scan_button = (Button) findViewById(R.id.scan_button);
        btnBusca = (Button) findViewById(R.id.btnbuscar);
        btnLeitura = (Button) findViewById(R.id.btnler);
        btnEscrita = (Button) findViewById(R.id.btngravar);

        recyclerView = findViewById(R.id.recyclerView);

        scanResults = new ArrayList<ScanResult>();
        scanResultAdapter = new AdapterDispositivos(scanResults, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(scanResultAdapter);

        enautecBLE.configure(this);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                if (isScanning) {
                                    stopBleScan();
                                    Log.i("Conect", "Scaner parado.");
                                }
                                BluetoothDevice btDevice = scanResults.get(position).getDevice();
                                bluetoothGatt = enautecBLE.conectarDispositivoBLE(btDevice);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }

    protected void onResume() {
        super.onResume();
    }

    public void onClickBtn(View v) {
        if (isScanning) {
            stopBleScan();
        } else {
            startBleScan();
        }

    }

    public void onClickBusca(View v) {
        if (bluetoothGatt == null) {
            Toast.makeText(getApplicationContext(), "Dispositivo bluetooth não conectado.", Toast.LENGTH_LONG).show();
        } else {
            enautecBLE.configure(this);
            enautecBLE.escaneartags(bluetoothGatt);
        }

    }

    public void onClickLeitura(View v) {
        if (bluetoothGatt == null) {
            Toast.makeText(getApplicationContext(), "Dispositivo bluetooth não conectado.", Toast.LENGTH_LONG).show();
        } else {
            enautecBLE.configure(this);
            enautecBLE.readUserMemory(bluetoothGatt);
        }
    }

    public void onClickEscrita(View v) {
        if (bluetoothGatt == null) {
            Toast.makeText(getApplicationContext(), "Dispositivo bluetooth não conectado.", Toast.LENGTH_LONG).show();
        } else {
            writeBlock.add(A);
            writeBlock.add(B);
            writeBlock.add(C);
            writeBlock.add(D);
            enautecBLE.configure(this);
            enautecBLE.gravarcrble(bluetoothGatt,writeBlock);
        }
    }

    @Override
    public void getDadosTag(AssetIdentification assetIdentification) {
        Intent UserMemory = new Intent(this, ActivityLeitura.class);
        UserMemory.putExtra("AssetIdentification",  assetIdentification);
        startActivity(UserMemory);
    }

    @Override
    public void writeDadosTag(TagEnautec tagEnautec) {
        Intent listaTags = new Intent(MainActivity.this, ActivityTagEscrita.class);
        listaTags.putExtra("TAG", tagEnautec);
        startActivity(listaTags);
    }

    @Override
    public void buscaTags(List<TagEnautec> listaTags) {
        Intent tagsScan = new Intent(MainActivity.this, ActivityTagScan.class);
        tagsScan.putExtra("TAG", (Serializable) listaTags);
        startActivity(tagsScan);
    }

    @Override
    public void connect(boolean connected) {
        /*
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("");
        dialog.setMessage("Dispositivo conectado.");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

         */
        //Toast.makeText(getApplicationContext(), "Dispositivo conectado.", Toast.LENGTH_LONG).show();
    }

    private void startBleScan() {

        scanResults.clear();
        scanResultAdapter.notifyDataSetChanged();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BluetoothLeScanner bluetoothLeScanner = btAdapter.getBluetoothLeScanner();
                if (bluetoothLeScanner != null) {
                    bluetoothLeScanner.startScan(scanCallback);
                    isScanning = true;
                    scan_button.setText("Parar Busca");
                }
            }
        }, 1000);


    }

    private void stopBleScan() {
        BluetoothLeScanner bluetoothLeScanner = btAdapter.getBluetoothLeScanner();
        bluetoothLeScanner.stopScan(scanCallback);
        isScanning = false;
        scan_button.setText("Encontrar Dispositivos");
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            int indexQuery = 0;
            boolean existe = false;
            boolean add = false;
            if (!scanResults.isEmpty()) {
                for (int i = 0; i < scanResults.size(); i++) {
                    existe = scanResults.get(i).getDevice().equals(result.getDevice());
                    if (existe) {
                        indexQuery = scanResults.indexOf(scanResults.get(i));

                        if (indexQuery != -1) {
                            scanResults.set(indexQuery, result);
                            scanResultAdapter.notifyItemChanged(indexQuery);
                            add = true;
                        }
                    }
                }
            }
            if (!existe && !add) {
                Log.i("ScanCallback", "Dispositivo BLE encontrado: "
                        + result.getDevice().getName() + " MAC: " + result.getDevice().getAddress());
                scanResults.add(result);
                scanResultAdapter.notifyItemInserted(scanResults.size() - 1);
            }

        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("ScanCallback", "onScanFailed: code " + errorCode);
        }
    };
}




