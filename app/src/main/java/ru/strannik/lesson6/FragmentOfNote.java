package ru.strannik.lesson6;

import android.content.Intent;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class FragmentOfNote extends Fragment {

    private static final String ARG_INDEX = "index";          //текущая заметка в списке
    private static String ARG_TITLE = "title_arg";      //текущий заголовк заметки
    private static String ARG_DATE = "date_arg";        //текущая дата заметки
    private static String ARG_DESC = "description_arg"; //текущая суть заметки

    private Button btn_Back;
    private TextInputLayout titleNote;
    private TextInputLayout dateNote;
    private TextInputLayout descriptionNote;

    //констркутор не нужно объявлять, потому что он всегда должен быть пустым
    //балом рулит FragmentManager
    //данные передаются через Bundle

    //создание экземпляра фрагмента ЗАПИСКИ
    public static FragmentOfNote newInstance(int index, String title, String date, String description) {
        //создание фрагмента
        FragmentOfNote fragment = new FragmentOfNote();
        //передача парметров через БАНДЛ
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DATE, date);
        args.putString(ARG_DESC, description);
        fragment.setArguments(args);
        return fragment;
    }

    //при создании макета фрагмента
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //надуваем макет с помощью Inflater
        return inflater.inflate(R.layout.fragment_of_note, container, false);
    }

    //инициализация компонентов
    private void initViews(View view) {
        //кнопка НАЗАД
        btn_Back = view.findViewById(R.id.btn_Back);
        titleNote = view.findViewById(R.id.title_text);
        dateNote = view.findViewById(R.id.date_text);
        descriptionNote = view.findViewById(R.id.description_text);
    }

    //после создания макета фрагмента
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setOnBtnClickListener();
        Bundle arguments = getArguments();
        //сделаем проверку на null
        if (arguments != null) {
            int index = arguments.getInt(ARG_INDEX);
            String curTitle = arguments.getString(ARG_TITLE);
            String curDate = arguments.getString(ARG_DATE);
            String curDesc = arguments.getString(ARG_DESC);
            titleNote.getEditText().setText(curTitle);
            dateNote.getEditText().setText(curDate);
            descriptionNote.getEditText().setText(curDesc);

        }
    }

    //обработка нажатий на компоненты фрагмента ЗАПИСКА
    private void setOnBtnClickListener() {
        //определим текущий фрагмент
        Fragment me = this;

        //кнопка НАЗАД
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //передача парметров через БАНДЛ
                Bundle args = new Bundle();
                args.putString(ARG_TITLE, titleNote.getEditText().getText().toString());
                args.putString(ARG_DATE, dateNote.getEditText().getText().toString());
                args.putString(ARG_DESC, descriptionNote.getEditText().getText().toString());
                me.setArguments(args);

                requireActivity().getSupportFragmentManager().beginTransaction().hide(me).commitNow();
            }
        });

    }
}