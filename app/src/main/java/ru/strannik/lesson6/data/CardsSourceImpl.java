package ru.strannik.lesson6.data;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import ru.strannik.lesson6.NotesFragment;

public class CardsSourceImpl implements CardsSource{
    private List<CardData> dataSource;
    private Resources resources;

    public CardsSourceImpl(Resources resources){
        dataSource = new ArrayList<>(7);
        this.resources = resources;
    }

    public CardsSourceImpl init(){
        for (int i = 0; i < NotesFragment.notesList.getLastIndex()+1; i++) {
            String title = NotesFragment.notesList.titleNote(i);
            String data = NotesFragment.notesList.dateNote(i);
            dataSource.add(new CardData(title, data));

        }
        return this;
    }

    public CardData getCardData (int position){
        return dataSource.get(position);
    }

    @Override
    public int size() {
        return dataSource.size();
    }
}
