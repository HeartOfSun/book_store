/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BookStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.Collator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookStore { //Книжный магазин

    private Department First; // Первый элемент для работы с однонаправленным списком
    private final int DefaultDepartmentSize = 2; //Начальный размер отдела

    public Department AddDepartment(String Name) { //Добавление отдела
        if (First == null) { //Если нет первого элемента
            First = new Department(Name, DefaultDepartmentSize); //Добаляем новый элемент на место первого
            First.SetNext(First);
            return First; //И возвращаем добавленный элемент
        }
        Collator russianCollator = Collator.getInstance(new Locale("ru", "RU"));
        Department Temp = First;
        if (russianCollator.compare(Name, First.GetDepartmentName()) < 0) {
            while (Temp.GetNext() != First) {
                Temp = Temp.GetNext();
            }
        } else {
            do {
                if (russianCollator.compare(Name, Temp.GetNext().GetDepartmentName()) < 0) {
                    break;
                }
                Temp = Temp.GetNext();
            } while (Temp.GetNext() != First);  //Проходим по списку в поисках элемента
        }
        Department TempNext = Temp.GetNext();
        Temp.SetNext(new Department(Name, DefaultDepartmentSize));
        Temp.GetNext().SetNext(TempNext);
        if (TempNext == First && russianCollator.compare(Name, First.GetDepartmentName()) < 0) {
            First = Temp.GetNext();
        }
        return Temp.GetNext(); //И возвращаем добавленный элемент
    }

    public Department GetFirst() {
        return First;
    }

    public boolean RemoveDepartment(String Name) { //Удаление отдела, возвращает true если отдел удалён, false если отдел не найден   
        Department Temp = First;
        do {
            if (Temp.GetNext().GetDepartmentName().equals(Name)) {
                if (Temp.GetNext() == First) {
                    if (Temp == First) {  //Если отдел всего один в списке
                        First = null; //Обнуляем его
                        return true;
                    } else {
                        First = First.GetNext();
                    }
                }
                Temp.SetNext(Temp.GetNext().GetNext()); //Удаляем отдел, установив предыдущему отделу указатель на следующий
                return true;
            }
            Temp = Temp.GetNext();
        } while (Temp != First);  //Проходим по списку в поисках элемента
        return false; //Дошли до конца списка, не встретив нужный отдел
    }

    public int AmountBooksInStore() { //Подсчет суммарного числа книг
        int result = 0; //Счетчик числа книгs
        if (First != null) {
            Department Temp = First; //Начинаем с первого элемента
            do {
                result = result + Temp.AmountBooksInDepartment(); //Подсчитываем количество книг
                Temp = Temp.GetNext(); //Переходим к следующему
            } while (Temp != First); //Пока не дойдём вновь до первого элемента
        }
        return result;
    }

    public void SaveStoreToFile(File FileToSave) {
        FileOutputStream fstream = null;
        try {
            //Сохранение магазина в файл
            String FilePath = FileToSave.getAbsolutePath();
            if (!FilePath.toLowerCase().endsWith(".bsr")) { //Добавляем файлу расширение, если его нет
                FilePath = FilePath + ".bsr";
            }
            fstream = new FileOutputStream(FilePath);
            BufferedWriter Writer = new BufferedWriter(new OutputStreamWriter(fstream, "UTF8"));
            Department Temp = First;
            do {
                Writer.write(Temp.GetDepartmentName() + "\n"); //Записываем название отдела
                if (Temp.AmountBooksInDepartment() > 0) {
                    for (int i = 0; i < Temp.Size(); ++i) { //Проходим по всем книгам в отделе
                        Book TempBook = Temp.Pop();
                        Temp.Push(TempBook);
                        Writer.write(TempBook.GetBookName() + "\n"); //Записываем название
                        Writer.write(TempBook.GetBookAuthor() + "\n"); //Записываем автора
                        Writer.write(String.valueOf(TempBook.GetBookQuantity()) + "\n"); //Записываем количество
                    }
                }
                Writer.write("\n"); //Записываем пустую строку для того, чтобы разделить отделы
                Temp = Temp.GetNext();
            } while (Temp != First);
            Writer.flush();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BookStore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BookStore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BookStore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fstream.close();
            } catch (IOException ex) {
                Logger.getLogger(BookStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void LoadStoreFromFile(File FileToLoad) {
        FileInputStream fstream = null;
        try {
            //Загрузка магазина из файла
            fstream = new FileInputStream(FileToLoad);
            BufferedReader Reader;
            Reader = new BufferedReader(new InputStreamReader(fstream, "UTF8"));
            First = null;
            Department CurrentDepartment = AddDepartment(Reader.readLine()); //Считываем название первого отдела, и создаём отдел
            boolean NewDepartment = false; //Переменная, сигнализирующая о том, что следующая строка - название нового отдела
            String line;
            while ((line = Reader.readLine()) != null) { //Пока файл не закончился
                if (line.isEmpty()) {
                    NewDepartment = true; //Если строка пустая, то запоминаем, что следующая строка - название нового отдела
                } else { //Если нет
                    if (!NewDepartment) { //Если текущая строка не завание нового отдела, то добавляем новую книгу, взяв текущую строку и две следующие
                        CurrentDepartment.AddBook(line, Reader.readLine(), Integer.parseInt(Reader.readLine()));
                    } else { //Иначе добавляем новый отдел
                        CurrentDepartment = AddDepartment(line);
                        NewDepartment = false;
                    }
                }

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BookStore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BookStore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BookStore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fstream.close();
            } catch (IOException ex) {
                Logger.getLogger(BookStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Department FindDepartment(String Name) { //Проверка, существует ли отдел с таким именем
        if (First == null) { //Если список отделов пуст, возвращаем null
            return null;
        }
        Department Temp = First;
        do {
            if (Temp.GetDepartmentName().equals(Name)) {
                return Temp; //Нашли элемент, возвращаем
            }
            Temp = Temp.GetNext();

        } while (Temp != First);  //Проходим по списку в поисках элемента

        return null;  //Дошли до конца списка, не найдя элемент с таким названием, возвращаем ложь 
    }
}
