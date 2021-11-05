package com.example.visualcommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class ConfirmFinalOrderAcivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView textView;

    String[] cities = { "Rawalpindi", "Islamabad", "Lahore"};
    String[] codes={"46000","42000","44000"};
    private EditText name,phone,address;
    private Spinner city,postalCode;
    private Button confrimOrder;
    private String  totalPrice="",userName;
    private String date,time,randomKey,Text,Text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order_acivity);
        getViews();
        Paper.init(this);
        userName=Paper.book().read(CurrentUser.userName);
        totalPrice=getIntent().getStringExtra("total price");
        setClickListener();
        Paper.init(this);
        userName=Paper.book().read(CurrentUser.userName);
        Spinner spin = (Spinner) findViewById(R.id.cnfrm_order_city);
        spin.setOnItemSelectedListener(ConfirmFinalOrderAcivity.this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,cities);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        Spinner spinn = (Spinner) findViewById(R.id.cnfrm_order_postal_code);
        spinn.setOnItemSelectedListener(this);
        ArrayAdapter aaa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,codes);
        aaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinn.setAdapter(aaa);
        Text = spin.getSelectedItem().toString();
        Text1 = spinn.getSelectedItem().toString();
    }

    private void setClickListener() {
        confrimOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private void check() {
        if (TextUtils.isEmpty(name.getText().toString())){
            Toast.makeText(this,"Name's field is empty!",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone.getText().toString())){
            Toast.makeText(this,"Phone number's field is empty!",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(address.getText().toString())){
            Toast.makeText(this,"Address's field is empty!",Toast.LENGTH_SHORT).show();
        }
        else {
            confirmOrder();
        }

    }

    private void confirmOrder() {
        if(!validatePhoneNo()){
            return;
        }
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDateFormat= new SimpleDateFormat("MMM dd, yyyy");
        date=currentDateFormat.format(calendar.getTime());
        SimpleDateFormat currentTimeFormat= new SimpleDateFormat("HH:mm:ss a");
        time=currentTimeFormat.format(calendar.getTime());
        randomKey=date+time;
        DatabaseReference cartReference= FirebaseDatabase.getInstance().getReference().child("cart list");
        DatabaseReference orderReference= FirebaseDatabase.getInstance().getReference().child("Order");
        HashMap<String,Object> orderMap= new HashMap<>();
        orderMap.put("totalAmount",totalPrice);
        orderMap.put("orderId",randomKey);
        orderMap.put("name",name.getText().toString());
        orderMap.put("Phone",phone.getText().toString());
        orderMap.put("city",Text);
        orderMap.put("postalCode",Text1);
        orderMap.put("address",address.getText().toString());
        orderMap.put("date",date);
        orderMap.put("time",time);
        orderMap.put("state","Not shipped");

        orderReference.child(userName).
                child("orders").
                child(randomKey).updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    cartReference.child(userName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                               // Toast.makeText(ConfirmFinalOrderAcivity.this,"Data is successfully deleted",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(ConfirmFinalOrderAcivity.this,"Data has been uploaded.",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ConfirmFinalOrderAcivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private Boolean validatePhoneNo() {
        String val = phone.getText().toString();

        if (val.isEmpty()) {
            phone.setError("Field cannot be empty");
            return false;
        }else if (val.length()<11){
            phone.setError("Phone not less then 11 integers");
            return false;

        }
        else if (val.length()>11){
            phone.setError("Phone not greater then 11 integers");
            return false;

        }
        else {
            phone.setError(null);
            return true;
        }
    }

    private void getViews() {
        textView=findViewById(R.id.textview);
        name=findViewById(R.id.cnfrm_order_name);
        phone=findViewById(R.id.cnfrm_order_phone_number);
        city=findViewById(R.id.cnfrm_order_city);
        postalCode=findViewById(R.id.cnfrm_order_postal_code);
        address=findViewById(R.id.cnfrm_order_address);
        confrimOrder=findViewById(R.id.btn_cnfrm_order_final);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}