package com.example.visualcommerce.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;

import com.example.visualcommerce.CurrentUser;
import com.example.visualcommerce.MainActivity;
import com.example.visualcommerce.R;
import com.example.visualcommerce.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class loginTabFragment extends Fragment {
    EditText userNameLogin, passwordLogin;
    Button buttonlogin;
    private String fullName;
    float v = 0;
    private String parentDB="users";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container,false);
        Paper.init(getContext());
        getViews(root);
        setOnClicklistener();
        return root;
    }

    private void setOnClicklistener() {
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyAccount();
            }
        });
    }

    private void verifyAccount() {
        String userName=userNameLogin.getText().toString();
        String password=passwordLogin.getText().toString();
        if (TextUtils.isEmpty(userName)){
            Toast.makeText(getContext(),"please must enter the user name!",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(getContext(),"please must enter the password!",Toast.LENGTH_SHORT).show();
        }
        else{
            allowAccess(userName,password);
        }
    }

    private void allowAccess(String userName, String password) {
//        if(remember.isChecked()){
//        }

            Paper.book().write(CurrentUser.userName,userName);
            Paper.book().write(CurrentUser.password,password);
        final DatabaseReference root= FirebaseDatabase.getInstance().getReference();
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDB).child(userName).exists()) {
                    User userData= snapshot.child(parentDB).child(userName).getValue(User.class);
                    if (userData.getUsername().equals(userName)) {
                        if (userData.getPassword().equals(password)){
                            Toast.makeText(getContext(), "Login Successfull", Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(getContext(), MainActivity.class);
                           startActivity(intent);
                        }
                        else {
                            Toast.makeText(getContext(), "Password is not matched", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "User name is not matched", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "This User name is not exists", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getViews(ViewGroup root) {
        userNameLogin=root.findViewById(R.id.user_name_login);
        passwordLogin=root.findViewById(R.id.password_login);
        buttonlogin=root.findViewById(R.id.loginbtn);
    }

}
