package ru.strannik.lesson6.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import ru.strannik.lesson6.Note;
import ru.strannik.lesson6.NotesFragment;
import ru.strannik.lesson6.NotesList;
import ru.strannik.lesson6.R;
import ru.strannik.lesson6.data.CardData;
import ru.strannik.lesson6.data.CardsSource;

//класс Адаптор для RecyclerView
public class NotesListAdapter
        extends RecyclerView.Adapter<NotesListAdapter.ViewHolder> {
    private final static String TAG = "NotesListAdapter";
    private CardsSource dataSource;
    private OnItemClickListener itemClickListener; //слушатель будет устанавливаться извне
    private final Fragment fragment;
    private int menuPosition;

    public int getMenuPosition(){
        return menuPosition;
    }

    //передаем в конструктор источник данных - массив
    public NotesListAdapter(CardsSource dataSource, Fragment fragment) {
        // this.dataSource = dataSource;
        this.dataSource = dataSource;
        this.fragment = fragment;
    }

    //Создадим новый элемент пользовательского интерфейса
    //запускается менеджером
    @NonNull
    @Override
    public NotesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //создаем новый элемент через Inflater
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        Log.d(TAG, "onCreateViewHolder");
        //Здесь можно установить всякие параметры
        return new ViewHolder(v);
    }

    //заменить данные в интерфейсе, вызывается менеджером
    @Override
    public void onBindViewHolder(@NonNull NotesListAdapter.ViewHolder holder, int position) {
        //получить элемент из источника данных
        //вынести на экран, используя ViewHolder
        holder.setData(dataSource.getCardData(position));
    }


    //вернуть размер данных, вызывается менеджером
    @Override
    public int getItemCount() {
        if (NotesFragment.notesList != null) return NotesFragment.notesList.getLastIndex() + 1;
        else return 0;
    }

    public void SetOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    //интерфейс для обработки нажатий, как в ListView
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //этот класс хранит связь между данными и элементами View
    public class ViewHolder extends RecyclerView.ViewHolder {
        //private TextView textView;
        private TextView title;
        private TextView data;
        private AppCompatImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //textView = (TextView) itemView;
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.imageView);
            data = itemView.findViewById(R.id.data);
            registerContexMenu(itemView);

            //обработчик нажатий на этом ViewHolder
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
            image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    menuPosition = getLayoutPosition();
                    itemView.showContextMenu(200,20);
                    return true;
                }
            });

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
            title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    menuPosition = getLayoutPosition();
                    itemView.showContextMenu(200,50);
                    return true;
                }
            });

            data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
            data.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    menuPosition = getLayoutPosition();
                    itemView.showContextMenu(200,100);
                    return true;
                }
            });
        }

        public void registerContexMenu(@NonNull View itemView) {
            if (fragment != null)
                fragment.registerForContextMenu(itemView);
        }


        public void setData(CardData cardData) {
            title.setText(cardData.getTitle());
            data.setText(cardData.getData());

        }
    }

}
