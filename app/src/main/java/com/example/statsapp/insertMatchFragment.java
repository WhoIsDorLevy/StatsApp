package com.example.statsapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.example.statsapp.databinding.FragmentInsertMatchBinding;

import static com.example.statsapp.AppDB.ASSISTS_TABLE_NAME;
import static com.example.statsapp.AppDB.COLUMN_ID;
import static com.example.statsapp.AppDB.H_GOALS_TABLE_NAME;
import static com.example.statsapp.AppDB.L_GOALS_TABLE_NAME;
import static com.example.statsapp.AppDB.L_PENALTIES_MADE_TABLE_NAME;
import static com.example.statsapp.AppDB.L_PENALTIES_MISSED_TABLE_NAME;
import static com.example.statsapp.AppDB.MATCHES_TABLE_NAME;
import static com.example.statsapp.AppDB.MATCH_DIFFICULTY_TABLE_NAME;
import static com.example.statsapp.AppDB.R_GOALS_TABLE_NAME;
import static com.example.statsapp.AppDB.R_PENALTIES_MADE_TABLE_NAME;
import static com.example.statsapp.AppDB.R_PENALTIES_MISSED_TABLE_NAME;

public class insertMatchFragment extends Fragment {

    private FragmentInsertMatchBinding binding;
    private SQLiteDatabase db;
    private String matchDifVal, rGoalsVal, lGoalsVal, hGoalsVal, assistsVal,
            rPenaltiesMadeVal, lPenaltiesMadeVal, rPenaltiesMissedVal, lPenaltiesMissedVal;
    private EditText[] editTexts;
    private boolean[] isValid;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        setDB();

        binding = FragmentInsertMatchBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    private void setDB(){
        MainActivity activity = (MainActivity) getActivity();
        db = activity.getDb().getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + MATCHES_TABLE_NAME + " (\n" +
                COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT,\n" +
                MATCH_DIFFICULTY_TABLE_NAME +" INTEGER NOT NULL,\n" +
                R_GOALS_TABLE_NAME + " integer NOT NULL,\n" +
                L_GOALS_TABLE_NAME + " integer NOT NULL,\n" +
                H_GOALS_TABLE_NAME + " integer NOT NULL,\n" +
                ASSISTS_TABLE_NAME + " integer NOT NULL,\n" +
                R_PENALTIES_MADE_TABLE_NAME + " integer NOT NULL,\n" +
                L_PENALTIES_MADE_TABLE_NAME + " integer NOT NULL,\n" +
                R_PENALTIES_MISSED_TABLE_NAME + " integer NOT NULL,\n" +
                L_PENALTIES_MISSED_TABLE_NAME + " integer NOT NULL" +
                ");");
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonEnter.setOnClickListener(view12 -> {
            getInputs();
            uploadStats();
            Toast.makeText(getActivity(), R.string.match_added, Toast.LENGTH_LONG).show();
            NavHostFragment.findNavController(insertMatchFragment.this)
                    .navigate(R.id.action_insertMatchFragment_to_FirstFragment);
        });

        setEditTexts();



    }

    private void setEditTexts(){
        isValid = new boolean[9];

        editTexts = new EditText[]{binding.matchDifEditText, binding.RGoalsEditText,
                binding.LGoalsEditText, binding.HGoalsEditText, binding.AssistsEditText,
                binding.RPenaltiesMadeEditText, binding.LPenaltiesMadeEditText,
                binding.RPenaltiesMissedEditText, binding.LPenaltiesMissedEditText};

        binding.matchDifEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String input = binding.matchDifEditText.getText().toString();
                if (checkValidNumber(input) && checkNumberInRange(input)){
                    isValid[0] = true;
                }
                else {
                    binding.matchDifEditText.setError("Must be a valid number between 1-5");
                    isValid[0] = false;
                }
                checkValidity();
            }
        });

        for (int ind = 1; ind < editTexts.length; ind++){
            int finalInd = ind;
            isValid[ind] = true;
            editTexts[ind].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void afterTextChanged(Editable editable) {
                    String input = editTexts[finalInd].getText().toString();
                    if (!checkValidNumber(input)){
                        editTexts[finalInd].setError("Must be a valid and positive number, or empty");
                        isValid[finalInd] = false;
                    }
                    else {
                        isValid[finalInd] = true;
                    }
                    checkValidity();
                }
            });
        }
    }

    private void checkValidity(){
        boolean toEnable = true;
        for (boolean valid : isValid){
            toEnable &= valid;
        }
        binding.buttonEnter.setEnabled(toEnable);
    }

    private boolean checkNumberInRange(String str){
        int num = (str.isEmpty()) ? 0 : Integer.parseInt(str);
        return 1 <= num & num <= 5;
    }

    private boolean checkValidNumber(String str){
        if (str.isEmpty()){
            return true;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    private boolean isEmptyEditText(EditText ed){
        return TextUtils.isEmpty(ed.getText());
    }

    private void getInputs(){
        matchDifVal = (isEmptyEditText(binding.matchDifEditText)) ? "0" : binding.matchDifEditText.getText().toString();
        rGoalsVal =  (isEmptyEditText(binding.RGoalsEditText)) ? "0" : binding.RGoalsEditText.getText().toString();
        lGoalsVal =  (isEmptyEditText(binding.LGoalsEditText)) ? "0" : binding.LGoalsEditText.getText().toString();
        hGoalsVal =  (isEmptyEditText(binding.HGoalsEditText)) ? "0" : binding.HGoalsEditText.getText().toString();
        assistsVal = (isEmptyEditText(binding.AssistsEditText)) ? "0" : binding.AssistsEditText.getText().toString();
        rPenaltiesMadeVal = (isEmptyEditText(binding.RPenaltiesMadeEditText)) ? "0" : binding.RPenaltiesMadeEditText.getText().toString();
        lPenaltiesMadeVal = (isEmptyEditText(binding.LPenaltiesMadeEditText)) ? "0" : binding.LPenaltiesMadeEditText.getText().toString();
        rPenaltiesMissedVal = (isEmptyEditText(binding.RPenaltiesMissedEditText)) ? "0" : binding.RPenaltiesMissedEditText.getText().toString();
        lPenaltiesMissedVal = (isEmptyEditText(binding.LPenaltiesMissedEditText)) ? "0" : binding.LPenaltiesMissedEditText.getText().toString();



    }

    private void uploadStats(){
        ContentValues values = new ContentValues();

        values.put(AppDB.MATCH_DIFFICULTY_TABLE_NAME, matchDifVal);
        values.put(AppDB.R_GOALS_TABLE_NAME, rGoalsVal);
        values.put(AppDB.L_GOALS_TABLE_NAME, lGoalsVal);
        values.put(AppDB.H_GOALS_TABLE_NAME, hGoalsVal);
        values.put(AppDB.ASSISTS_TABLE_NAME, assistsVal);
        values.put(AppDB.R_PENALTIES_MADE_TABLE_NAME, rPenaltiesMadeVal);
        values.put(AppDB.L_PENALTIES_MADE_TABLE_NAME, lPenaltiesMadeVal);
        values.put(AppDB.R_PENALTIES_MISSED_TABLE_NAME, rPenaltiesMissedVal);
        values.put(AppDB.L_PENALTIES_MISSED_TABLE_NAME, lPenaltiesMissedVal);

        db.insert(AppDB.MATCHES_TABLE_NAME, null, values);



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        db.close();
    }


}