package ru.strannik.lesson6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //создание фрагмента список ЗАМЕТОК
        NotesFragment notesFragment = new NotesFragment();
        //вызов FragmentManager
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, notesFragment).commit();
    }
}