package com.tashambra.mobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by bkoirlal on 8/8/17.
 */

public class FirstTimeFragment extends Fragment{

    private RadioButton mMaleRadio;
    private RadioButton mFemaleRadio;
    private Button mButton;
    private EditText mWeightEditText;
    private EditText mEmergencyContact;
    private EditText mEmergencyAddress;
    public SharedPreferences defaultsSharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.first_time_fragment, container, false);

        defaultsSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mMaleRadio = (RadioButton) v.findViewById(R.id.male_rb);
        mFemaleRadio = (RadioButton) v.findViewById(R.id.female_rb);
        mWeightEditText = (EditText) v.findViewById(R.id.weight_edit_text);
        mEmergencyContact = (EditText) v.findViewById(R.id.emergencyC_edit_text) ;
        mEmergencyAddress = (EditText) v.findViewById(R.id.emergencya_edit_text);

        mButton = (Button) v.findViewById(R.id.button_1);
        mButton.setOnClickListener(new View.OnClickListener() {

            //Store to DB
            //Exit the fragment with null graph
            @Override
            public void onClick(View view){
                String mGender;
                if (mMaleRadio.isChecked()){
                     mGender = "male";
                } else {
                     mGender = "female";
                }
                SharedPreferences.Editor edit = defaultsSharedPref.edit();
                edit.clear();
                edit.putString("Gender", mGender);
                edit.putString("WeightInPounds", mWeightEditText.getText().toString());
                edit.putString("EmergencyContact", mEmergencyContact.getText().toString());
                edit.putString("EmergencyAddress", mEmergencyAddress.getText().toString());
                edit.putString("SettingsDone", "1");
                edit.putString("TotalCustomDrinks", "0");
                edit.putString("CurrentBAC", "0");
                edit.apply();
                Toast.makeText(getContext(), "User details stored successfully", Toast.LENGTH_SHORT);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return v;
    }










}
