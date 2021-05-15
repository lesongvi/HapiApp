package com.example.scheduleproject.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scheduleproject.ForgotPassword;
import com.example.scheduleproject.MainActivity;
import com.example.scheduleproject.R;
import com.example.scheduleproject.XemDiem;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class EditProfile extends Fragment {
    View view;
    Button Change = view.findViewById(R.id.btn_Change);
    EditText MK = view.findViewById(R.id.New);
    EditText MK_moi = view.findViewById(R.id.Authentication);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        Change.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean ketqua = false;
                        String mk = MK.getText().toString();
                        String mk_moi = MK_moi.getText().toString();
                        if (mk.equals("")) {
                            Toast.makeText(getContext(), "Nhập mật khẩu vô", Toast.LENGTH_SHORT).show();
                        } else if (mk.equals(mk_moi)) {
                            ketqua = true;
                        }


                        if (ketqua == true) {
                            //thay đổi pass
                            startActivity(new Intent(getContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getContext(), "không khớp nhau nha", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return view;
    }
}