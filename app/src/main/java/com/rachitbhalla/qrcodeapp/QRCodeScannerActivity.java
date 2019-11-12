package com.rachitbhalla.qrcodeapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeScannerActivity extends AppCompatActivity {

    Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        final Activity activity = this;

        scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setPrompt("Scan the QR Code");
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult != null){
            if(intentResult.getContents() == null){
                Toast.makeText(this, "QR Scanning Cancelled", Toast.LENGTH_LONG).show();
            }else{
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_box);
                final EditText editText = (EditText) dialog.findViewById(R.id.editText);
                editText.setText(intentResult.getContents());
                Button negativeButton = (Button) dialog.findViewById(R.id.negativeButton);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                Button positiveButton = (Button) dialog.findViewById(R.id.positiveButton);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Generating QR Code", Toast.LENGTH_SHORT).show();
                        String QRString = editText.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), QRCodeGeneratorActivity.class);
                        intent.putExtra("QRString",QRString);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
