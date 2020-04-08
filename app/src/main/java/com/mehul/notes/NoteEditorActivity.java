package com.mehul.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashMap;
import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {
    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = (EditText) findViewById(R.id.editText);

        //get the variable passed to this class from MainActivity
        Intent intent = getIntent();
        //create int variable to retrieve the integer value being passed from the MainActivity intent
        noteId = intent.getIntExtra("noteId", -1);

        //do a check to see if noteId is not -1, which we set as default value
        if(noteId != -1){
            //get the value of the notes arraylist in MainActivity and display it in the EditText
            editText.setText(MainActivity.notes.get(noteId));
        } else {
            //add a new note
            MainActivity.notes.add("");

            //give it a noteId so we can access it
            noteId = MainActivity.notes.size() - 1;

            //update the arrayadapter to display it in the listview
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }

        //save the note as the user edits it by doing something when the editText is changed.
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //updated notes arraylist from Main Activity
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.notes.set(noteId, String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("package com.mehul.notes", Context.MODE_PRIVATE);

                //create a set/hash set of strings from the notes array list in MainActivity
                HashSet<String> set = new HashSet<>(MainActivity.notes);
                //save the set/hashset in sharepreferences and call it notes
                sharedPreferences.edit().putStringSet("notes", set).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
