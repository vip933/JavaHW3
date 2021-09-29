package Zappy.java.bse208.hw2;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Student {
    // Список всех студентов.
    public static List<Student> students = new ArrayList<>();
    public static boolean isGodModeActivated = false;
    private final int id;
    private final String fullName;
    public boolean isPresent = false;
    public List<Integer> marks = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public Student(int id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    // Часть режима бога, где автору ставятся только 10)))
    public void setMark(int studentMark) {
        if (isGodModeActivated && fullName.equals("Лейпунский Максим Дмитриевич")) {
            marks.add(10);
        } else {
            marks.add(studentMark);
        }
    }

    // Вывод информации о полученных оценках.
    public String getMarks() {
        StringBuilder output = new StringBuilder();
        for (Integer mark : marks) {
            output.append(mark).append(" ");
        }
        return output.toString();
    }

    // Считывание студента из строки в файле.
    public static void parseStudent(String line, int numberOfLine) throws InvalidParameterException {
        // Отделяем id от имени.
        String[] splittedNumberAndFullName = line.split(": ");
        if (splittedNumberAndFullName.length != 2) {
            throw new InvalidParameterException("Строка с номером: " + numberOfLine + " записана некорректно!");
        }
        int number = Integer.parseInt(splittedNumberAndFullName[0]);
        if (number < 0) {
            throw new InvalidParameterException("Ошибка в строке " + ((long) students.size() + 1) + ": номер " +
                    "не может быть отрицательным!");
        }
        if (isPresent(number)) {
            throw new InvalidParameterException("Ошибка в строке " + ((long) students.size() + 1) +
                    ": номер " + number + " уже существует в базе данных!");
        }
        String name = splittedNumberAndFullName[1];
        // Добавление полученного студента в программу.
        students.add(new Student(number, name));
    }

    // Проверка, что id не занято.
    private static Boolean isPresent(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return true;
            }
        }
        return false;
    }

    // Удобный вывод в консоль.
    public String toString() {
        return "Id-" + id + ": " + fullName;
    }
}
