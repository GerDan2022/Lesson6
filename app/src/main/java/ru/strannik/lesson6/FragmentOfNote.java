package ru.strannik.lesson6;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//Фрагмент для отображения ЗАМЕТКИ
public class FragmentOfNote extends Fragment {

    static final String ARG_INDEX = "index";          //текущая заметка в списке
    static final String ARG_TITLE = "title_arg";      //текущий заголовк заметки
    static final String ARG_DATE = "date_arg";        //текущая дата заметки
    static final String ARG_DESC = "description_arg"; //текущая суть заметки
    static final Boolean ARG_IS_REMOVE = false;
    private int curIndex;

    private Button btn_Back;
    private Button btn_Save;
    private Button btn_Remove;
    private TextInputLayout titleNote;
    private Date dateNote;
    private TextInputLayout descriptionNote;
    private DatePicker datePicker;

    //констркутор не нужно объявлять, потому что он всегда должен быть пустым
    //балом рулит FragmentManager
    //данные передаются через Bundle

    //создание экземпляра фрагмента ЗАПИСКИ---------------------------------------------------------
    public static FragmentOfNote newInstance(int index, String title, String date, String description) {
        //создание фрагмента
        FragmentOfNote fragment = new FragmentOfNote();
        //передача парметров через БАНДЛ
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DATE, date);
        args.putString(ARG_DESC, description);
        args.putBoolean(String.valueOf(ARG_IS_REMOVE), false);
        fragment.setArguments(args);
        return fragment;
    }

    //при создании макета фрагмента-----------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        //надуваем макет с помощью Inflater
        View rootView = inflater.inflate(R.layout.fragment_of_note, container, false);
        //initToolBar(rootView);
        return rootView;
    }

 /*   private void initToolBar(View view) {
       //Toolbar toolbar = view.findViewById(R.id.toolbar);
       // ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_note);
        Menu menu = toolbar.getMenu();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
            }
        });
    }

  */

    //при создании верхнего меню--------------------------------------------------------------------
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        //визуализируем его
        menuInflater.inflate(R.menu.menu_note, menu);
    }

    //при выборе пунктов меню-----------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //кнопка НАЗАД
            case R.id.action_back:
                pressBackBtn();
                break;

            //кнопка сохранить
            case R.id.action_save:
                btn_Save.callOnClick();
                break;

            //кнопка удалить
            case R.id.action_remove:
                btn_Remove.callOnClick();
                break;

            //кнопка Тема
            case R.id.action_theme:
                //do some stuff
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    //после создания макета фрагмента---------------------------------------------------------------
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);                              //инициализация компонентов
        setOnViewsClickListener();                      //обработка событий нажатий на компоненты
        Bundle arguments = getArguments();            //доступ к Bundle
        //сделаем проверку на null агрументов Bundle
        if (arguments != null) {
            //в поля ЗАМЕТКИ установим значения из БАНДЛ
            curIndex = arguments.getInt(ARG_INDEX);
            String curTitle = arguments.getString(ARG_TITLE);
            String curDate = arguments.getString(ARG_DATE);
            String curDesc = arguments.getString(ARG_DESC);
            titleNote.getEditText().setText(curTitle);
            dateNote = stringToDate(curDate, "dd-MM-yyyy");
            initDatePicker(dateNote);
            descriptionNote.getEditText().setText(curDesc);
        }
    }

    //инициализация компонентов---------------------------------------------------------------------
    private void initViews(View view) {
        btn_Save = view.findViewById(R.id.btn_save);              //кнопка СОХРАНИТЬ
        btn_Remove = view.findViewById(R.id.btn_remove);          //кнопка УДАЛИТЬ
        titleNote = view.findViewById(R.id.title_text);
        //dateNote = view.findViewById(R.id.date_text);
        descriptionNote = view.findViewById(R.id.description_text);
        datePicker = view.findViewById(R.id.datePicker);
    }

    //обработка нажатий на компоненты фрагмента ЗАПИСКА---------------------------------------------
    private void setOnViewsClickListener() {
        //определим фрагмент который нужно отобразить (СПИСОК ЗАМЕТОК)
        Fragment notesFragment = new NotesFragment();

        //кнопка СОХРАНИТЬ
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //передача парметров через БАНДЛ
                Bundle args = new Bundle();   //получим доступ к Bundle
                //сохраним значения полей в БАНДЛ
                args.putInt(ARG_INDEX, curIndex);
                if (titleNote.getEditText().getText().toString().equals("")) titleNote.getEditText()
                        .setText(titleNote.getHint().toString());
                args.putString(ARG_TITLE, titleNote.getEditText().getText().toString());
                args.putString(ARG_DATE, getDateFromDatePicker());
                args.putString(ARG_DESC, descriptionNote.getEditText().getText().toString());
                //устновим агрументы для отображаемого фрагмента (список ЗАМЕТОК)
                notesFragment.setArguments(args);

                //тут обновим массив заметок и в целом передача данных через БАНДЛ уже и не нужна
                String curTitle = titleNote.getEditText().getText().toString();
                String curDate = getDateFromDatePicker();
                String curDesc = descriptionNote.getEditText().getText().toString();
                NotesFragment.notesList.setNote(curIndex, curTitle, curDate, curDesc);

                //доступ к ФМ получим через requireActivity, потому что это ФРАГМЕНТ
                //откроем транзакцию с помощью ФМ и заменим с помощью replace текущий фрагмент на
                //новый - СПИСОК ЗАМЕТОК
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, notesFragment).commitNow();
            }
        });

        //кнопка УДАЛИТЬ
        btn_Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                final List<Fragment> fragments = fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof FragmentOfNote && fragment.isVisible()) {
                        //передача парметров через БАНДЛ
                        Bundle args = new Bundle();   //получим доступ к Bundle
                        //сохраним значения полей в БАНДЛ
                        args.putInt(ARG_INDEX, curIndex);
                        if (titleNote.getEditText().getText().toString().equals(""))
                            titleNote.getEditText()
                                    .setText(titleNote.getHint().toString());
                        args.putString(ARG_TITLE, titleNote.getEditText().getText().toString());
                        args.putString(ARG_DATE, getDateFromDatePicker());
                        args.putString(ARG_DESC, descriptionNote.getEditText().getText().toString());
                        args.putBoolean(String.valueOf(ARG_IS_REMOVE), true);
                        //устновим агрументы для отображаемого фрагмента (список ЗАМЕТОК)
                        notesFragment.setArguments(args);
                        //заменим фрагмент
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, notesFragment).commitNow();
                    }
                }
            }
        });

        //поле ДАТЫ
     /*   calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                StringBuilder userDate = new StringBuilder().append(i2).append(".").append(i1+1).append(".").append(i);
                dateNote.getEditText().setText(userDate);
            }
        });
      */
    }

    //при нажатии кнопки меню НАЗАД-----------------------------------------------------------------
    private void pressBackBtn(){
        Fragment notesFragment = new NotesFragment();
        //доступ к ФМ получим через requireActivity, потому что это ФРАГМЕНТ
        //откроем транзакцию с помощью ФМ и заменим с помощью replace текущий фрагмент на
        //новый - СПИСОК ЗАМЕТОК
        final FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        final List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof FragmentOfNote && fragment.isVisible()) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, notesFragment).commitNow();
            }
        }
    }

    //получение даты из DatePicker------------------------------------------------------------------
    private String getDateFromDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, this.datePicker.getYear());
        cal.set(Calendar.MONTH, this.datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, this.datePicker.getDayOfMonth());
        return cal.getTime().toString();
    }

    //установка даты в DatePicker-------------------------------------------------------------------
    private void initDatePicker(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
            this.datePicker.init(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH), null);
        } else {
            date = calendar.getTime();
            calendar.setTime(date);
            this.datePicker.init(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH), null);

        }
    }

    //перевод String в Date-------------------------------------------------------------------------
    private Date stringToDate(String aDate, String aFormat) {
        if (aDate == null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;
    }
}