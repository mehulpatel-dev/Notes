package com.mehul.notes;

/*
Title: Notes
Author: Mehul Patel
Date: 04/06/2020
Description: A notes app which allows the user to create notes and save and delete these notes
 */
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Arraylist to link to the listview and its contents
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    //the onCreateOptionsMenu to specify the options menu for this activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //use menu inflater to link with the menu
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //onOptionsItemSelected for when an item in the options/actions menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        //check if item passed through this method (item that is selected) matches the same id in the action/options menu
        if(item.getItemId() == R.id.add_note){
            //create intent variable to jump to the NoteEditorActivity class
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);

            //start/commit the intent or the jump to the NoteEditorActivity.class
            startActivity(intent);

            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create listview variable to link to the listview created
        ListView listView = (ListView) findViewById(R.id.listView);

        //when the app opens we want to check sharedpreferences and see if there is a set there/a note saved
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("package com.mehul.notes", Context.MODE_PRIVATE);
        //retrieve the stringset which was saved as a hashset from sharedPreferences named notes
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);
        //check to see if the set is null then set the notes arraylist as the default setup so the listview will show the default setup
        //else recreate our notes arraylist and recreate it as a new arraylist based on the set
        //which was the data saved retrieved from permanent storage/shared preferences
        if(set == null){
            notes.add("Example note");
        }else {
            notes = new ArrayList<>(set);
        }

        //setup/add an initial note
        notes.add("Example notes");

        //setup array adapter for this activity using a simple list item format using array list notes as the array for the list
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        //display the contents in the arraylist via array adapter in the listview
        listView.setAdapter(arrayAdapter);

        //when user taps an item in the listview, screen jumps to the note editor
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //when item is clicked run the below method, the onItemClick method
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //create intent variable to jump to the NoteEditorActivity class
                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                //let the NoteEditorActivity know which item was tapped/selected giving it a variable
                //name notID and the value of int i which is passed to this method stating which row was tapped
                intent.putExtra("noteId", i);
                startActivity(intent);
            }
        });

        //delete note when it is long pressed
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int itemToDelete = i;

                //alert dialog box to confirm with user to delete note
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(itemToDelete);
                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("package com.mehul.notes", Context.MODE_PRIVATE);

                                //create a set/hash set of strings from the notes array list in MainActivity
                                HashSet<String> set = new HashSet<>(MainActivity.notes);
                                //save the set/hashset in sharepreferences and call it notes
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                            }
                        })

                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }
}
