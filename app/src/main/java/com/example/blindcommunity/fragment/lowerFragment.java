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

    ImageButton homeButton,logoutButton,myTraceButton; //노란 색의 버튼들
    ImageButton homeButton2,logoutButton2,myTraceButton2; //빨간 색의 버튼들

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
                setRedToYellow();
                homeButton2.setVisibility(View.VISIBLE);
                homeButton.setVisibility(View.INVISIBLE);

            }
        });
        logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                homeActivity.onChangeFragment(-1);
                setRedToYellow();
                logoutButton2.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.INVISIBLE);
            }
        });
        myTraceButton = view.findViewById(R.id.myTraceButton);
        myTraceButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                homeActivity.onChangeFragment(4);
                setRedToYellow();
                myTraceButton2.setVisibility(View.VISIBLE);
                myTraceButton.setVisibility(View.INVISIBLE);

            }
        });

        homeButton2 = view.findViewById(R.id.homeButton2);
        homeButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                homeActivity.onChangeFragment(0);
                setRedToYellow();
                homeButton2.setVisibility(View.VISIBLE);
                homeButton.setVisibility(View.INVISIBLE);

            }
        });
        logoutButton2 = view.findViewById(R.id.logoutButton2);
        logoutButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                homeActivity.onChangeFragment(-1);
                setRedToYellow();
                logoutButton2.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.INVISIBLE);
            }
        });
        myTraceButton2 = view.findViewById(R.id.myTraceButton2);
        myTraceButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                homeActivity.onChangeFragment(4);
                setRedToYellow();
                myTraceButton2.setVisibility(View.VISIBLE);
                myTraceButton.setVisibility(View.INVISIBLE);
            }
        });

        homeButton2.setVisibility(View.VISIBLE);
        homeButton.setVisibility(View.INVISIBLE);


        return view;
    }
    public void setRedToYellow(){
        homeButton.setVisibility(View.VISIBLE);
        logoutButton.setVisibility(View.VISIBLE);
        myTraceButton.setVisibility(View.VISIBLE);
        homeButton2.setVisibility(View.INVISIBLE);
        logoutButton2.setVisibility(View.INVISIBLE);
        myTraceButton2.setVisibility(View.INVISIBLE);
    }
}
