package com.example.arafat.e_commerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arafat.e_commerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

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

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });


    }

    private void Check() {
        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String city = cityText.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address) || TextUtils.isEmpty(city)) {
            Toast.makeText(this, "Fill up all information", Toast.LENGTH_SHORT).show();
        } else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        DatabaseReference OrdersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> orderMap = new HashMap<>();

        orderMap.put("totalAmount", totalAmout);
        orderMap.put("name", nameEditText.getText().toString().trim());
        orderMap.put("phone", phoneEditText.getText().toString());
        orderMap.put("address", addressEditText.getText().toString());
        orderMap.put("city", cityText.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("state", "Not Shiped");

        OrdersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ConfirmFinalOrder.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ConfirmFinalOrder.this, HomeActivity.class));
                                    }
                                }

                            });


                } else {
                    Toast.makeText(ConfirmFinalOrder.this, "Something is going wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
