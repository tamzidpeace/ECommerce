package com.example.arafat.e_commerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private static final String TAG = "AdminAddNewProductActivity";
    private static final int GALLERY_PICK = 1;
    private Uri ImageUri;
    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime;
    private String productRandomKey, downloadImageUrl;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private Button AddNewProductButton;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        InputProductImage = findViewById(R.id.select_product_image);
        InputProductName = findViewById(R.id.product_name);
        InputProductDescription = findViewById(R.id.product_description);
        InputProductPrice = findViewById(R.id.product_price);
        AddNewProductButton = findViewById(R.id.add_new_product);
        loadingBar = new ProgressDialog(this);


        CategoryName = getIntent().getExtras().get("category").toString();
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        ProductImagesRef = FirebaseStorage.getInstance().getReference()
                .child("Product Images");

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();
            }
        });

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminAddNewProductActivity.this, CategoryName, Toast.LENGTH_SHORT).show();

                ValidateProductData();
            }
        });

    }


    private void openGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {

            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }

    private void ValidateProductData() {

        Pname = InputProductName.getText().toString().trim();
        Description = InputProductDescription.getText().toString().trim();
        Price = InputProductPrice.getText().toString().trim();

        if(ImageUri == null) {

            Toast.makeText(this, "Product image is mandatory", Toast.LENGTH_SHORT).show();

        } else if(TextUtils.isEmpty(Pname)) {

            Toast.makeText(this, "Enter product name", Toast.LENGTH_SHORT).show();

        } else if(TextUtils.isEmpty(Description)) {

            Toast.makeText(this, "Enter product description", Toast.LENGTH_SHORT).show();

        } else if(TextUtils.isEmpty(Price)) {

            Toast.makeText(this, "Enter product price", Toast.LENGTH_SHORT).show();

        } else {

            StoreProductInformation();

        }
    }

    private void StoreProductInformation() {

        loadingBar.setTitle("Adding new Product");
        loadingBar.setMessage("Please wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");

        saveCurrentDate = currentDate.format(calendar.getTime());
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImagesRef
                .child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AdminAddNewProductActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()) {

                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful()) {

                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddNewProductActivity.this, "got image url", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(AdminAddNewProductActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });
    }

    private void SaveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", CategoryName);
        productMap.put("price", Price);
        productMap.put("pname", Pname);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()) {

                            startActivity(new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class));

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                        } else {

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
