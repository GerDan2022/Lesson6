package ru.strannik.lesson6.data;

public interface CardsSource {
    CardData getCardData (int position);
    int size();
}
