package com.pao.laboratory03.Service;

import com.pao.laboratory03.model.Student;
import com.pao.laboratory03.model.Subject;
import com.pao.laboratory03.exceptions.StudentNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;

public class StudentService {
    private static StudentService instance;
    private final List<Student> students;

    private StudentService() {
        students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) instance = new StudentService();
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Studentul " + name + " există deja.");
            }
        }
        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) return s;
        }
        throw new StudentNotFoundException("Studentul " + name + " nu a fost găsit.");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        Student s = findByName(studentName);
        s.addGrade(subject, grade);
    }

    public void printAllStudents() {
        for (Student s : students) {
            System.out.println(s);
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
            }
        }
    }

    public void printTopStudents() {
        students.stream()
                .sorted(Comparator.comparingDouble(Student::getAverage).reversed())
                .forEach(System.out::println);
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, List<Double>> temp = new HashMap<>();
        for (Student s : students) {
            for (Map.Entry<Subject, Double> e : s.getGrades().entrySet()) {
                temp.computeIfAbsent(e.getKey(), k -> new ArrayList<>()).add(e.getValue());
            }
        }
        Map<Subject, Double> averages = new HashMap<>();
        for (Map.Entry<Subject, List<Double>> e : temp.entrySet()) {
            List<Double> vals = e.getValue();
            double sum = 0;
            for (double g : vals) sum += g;
            averages.put(e.getKey(), sum / vals.size());
        }
        return averages;
    }
}