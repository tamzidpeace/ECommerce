package com.example.arafat.e_commerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String CategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        CategoryName = getIntent().getExtras().get("category").toString();
        Toast.makeText(this, CategoryName, Toast.LENGTH_SHORT).show();
    }
}
