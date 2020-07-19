/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BookStore;

import java.util.Arrays;

public class Department { //Отдел

    private String DepartmentName; //Название отдела
    private Book[] Books; //Массив для хранения элементов
    private int ElementsCount; //Количество элементов
    private Department Next; //Следующий отдел в списке

    public Department(String Name, int ArraySize) { //Конструктор класса 
        DepartmentName = Name;
        ElementsCount = 0;
        Books = new Book[ArraySize]; //Инициализация массива
        Next = null;
    }

    public String GetDepartmentName() { //Получить имя
        return DepartmentName;
    }

    public void SetDepartmentName(String Name) { //Установить имя
        DepartmentName = Name;
    }

    public void SetNext(Department Next) { //Установить следующий в списке
        this.Next = Next;
    }

    public Department GetNext() { //Получить следующий в списке
        return Next;
    }

    public void AddBook(String Name, String Author, int Quantity) { //Добавить книгу
        Push(new Book(Name, Author, Quantity));
    }

    public boolean BookIsExists(String Name) {
        for (int i = 0; i < ElementsCount; ++i) {
            if (Books[i].GetBookName().equals(Name)) {
                return true;
            }
        }
        return false; //Нет искомого элемента в очереди
    }

    public int AmountBooksInDepartment() { //Подсчет суммарного числа книг в отделе
        int result = 0;
        for (int i = 0; i < ElementsCount; ++i) {
            result = result + Books[i].GetBookQuantity(); //Суммируем количество
        }
        return result; //Возвращаем результат
    }

    public void Push(Book NewElement) { //Положить в очередь
        if (ElementsCount == Books.length - 1) { //Если массив заполнен, расширяем его
            Book[] newArray = Arrays.copyOf(Books, Books.length * 2); //Создаём новый массив в два раза больше размером, и копируем старый массив в него
            Books = newArray;
        }

        Books[ElementsCount] = NewElement; //Добавляем новый элемент
        ElementsCount++; //Увеличиваем счётчик элементов
    }

    public Book Pop() { //Забрать из очереди
        if (ElementsCount == 0) {
            return null; //Если очередь пуста, возвращаем пустой элемент
        }
        Book top = Books[0]; //Сохраняем верхний элемент

        for (int i = 0; i < ElementsCount; ++i) { //Выполняем сдвиг элементов в массиве
            Books[i] = Books[i + 1];
        }

        ElementsCount--; //Уменьшаем счётчик элементов

        return top; //Возвращаем верхний элемент
    }

    public int Size() { //Размер очереди
        return ElementsCount;
    }

}
