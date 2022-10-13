package ru.strannik.lesson6;

import java.util.ArrayList;

//вспомагательный класс для работы со списком ЗАМЕТОК, реализованным через ArrayList
public class NotesList {
    //список ЗАМЕТОК
    public static ArrayList<Note> notesList = new ArrayList();


    //добавить ЗАМЕТКУ
    public void addNote( Note note){
        notesList.add(note);
    }

    //добавить ЗАМЕТКУ
    public void addNote(int index, String title, String dateOfCreation, String description){
        Note curNote = new Note(title, dateOfCreation, description);
        notesList.add(index,curNote);

    }

    //заменить ЗАМЕТКУ
    public void setNote(int index, String title, String dateOfCreation, String description){
        Note curNote = new Note(title, dateOfCreation, description);
        notesList.set(index, curNote);
    }

    //получить ЗАМЕТКУ
    public Note getNote(int index){

        return notesList.get(index);
    }

    //удалить текущий элемент массива
    public void removeCurNote(int index){
        if (notesList.size() > 0)
            notesList.remove(index);
    }
    //узнать последний индекс в массиве ArrayList
    public int getLastIndex(){
        return notesList.size()-1;
    }

    //получить заголовок ЗАМЕТКИ
    public String titleNote(int index){
        return notesList.get(index).title;
    }

    //получить дату создания ЗАМЕТКИ
    public String dateNote(int index){
        return notesList.get(index).dateOfCreate;
    }

    //получить описание ЗАМЕТКИ
    public String descNote(int index){
        return notesList.get(index).description;
    }

    //узнать есть ли элементы в массиве
    public boolean isNotesListExists(){
        if (!(notesList.isEmpty())) return true;
        return false;
    }

    //удалить все ЗАМЕТКИ
    public void clearNotes(){
        notesList.clear();
    }


}
