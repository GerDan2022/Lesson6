package ru.strannik.lesson6.data;

public class CardData {
    private String title; //заголовок
    private String data; //дата
    private int picture;  //картинка

    public CardData(String title, String data){
        this.title = title;
        this.data = data;
    }

    public String getTitle(){
        return title;
    }

    public String getData(){
        return data;
    }


}
