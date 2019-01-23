package com.example.arafat.e_commerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmFinalOrder extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText, cityText;
    private String totalAmout = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        nameEditText = findViewById(R.id.shipment_name);
        phoneEditText = findViewById(R.id.shipment_phone_number);
        addressEditText = findViewById(R.id.shipment_address);
        cityText = findViewById(R.id.shipment_city);

        Button confirmOrderBtn = findViewById(R.id.confirm_final_order_btn);

        totalAmout = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price" + totalAmout, Toast.LENGTH_SHORT).show();
        TextView totalPriceTextView = findViewById(R.id.total_price_text_view);
        totalPriceTextView.setText("Total Price: " + totalAmout + "$");

        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String city = cityText.getText().toString();


    }
}
