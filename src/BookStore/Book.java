/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BookStore;

public class Book { //Книга

    private String BookName, BookAuthor; //Название и автор
    private int BookQuantity; //Количество экземлпяров

    public Book(String Name, String Author, int Quantity) { //Конструктор класса 
        BookName = Name;
        BookAuthor = Author;
        BookQuantity = Quantity;
    }

    public String GetBookName() { //Получить имя
        return BookName;
    }

    public void SetBookName(String Name) { //Задать имя
        BookName = Name;
    }

    public String GetBookAuthor() { //Получить автора
        return BookAuthor;
    }

    public void SetBookAuthor(String Author) { //Задать автора
        BookAuthor = Author;
    }

    public int GetBookQuantity() { //Получить количество
        return BookQuantity;
    }

    public void SetBookQuantity(int Quantity) { //Задать количество
        BookQuantity = Quantity;
    }
}
