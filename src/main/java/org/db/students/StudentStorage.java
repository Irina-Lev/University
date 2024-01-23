package org.db.students;

import java.util.*;
import java.util.stream.Collectors;


public class StudentStorage {
    private Map<Long, Student> studentStorageMap = new HashMap<>();
    private StudentSurnameStorage studentSurnameStorage = new StudentSurnameStorage();
    private Long currentId = 0L;

    /**
     * Создание данных о студенте
     *
     * @param student данные о студенте
     * @return сгенерированный уникальный идентификатор студента
     */
    public Long createStudent (Student student) {
        Long nextId = getNextId();
        studentStorageMap.put(nextId, student);
        studentSurnameStorage.studentCreated(nextId, student.getSurname());
        return nextId;
    }

    /**
     * Обновление данных о студенте
     *
     * @param id      идентификатор студента
     * @param student данные студента
     * @return true если данные были обновлены, false если студент не
     * был найден
     */
    public boolean updateStudent (Long id, Student student) {
        if (!studentStorageMap.containsKey(id)) {
            System.out.println("Студент с кодом \"" + id + "\" отсутствует в базе.");
            return false;
        } else {
            String newSurname = student.getSurname();
            String oldSurname = studentStorageMap.get(id).getSurname();
            studentSurnameStorage.studentUpdated(id, oldSurname, newSurname);
            studentStorageMap.put(id, student);
            return true;
        }
    }

    /**
     * Удаляет данные о студенте
     *
     * @param id идентификатор студента
     * @return true если данные был удален, false если студент не
     * был найден по идентификатору
     */
    public boolean deleteStudent (Long id) {
        Student removed = studentStorageMap.remove(id);
        if (removed != null) {
            studentSurnameStorage.studentDeleted(id, removed.getSurname());
        } else {
            System.out.println("Студент с кодом \"" + id + "\" отсутствует в базе.");
        }
        return removed != null;
    }

    public void search (String surname) {
        try {
            if (surname.isEmpty()) {
                for (Student student : studentStorageMap.values()) {
                    System.out.println(student);
                }
            } else {
                String[] dataArray = surname.split(",");
                if (dataArray.length == 1) {
                    validateSurname(surname);
                    Set<Long> students = studentSurnameStorage
                            .getStudentBySurnames(surname);
                    for (Long studentId : students) {
                        Student student = studentStorageMap.get(studentId);
                        System.out.println(student);
                    }
                } else {
                    validateSurname(dataArray[0]);
                    validateSurname(dataArray[1]);
                    Arrays.sort(dataArray);
                    Set<Long> students = studentSurnameStorage
                            .getStudentBetweenTwoSurnames(dataArray[0], dataArray[1]);
                    for (Long studentId : students) {
                        Student student = studentStorageMap.get(studentId);
                        System.out.println(student);
                    }
                }

            }
        } catch (Exception ex) {
            System.out.println("Проблема обработки ввода. " + ex.getMessage());
        }

    }

    private void validateSurname (String surname) {
        List<Student> listStudentsFamily = studentStorageMap.values().stream().filter(s -> s.getSurname().equals(surname)).toList();
        if (listStudentsFamily.isEmpty()) {
            throw new RuntimeException("Студент с фамилией \"" + surname + "\" отсутствует в базе.");
        }
    }

    public long getNextId () {
        currentId = currentId + 1;
        return currentId;
    }

    public void printAll () {
        System.out.println(studentStorageMap);
    }

    public void printMap (Map<String, Long> data) {
        data.entrySet().stream().forEach(e -> {
            System.out.println(e.getKey() + " - " + e.getValue());
        });
    }

    public Map<String, Long> getCountByCourse () {
        Map<String, Long> res = studentStorageMap.values().stream()
                .collect(Collectors.toMap(
                        student -> student.getCourse(),
                        student -> 1L,
                        (count1, count2) -> count1 + count2
                ));
        return res;
    }

    public Map<String, Long> getCountByCity () {
        Map<String, Long> res = studentStorageMap.values().stream()
                .collect(Collectors.toMap(
                        student -> student.getCity(),
                        student -> 1L,
                        (count1, count2) -> count1 + count2
                ));
        return res;
    }
}
