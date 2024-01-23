package org.db.students;

import java.util.Scanner;

public class Main {

    private static StudentCommandHandler STUDENT_COMMAND_HANDLER
            = new StudentCommandHandler();

    public static void main (String[] args) {
        while (true) {
            printMessage();
            Command command = readCommand();
            if (command.getAction() == Action.EXIT) {
                return;
            } else if (command.getAction() == Action.ERROR) {
                continue;
            } else {
                STUDENT_COMMAND_HANDLER.processCommand(command);
            }
        }
    }

    private static Command readCommand () {
        Scanner scanner = new Scanner(System.in);
        try {
            String code = scanner.nextLine();
            codeChecking(code);
            Integer actionCode = Integer.valueOf(code);
            Action action = Action.fromCode(actionCode);

            if (action.isRequireAdditionalData()) {
                String data = scanner.nextLine();
                boolean dataIsCorrect = dataChecking(action, data);
                if (dataIsCorrect) {
                    return new Command(action, data);
                } else {
                    return new Command(Action.ERROR);
                }

            } else {
                return new Command(action);
            }
        } catch (Exception ex) {
            System.out.println("Проблема обработки ввода. " + ex.getMessage());
            return new Command(Action.ERROR);
        }
    }

    private static void codeChecking (String code) {
        if (code.isEmpty()) {
            throw new  RuntimeException("Не введена информация о коде команды.");
        }
        try {
           Integer.valueOf(code);
        } catch (NumberFormatException e) {
            throw new  RuntimeException ("Значение \"" + code + "\" не удалось привести к числу.");
        }

    }

    private static boolean dataChecking (Action action, String data) {
        if (action == Action.CREATE) {
            String[] dataArray = data.split(",");
            if (dataArray.length != 5) {
                throw new RuntimeException("Неверное количество данных для создания студента. " +
                        "Должно быть пять значений разделенных запятой.");
            }
            try {
                Integer age = Integer.valueOf(dataArray[4]);
                if (age <= 0) {
                    throw new RuntimeException("Возраст студента должен быть больше 0.");
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("Не удалось преобразовать значение \"" + dataArray[4] + "\" к числу.");
            }
        } else if (action == Action.UPDATE) {
            String[] dataArray = data.split(",");
            if (dataArray.length != 6) {
                throw new RuntimeException("Неверное количество данных для редактирования студента. " +
                        "Должно быть шесть значений разделенных запятой.");
            }
            try {
                Integer id = Integer.valueOf(dataArray[0]);
                if (id <= 0) {
                    throw new RuntimeException("Код студента это целое число больше 0.");
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("Не удалось преобразовать значение \"" + dataArray[0] + "\" к числу.");
            }
            try {
                Integer age = Integer.valueOf(dataArray[5]);
                if (age <= 0) {
                    throw new RuntimeException("Возраст студента должен быть больше 0.");
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("Не удалось преобразовать значение " + dataArray[5] + " к числу.");
            }
        } else if(action == Action.DELETE) {
            if (data.isEmpty()) {
                throw new  RuntimeException ("Не введен код студента.");
            }
            try {
                Long.valueOf(data);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Не удалось преобразовать значение " + data + " к числу.");
            }
        }
        else if(action == Action.SEARCH) {
           String[] dataArray = data.split(",");
            if (dataArray.length > 2) {
                throw new RuntimeException("Нужно ввести не более двух фамилий разделенных запятыми.");
            }
        }
        return true;
    }


    private static void printMessage() {
        System.out.println("---------------------------------");
        System.out.println("0. Выход");
        System.out.println("1. Создание данных");
        System.out.println("2. Обновление данных");
        System.out.println("3. Удаление данных");
        System.out.println("4. Вывод статистики по курсам");
        System.out.println("5. Вывод статистики по городам");
        System.out.println("6. Поиск по фамилии");
        System.out.println("---------------------------------");
    }
}
