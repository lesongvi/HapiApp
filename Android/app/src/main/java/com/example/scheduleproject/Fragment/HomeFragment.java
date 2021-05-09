package com.example.scheduleproject.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scheduleproject.LichThi;
import com.example.scheduleproject.R;
import com.example.scheduleproject.RLuyen;
import com.example.scheduleproject.TKB;
import com.example.scheduleproject.XemDiem;

public class HomeFragment extends Fragment {
    Button btn_thongtin, btn_xemdiem, btn_thoi_khoa_bieu, btn_lich_thi;
    TextView btn_renluyen;
   View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        btn_thongtin =view.findViewById(R.id.btn_thongtin);
        btn_xemdiem = view.findViewById(R.id.btn_diem);
        btn_lich_thi = view.findViewById(R.id.btn_lichthi);
        btn_thoi_khoa_bieu = view.findViewById(R.id.btn_thoikhoabieu);
        btn_renluyen = view.findViewById(R.id.btn_dgrl);

        btn_xemdiem.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), XemDiem.class));
        });

        btn_thoi_khoa_bieu.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), TKB.class));
        });

        btn_lich_thi.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), LichThi.class));
        });

        btn_renluyen.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), RLuyen.class));
        });
        return view;
    }

}