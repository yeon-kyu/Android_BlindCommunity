package com.example.blindcommunity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.blindcommunity.R;
import com.example.blindcommunity.UI.HomeActivity;


public class HomeScreenFragment extends Fragment {

    HomeActivity homeActivity;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        homeActivity = (HomeActivity)getActivity();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        homeActivity = null;
    }

    Button freeBtn, infoBtn,employeeBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_mainscreen,container,false);

        freeBtn = view.findViewById(R.id.freePost);
        freeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                homeActivity.onChangeFragment(1);

            }
        });
        infoBtn = view.findViewById(R.id.infoPost);
        infoBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                homeActivity.onChangeFragment(2);

            }
        });
        employeeBtn = view.findViewById(R.id.employeePost);
        employeeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                homeActivity.onChangeFragment(3);

            }
        });

        return view;
    }
}
