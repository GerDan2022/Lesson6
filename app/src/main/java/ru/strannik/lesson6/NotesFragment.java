package ru.strannik.lesson6;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.Inflater;

import ru.strannik.lesson6.data.CardsSource;
import ru.strannik.lesson6.data.CardsSourceImpl;
import ru.strannik.lesson6.ui.NotesListAdapter;

//Фрагмент для отображения списка ЗАМЕТОК
public class NotesFragment extends Fragment {
    private static final String CURRENT_NOTE = "currentNote";
    private int currentPosition = 0;  //текущая выбранная позиция в списке ЗАМЕТОК
    Button btn_Plus;                  //
    public static NotesList notesList = new NotesList();    //список ЗАМЕТОК
    NotesListAdapter notesAdapter; //адаптор для RecyclerView
    private int title;


    //при создании фрагмента укажем макет-----------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        //определим RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_lines);
        //зададим CardsSource
        CardsSource data = new CardsSourceImpl(getResources()).init();
        initRecyclerView(recyclerView, data);
        setHasOptionsMenu(true);
        return view;
    }

    //при создании верхнего меню--------------------------------------------------------------------
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        //визуализируем его
        menuInflater.inflate(R.menu.menu, menu);
    }

    //при выборе пунктов меню-----------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //кнопка добавить
            case R.id.action_add:
                //добавим новую ЗАМЕТКУ в массив ЗАМЕТОК
                notesList.addNote(notesList.getLastIndex() + 1, "", "", "");
                //отобразим фрагмент для новой заметки
                showCurNote(notesList.getLastIndex());
                return true;

            //кнопка Тема
            case R.id.action_theme:
                //do some stuff
                return true;

            //кнопка Очистка списка
            case R.id.action_clear:
                notesList.clearNotes();
                notesAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //при создании всплывающего меню--------------------------------------------------------------------
    @Override
    public void onCreateContextMenu(ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //визуализируем его
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.card_menu, menu);
    }

    //при выборе пунктов меню-----------------------------------------------------------------------
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //кнопка обновить Заметки
            case R.id.popup_update:
                Toast.makeText(requireActivity(), "Update notes", Toast.LENGTH_SHORT).show();
                return true;

            //кнопка Удалить заметку
            case R.id.popup_remove:
                if (notesList.getLastIndex() > 0) {
                    Toast.makeText(requireActivity(), "Remove note", Toast.LENGTH_SHORT).show();
                    int position = notesAdapter.getMenuPosition();
                    notesList.removeCurNote(position);
                    notesAdapter.notifyItemRemoved(position);
                }

                return true;

            //кнопка Очистка списка
            case R.id.action_clear:
                notesList.clearNotes();
                notesAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }


    //инициализируем RecyclerView-------------------------------------------------------------------
    private void initRecyclerView(RecyclerView recyclerView, CardsSource data) {
        //для повышения производительности системы
        recyclerView.setHasFixedSize(true);
        //будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //установим адаптер
        notesAdapter = new NotesListAdapter(data, this);
        recyclerView.setAdapter(notesAdapter);

        //добавим разделитель карточек
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);

        //установим слушателя
        notesAdapter.SetOnItemClickListener(new NotesListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //покажем текущую заметку
                showCurNote(position);
            }
        });
    }


    //после создания макета экрана, создадим список ЗАМЕТОК-----------------------------------------
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //восстановление текущей позиции
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(CURRENT_NOTE, 0);
        }
        //сделаем проверку на null агрументов Bundle
        Bundle args = getArguments();
        if (args != null) {
            //если все ок, подтянем данные из Bundle
            int index = getArguments().getInt(FragmentOfNote.ARG_INDEX);
            String title = getArguments().getString(FragmentOfNote.ARG_TITLE);
            String date = getArguments().getString(FragmentOfNote.ARG_DATE);
            String desc = getArguments().getString(FragmentOfNote.ARG_DESC);
            Boolean is_Remove = getArguments().getBoolean(String.valueOf(FragmentOfNote.ARG_IS_REMOVE));

            //заменим соответствущую запись в списке ЗАМЕТОК
            //notesList.setNote(index, title, date, desc); //выключил потому что добавляю на кнопку СОХРАНИТЬ
            //notesAdapter.notifyDataSetChanged();
            if (is_Remove) {
                if (notesList.getLastIndex() > 0)
                    notesList.removeCurNote(index);
            }
        }

        //инициализируем кнопку +
        //initButton(view);

        //отображение ранее открытой ЗАМЕТКИ
        if (isLandscape()) showLandCurNote(currentPosition);
    }


    //инициализация кнопки "+" и обработка события на нажатие---------------------------------------
    //private void initButton(View view) {
    //    btn_Plus = view.findViewById(R.id.btn_Plus);
    //    btn_Plus.setOnClickListener(new View.OnClickListener() {
    //        @Override
    //        public void onClick(View view) {
    //            //добавим новую ЗАМЕТКУ в массив ЗАМЕТОК
    //             notesList.addNote(notesList.getLastIndex() + 1, "", "", "");
    //             //отобразим фрагмент для новой заметки
    //            showCurNote(notesList.getLastIndex());
    //        }
    //    });
    //}

    //выбор отображения в зависимости от ориентации экрана------------------------------------------
    private void showCurNote(int index) {
        //проверим ориентацию экрана, если альбомная
        if (isLandscape()) {
            showLandCurNote(index);
        } else {
            //если портретная
            showPortCurNote(index);
        }
    }

    //показ текущей ЗАМЕТКИ в портретном режиме-----------------------------------------------------
    private void showPortCurNote(int index) {
        //вытащим данные по текущей ЗАМЕТКЕ
        String title = notesList.titleNote(index);
        String date = notesList.dateNote(index);
        String description = notesList.descNote(index);
        //создадим экземпляр ЗАМЕТКИ
        FragmentOfNote fragmentOfNote = FragmentOfNote.newInstance(index, title, date, description);
        //подключим FragmentManager через requireActivity потому что у нас нет к нему доступа напрямую,
        //мы работаем с фрагментом, который НЕ компонент АНДРОИД
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        //переменная для транзакции
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //добавляем фрагмент через replace в основной контейнер
        fragmentTransaction.replace(R.id.fragment_container, fragmentOfNote);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    //показ текущей ЗАМЕТКИ в альбомном режиме------------------------------------------------------
    private void showLandCurNote(int index) {
        //если массив пустой, создадим первый элемент
        if (!(notesList.isNotesListExists()))
            //добавим новую ЗАМЕТКУ в массив ЗАМЕТОК
            notesList.addNote(notesList.getLastIndex() + 1, "", "", "");
        //вытащим данные по текущей ЗАМЕТКЕ
        String title = notesList.titleNote(index);
        String date = notesList.dateNote(index);
        String description = notesList.descNote(index);
        //создадим экземпляр ЗАМЕТКИ
        FragmentOfNote fragmentOfNote = FragmentOfNote.newInstance(index, title, date, description);
        //подключим FragmentManager через requireActivity потому что у нас нет к нему доступа напрямую,
        //мы работаем с фрагментом, который НЕ компонент АНДРОИД
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        //переменная для транзакции
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //добавляем фрагмент через replace во второй (дополнительный) контейнер
        fragmentTransaction.replace(R.id.note_fragment_container, fragmentOfNote);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    //при сохранении состояния параметров-----------------------------------------------------------
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CURRENT_NOTE, currentPosition);  //сохраним текущую выбранную позицию
        super.onSaveInstanceState(outState);
    }

    //проверка на состояние экрана, если альбомный подтвердить
    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}

