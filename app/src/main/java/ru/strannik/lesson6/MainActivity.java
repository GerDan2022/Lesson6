package ru.strannik.lesson6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // initToolbar();
       //создание фрагмента список ЗАМЕТОК
        NotesFragment notesFragment = new NotesFragment();
        //вызов FragmentManager и поручение на устновку notesFragment в fragment_container
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, notesFragment).commit();


    }

   /*  private void initToolbar() {
       Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

     */
}