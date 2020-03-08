package com.example.food_safari;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_safari.model.CategoryTwo;
import com.example.food_safari.model.UploadItem;
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
import java.util.ArrayList;
import java.util.Calendar;

public class AdminNewRestaurant extends AppCompatActivity
{
    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;

    Button btn_add_to_sublist;


    EditText sub_category_edit_id,sub_category_edit_name,sub_category_edit_age;
    CategoryTwo categoryTwo;
    UploadItem uploadItem;
    ArrayList<CategoryTwo> categoryTwos =new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_restaurant);

        sub_category_edit_id = findViewById(R.id.edit_upload_sub_category_id);
        sub_category_edit_name = findViewById(R.id.edit_upload_sub_category_name);
        sub_category_edit_age = findViewById(R.id.edit_upload_sub_category_age);

        btn_add_to_sublist = findViewById(R.id.btn_add_to_subList);
        btn_add_to_sublist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sub_category_edit_id.getText().toString().isEmpty()
                        && !sub_category_edit_age.getText().toString().isEmpty()
                        && !sub_category_edit_name.getText().toString().isEmpty()) {
                    addToSubList(sub_category_edit_id.getText().toString(), sub_category_edit_name.getText().toString(), sub_category_edit_age.getText().toString());
                }
            }
        });

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Restaurant Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Restaurants");


        AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText) findViewById(R.id.Restaurant_Name);
        InputProductDescription = (EditText) findViewById(R.id.Restaurant_Timings);
        InputProductPrice = (EditText) findViewById(R.id.Restaurant_desc);
        loadingBar = new ProgressDialog(this);


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });


        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
            }
        });
    }



    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }


    private void ValidateProductData()
    {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();


        if (ImageUri == null)
        {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }



    private void StoreProductInformation()
    {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AdminNewRestaurant.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminNewRestaurant.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminNewRestaurant.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }



    private void SaveProductInfoToDatabase()
    {
       uploadItem=new UploadItem(Pname, Description, Price, CategoryName, downloadImageUrl, categoryTwos);
//        HashMap<String, Object> productMap = new HashMap<>();
//
//        productMap.put("description", Description);
//        productMap.put("image", downloadImageUrl);
//        productMap.put("category", CategoryName);
//        productMap.put("price", Price);
//        productMap.put("pname", Pname);

        ProductsRef.child(Pname).setValue(uploadItem);
        loadingBar.dismiss();
//
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task)
//                    {
//                        if (task.isSuccessful())
//                        {
//
//                            Toast.makeText(AdminNewRestaurant.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
//                            loadingBar.dismiss();
//                            String message = task.getException().toString();
//                            Toast.makeText(AdminNewRestaurant.this, "Error: " + message, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }
    private void addToSubList(String id,String name,String age)
    {
        categoryTwo = new CategoryTwo(name,id,age);
        categoryTwos.add(categoryTwo);
        sub_category_edit_id.setText("");
        sub_category_edit_age.setText("");
        sub_category_edit_name.setText("");
    }
}