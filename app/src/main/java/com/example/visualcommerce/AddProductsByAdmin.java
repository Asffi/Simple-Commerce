package com.example.visualcommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.visualcommerce.model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddProductsByAdmin extends AppCompatActivity {
    private String date,time;
    private EditText productName,productDescription,productPrice,productQuantity;
    private Button productUpload;
    private ImageView productImage;
    private StorageReference productImageReference;
    private DatabaseReference productReference;
    private Products product=new Products();
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products_by_admin);

        productImageReference= FirebaseStorage.getInstance().getReference().child("Product Images");
        productReference= FirebaseDatabase.getInstance().getReference().child("products");
        setViews();
        //for adding image
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        //for uploading data in firebase
        productUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserData();
                validateProductsData();
            }
        });
        //setOnClick();
    }


    private void setViews() {
        productName=findViewById(R.id.edittext_product_name);
        productDescription=findViewById(R.id.edittext_product_description);
        productPrice=findViewById(R.id.edittext_product_price);
        productQuantity=findViewById(R.id.edittext_product_quatity);
        productUpload=findViewById(R.id.button_upload_product);
        productImage=findViewById(R.id.imageview_productview);
    }

    private void setOnClick() {

    }

    private void openGallery() {
        Intent galleryIntent= new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");// * for all type of image extension.
        startActivityForResult(galleryIntent,1);//The request code identifies the return result when the result arrives
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            productImage.setImageURI(imageUri);
        }
    }


    private void validateProductsData() {

        if (imageUri==null){
            Toast.makeText(this,"please must enter the product image!",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(product.getName())){
            Toast.makeText(this,"please must enter the product name!",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(product.getDescription())){
            Toast.makeText(this,"please must enter the Description!",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(product.getPrice())){
            Toast.makeText(this,"please must enter the Price!",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(product.getQuantity())){
            Toast.makeText(this,"please must enter the quantity of product!",Toast.LENGTH_SHORT).show();
        }
        else{
            storageProductInformation();
        }
    }

    private void storageProductInformation() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDateFormat= new SimpleDateFormat("MMM dd, yyyy");
        date=currentDateFormat.format(calendar.getTime());
        SimpleDateFormat currentTimeFormat= new SimpleDateFormat("HH:mm:ss a");
        time=currentTimeFormat.format(calendar.getTime());
        product.setPid(date+time);

        StorageReference filePath = productImageReference.child(imageUri.getLastPathSegment()+".jpg");
        UploadTask uploadTask= filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String error=e.toString();
                Toast.makeText(AddProductsByAdmin.this,"Error! "+error,Toast.LENGTH_SHORT).show();

            }

        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddProductsByAdmin.this,"Image uploaded successfully!!",Toast.LENGTH_SHORT).show();

                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        product.setImage(uri.toString());
                        productReference.push().setValue(product, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                                if (error != null) {
                                    Toast.makeText(AddProductsByAdmin.this, "Data is not Succesfully added", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(AddProductsByAdmin.this, "Data is Succesfully added", Toast.LENGTH_SHORT).show();
                                    /*Intent intent = new Intent(AddProductsByAdmin.this, Home.class);
                                    startActivity(intent);*/
                                }

                            }
                        });

                        /*HashMap productMap=new HashMap();
                        productMap.put("Description",pDescription);
                        productMap.put("Name",pName);
                        productMap.put("Price",pPrice);
                        productMap.put("Quantity",pQuantity);
                        productMap.put("image",uri.toString());
                        productMap.put("pid",pid);

                        productReference.child(pid).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Intent intent =new Intent(AddProductsByAdmin.this,Home.class);
                                    startActivity(intent);
                                    Toast.makeText(AddProductsByAdmin.this,"Products has successfully uploaded!!",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    String mas= task.getException().toString();
                                    Toast.makeText(AddProductsByAdmin.this,"Error!! "+ mas,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });*/
                    }
                });
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){

                    Toast.makeText(AddProductsByAdmin.this,"Image Url has successfully!!",Toast.LENGTH_SHORT).show();
                    //saveInfoToDatabase();
                }
            }
        });
    }
    void getUserData(){
        product.setName(productName.getText().toString());
        product.setDescription(productDescription.getText().toString());
        product.setPrice(productPrice.getText().toString());
        product.setQuantity(productQuantity.getText().toString());

    }



}
