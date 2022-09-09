package ru.strannik.lesson6;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class NotesFragment extends Fragment{
    Button btn_Plus;

    public ArrayList<Note> notesList = new ArrayList(); //список ЗАМЕТОК


    //при создании фрагмента укажем макет
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    //после создания макета экрана, создадим список ЗАМЕТОК
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //сделаем проверку на null
        if (getArguments() != null) {
            int index = getArguments().getInt("ARG_INDEX");
            String title = getArguments().getString("ARG_TITLE");
            String date = getArguments().getString("ARG_DATE");
            String desc = getArguments().getString("ARG_DESC");
            Note note = new Note(title, date, desc);
            notesList.set(index, note);
        }
        initNotes(view);
        initButton(view);
    }

    //инициализация кнопки "+" и обработка события на нажатие
    private void initButton(View view) {
        btn_Plus = view.findViewById(R.id.btn_Plus);
        btn_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //добавим новую ЗАМЕТКУ в массив ЗАМЕТОК
                Note curNote = new Note();
                notesList.add(curNote);
                //отобразим фрагмент для новой заметки
                showCurNote(notesList.size()-1, "", "", "");
            }
        });
    }

    //создание списка ЗАМЕТОК
    private void initNotes(View view) {
        Note firstNote = new Note("Первая заметка", "Трудно сказать что-то", "07.09.2022");
        Note secondNote = new Note("Вторая заметка", "Трудно сказать что-то", "07.09.2022");
        notesList.add(0,firstNote);
        notesList.add(1,secondNote);
        if (view instanceof LinearLayout) {
            LinearLayout layoutView = (LinearLayout) view;

            for (int i = 0; i < notesList.size(); i++) {
                TextView tv = new TextView(getContext());
                tv.setText(notesList.get(i).title);
                tv.setTextSize(30);
                layoutView.addView(tv);
                final int position = i;
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //вытащим данные по текущей ЗАМЕТКЕ
                        String title = notesList.get(position).title;
                        String date = notesList.get(position).dateOfCreate;
                        String description = notesList.get(position).description;
                        //отобразим текущую ЗАМЕТКУ
                        showCurNote(position, title, date, description);
                    }
                });
            }
        }

    }
    //показ текущей ЗАМЕТКИ
    private void showCurNote(int index, String title, String date, String description){
        //определим текущую заметку
        FragmentOfNote fragmentOfNote = FragmentOfNote.newInstance(index, title, date, description);
        //подключим FragmentManager через requireActivity потому что у нас нет к нему доступа напрямую,
        //мы работаем с фрагментом, который НЕ компонент АНДРОИД
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        //переменная для транзакции
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //добавляем фрагмент через ADD
        fragmentTransaction.add(R.id.fragment_container, fragmentOfNote);
        fragmentTransaction.commit();

    }
}