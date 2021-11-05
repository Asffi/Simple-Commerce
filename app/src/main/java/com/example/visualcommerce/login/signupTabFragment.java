package com.example.visualcommerce.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.visualcommerce.MainActivity;
import com.example.visualcommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class signupTabFragment extends Fragment {
    private EditText fullName,userName,password,confrimPassword;
    private Button signUp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container,false);
        getViews(root);
        setOnClickListener();
        return root;
    }

    private void getViews(ViewGroup root) {
        userName=root.findViewById(R.id.username);
        fullName=root.findViewById(R.id.fullname);
        password=root.findViewById(R.id.Password);
        confrimPassword=root.findViewById(R.id.ConfirmPassword);
        signUp=root.findViewById(R.id.signupbtn);
    }

    private void setOnClickListener() {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String fulName=fullName.getText().toString();
        String usrName=userName.getText().toString();
        String pass=password.getText().toString();
        String cnfrmPassword=confrimPassword.getText().toString();
        if (TextUtils.isEmpty(fulName)){
            Toast.makeText(getContext(),"please must enter name!",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cnfrmPassword)){
            Toast.makeText(getContext(),"please must enter the confirm password!",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(usrName)){
            Toast.makeText(getContext(),"please must enter the user name!",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pass)){
            Toast.makeText(getContext(),"please must enter the password!",Toast.LENGTH_SHORT).show();
        }/*
        else if (!TextUtils.equals(pass,cnfrmPassword)){
            Toast.makeText(getContext(),"Password is not matched",Toast.LENGTH_SHORT).show();
        }*/
        else{
            validateUserName(fulName,usrName,pass);
        }
    }

    private void validateUserName(String fulName, String usrName, String pass) {
        if(!validateName() |!validatePassword() |  !validateUsername())
        {
            return;
        }
        final DatabaseReference root= FirebaseDatabase.getInstance().getReference();
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child(("users")).child(usrName).exists())){
                    HashMap<String, Object> userDataMap=new HashMap<>();
                    userDataMap.put("name",fulName);
                    userDataMap.put("username",usrName);
                    userDataMap.put("password",pass);
                    root.child("users").child(usrName).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(),"Register Successfully",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getContext(),"Network Error! Please try again.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(),"this "+ usrName +" is already taken please be unique",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private Boolean validateName() {
        String val = fullName.getText().toString();
        if(val.length()>20) {
            fullName.setError("name is too long");
            return false;
        }
        else {
            fullName.setError(null);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = userName.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.length() >= 15) {
            userName.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            userName.setError("White Spaces are not allowed");
            return false;
        } else {
            userName.setError(null);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = password.getText().toString();
        String val2=confrimPassword.getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (!val.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        }
        else if (!val.equals(val2)) {
            confrimPassword.setError("Password is not matched");
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    }

}
