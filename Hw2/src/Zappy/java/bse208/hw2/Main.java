package Zappy.java.bse208.hw2;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        try {
            // Сериализуем данные о студентах.
            readFile();
        } catch (IOException | InvalidParameterException exception) {
            System.out.println(exception.getMessage());
            return;
        } catch (Exception exception) {
            System.out.println("Возникла непредвиденная ошибка!");
            return;
        }
        // Выводим приветствие и запускаем цикл консоли.
        help();
        console();
    }

    // Сериализация данных о студентах из файла.
    private static void readFile() throws IOException, InvalidParameterException {
        final String path = "StudentsList.txt";
        File file = new File(path);
        // Проверка, что файл существует.
        if (!file.createNewFile()) {
            Scanner fileScanner = new Scanner(file);
            int numberOfLine = 1;
            // Считываем очередную строку.
            while (fileScanner.hasNextLine()) {
                Student.parseStudent(fileScanner.nextLine(), numberOfLine++);
            }
            if (numberOfLine == 1) {
                throw new IOException("Файл с записями пуст, заполните его и возвращайтесь!");
            }
        } else {
            throw new FileNotFoundException("Файл с записью студентов не существует, заполните созданный новый файл!");
        }
    }

    // Работа консоли.
    private static void console() {
        Scanner consoleScanner = new Scanner(System.in);
        System.out.print(System.lineSeparator() + "Введите вашу команду: ");
        String nextLine = consoleScanner.nextLine();
        while (!nextLine.equals("/q")) {
            // Доступные варианты.
            switch (nextLine.toLowerCase(Locale.ROOT)) {
                case "/list" -> printList(false);
                case "/random" -> getRandomStudent();
                case "/help" -> help();
                case "/god-mode" -> godMode();
                case "/add" -> addStudent();
                default -> System.out.println("Некорректная команда!");
            }
            // Запрос следующей команды.
            System.out.print(System.lineSeparator() + "Введите вашу команду: ");
            nextLine = consoleScanner.nextLine();
        }
        System.out.println();
        // Вывод выставленных оценок после завершения работы.
        printList(true);
    }

    // Вывод списка студентов (опционально с оценками).
    private static void printList(boolean withMarks) {
        if (Student.students.size() == 0) {
            System.out.println("Список студентов пуст!");
            return;
        }
        // Вывод списка с оценками.
        if (withMarks) {
            Student.students.forEach(student -> {
                String mark = (student.marks.size() == 0 ? "Нет оценок!" : "" + student.getMarks());
                // Форматирование вывода.
                System.out.format("%-50s%-16s", student + ":", mark);
                System.out.println();
            });
        // вывод списка без оценок.
        } else {
            Student.students.forEach(System.out::println);
        }
        System.out.println();
    }

    // Выбор рандомного студента.
    private static void getRandomStudent() {
        boolean isPresent = false;
        Scanner consoleScanner = new Scanner(System.in);
        // Будем выполнять, пока не найдем присутствующего студента.
        while (!isPresent) {
            // Выбор рандомного числа.
            int randomInt = ThreadLocalRandom.current()
                    .nextInt(1, Student.students.size() + 1);
            Student student = Student.students.get(randomInt - 1);
            System.out.println(student);
            // Выбор действия.
            String answer = getCorrectAnswer();
            if (answer.equals("отмена")) {
                return;
            } else if (answer.equals("есть")) {
                student.isPresent = isPresent = true;
                System.out.print("Полученная оценка: ");
                // Тут можно было реализовать проверку корректности оценки, но мне уже что-то лень.
                student.setMark(consoleScanner.nextInt());
            }
        }
    }

    // Запрос выбора действия для рандомно выбранного студента.
    private static String getCorrectAnswer() {
        Scanner consoleScanner = new Scanner(System.in);
        System.out.print("Есть/нет/отмена?" + System.lineSeparator() + "Введите ваш ответ: ");
        String input = consoleScanner.nextLine().toLowerCase(Locale.ROOT);
        // Проверка корректности выбранного действия.
        while (!input.equals("есть") && !input.equals("нет") && !input.equals("отмена")) {
            System.out.println("Неправильный ввод, повторите!");
            System.out.print("Есть/нет/отмена?" + System.lineSeparator() + "Введите ваш ответ: ");
            input = consoleScanner.nextLine().toLowerCase(Locale.ROOT);
        }
        return input;
    }

    // Вывод меню помощи.
    private static void help() {
        System.out.println("Это меню помощи!");
        System.out.println("/q - выход и печать всех студентов и оценок с пары.");
        System.out.println("/list - выводит список всех.");
        System.out.println("/random - рандомный выбор студента.");
        System.out.println("/help - показать эту справку.");
        System.out.println("/god-mode - ???");
        System.out.println("/add - добавить студента в файл и программу.");
    }

    // Секретный режим, при активации которого автору программы ставятся только 10)))
    private static void godMode() {
        // Переключение режима.
        boolean isGod = Student.isGodModeActivated;
        Student.isGodModeActivated = !isGod;
        if (!isGod) {
            System.out.println("Режим бога активирован!" + System.lineSeparator()
                    + "Конечно же ничего не произошло)))))");
        } else {
            System.out.println("Режим бога дезактивирован!");
        }
    }

    // Не смог раздобыть актуальный список группы, так что пусть будет.
    // Добавление нового студента в программу и файл.
    private static void addStudent() {
        Scanner consoleScanner = new Scanner(System.in);
        System.out.print("Введите фио студента: ");
        String name = consoleScanner.nextLine();
        System.out.println();
        // Добавление студента в программу со следующим идентификационным номером.
        Student newStudent = new Student(Student.students.size() + 1, name);
        Student.students.add(newStudent);
        final String path = "StudentsList.txt";
        // Добавление студента в файл.
        try (FileWriter writer = new FileWriter(path, true)) {
            writer.write(System.lineSeparator() + newStudent.getId() + ": " + newStudent.getFullName());
        } catch (IOException ex) {
            System.out.println("Возникла ошибка при попытке добавления нового студента в файл!");
            return;
        }
        System.out.println("Успешная запись!");
    }
}
