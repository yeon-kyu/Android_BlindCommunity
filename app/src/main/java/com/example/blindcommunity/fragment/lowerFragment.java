package com.example.blindcommunity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.blindcommunity.R;
import com.example.blindcommunity.UI.HomeActivity;

public class lowerFragment extends Fragment {

    HomeActivity homeActivity;

    ImageButton homeButton,logoutButton,myTraceButton;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.fragment_lower,container,false);

        homeButton = view.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                homeActivity.onChangeFragment(0);

            }
        });
        logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                homeActivity.onChangeFragment(-1);

            }
        });
        myTraceButton = view.findViewById(R.id.myTraceButton);
        myTraceButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                homeActivity.onChangeFragment(4);

            }
        });


        return view;
    }
}
