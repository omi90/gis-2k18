package com.udacity.googleindiascholarships.challenges.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.googleindiascholarships.R;
import com.udacity.googleindiascholarships.challenges.ui.adapter.ChallengesAdapter;
import com.udacity.googleindiascholarships.challenges.entities.Challenge;
import com.udacity.googleindiascholarships.challenges.ui.adapter.ChallengesListAdapter;
import com.udacity.googleindiascholarships.projects.entities.Project;
import com.udacity.googleindiascholarships.projects.ui.adapter.ProjectsAdapter;
import com.udacity.googleindiascholarships.utils.Constants;

import java.util.ArrayList;

/**
 * Created by jha.anuj.2108 on 13-04-2018.
 */

public class ChallengesFragment extends android.support.v4.app.Fragment {

    RecyclerView challengeRecyclerView;
    ArrayList<Challenge> challengeList;
    ChallengesListAdapter challengesAdapter;


    //Firebase Variable Declaration
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mFirebaseDatabaseReference;
    ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_challenges, container, false);


        challengeRecyclerView = rootView.findViewById(R.id.challenges_recyclerView);
        challengeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProgressBar = rootView.findViewById(R.id.progress_barChallenges);


        challengeList = new ArrayList<Challenge>();

        if(checkInternetConnectivity()){
            readChallengesFirebase();
        }else{
            Toast.makeText(getContext(),"No internet connection",Toast.LENGTH_LONG).show();

        }



        return rootView;
    }


    void  readChallengesFirebase(){

        mFirebaseDatabase = FirebaseDatabase.getInstance(Constants.DATABASE_URL);
        mFirebaseDatabaseReference = mFirebaseDatabase.getReference("challenges").child("challenge_list");
        mFirebaseDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                challengeList.clear();
                for(DataSnapshot projectSnapshot : dataSnapshot.getChildren()){

                    Challenge challenge = projectSnapshot.getValue(Challenge.class);
                    challengeList.add(challenge);

                }

                challengesAdapter = new ChallengesListAdapter(getContext(), challengeList);
                challengeRecyclerView.setAdapter(challengesAdapter);
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Challenges");
    }


    public boolean checkInternetConnectivity(){
        //Check internet connection//
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network//
        NetworkInfo netInformation = connectivityManager.getActiveNetworkInfo();
        // If there is a network connection, then fetch data//
        if(netInformation!=null && netInformation.isConnected()){
            return true;
        }else{
            return false;
        }
    }
}
