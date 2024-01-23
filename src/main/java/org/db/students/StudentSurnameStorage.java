package org.db.students;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class StudentSurnameStorage {
    private TreeMap<String, Set<Long>> surnamesTreeMap = new TreeMap<>();


    public void studentCreated (Long id, String surname) {
        Set<Long> existingIds = surnamesTreeMap.getOrDefault(surname, new HashSet<>());
        existingIds.add(id);
        surnamesTreeMap.put(surname, existingIds);
    }

    public void studentDeleted (Long id, String surname) {
        surnamesTreeMap.get(surname).remove(id);
    }

    public void studentUpdated (Long id, String oldSurname, String newSurname) {
        studentDeleted(id, oldSurname);
        studentCreated(id, newSurname);
    }


    /**
     * Данный метод возвращает всех студентов с переданной фамилией.
     *
     * @param surname - передаваемая в метод фамилия.
     * @return set
     */
    public Set<Long> getStudentBySurnames (String surname) {
        Set<Long> res = surnamesTreeMap.entrySet()
                .stream().filter(x -> surname.equals(x.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                .values().stream()
                .flatMap(longs -> longs.stream())
                .collect(Collectors.toSet());
        return res;
    }

    /**
     * Метод возвлащает всех студентов, чьи фамилии в алфавитном
     * порядке >= первой фамилии и <= второй фамилии.
     *
     * @param firstSurname - передаваемая в метод первая фамилия
     * @param secondSurname - передаваемая в метод вторая фамилия
     * @return
     */
public Set<Long> getStudentBetweenTwoSurnames (String firstSurname, String secondSurname) {
       Set<Long> res = surnamesTreeMap.subMap(firstSurname, true, secondSurname, true)
                .values()
                .stream()
                .flatMap(longs -> longs.stream())
                .collect(Collectors.toSet());
       return  res;
}

}
