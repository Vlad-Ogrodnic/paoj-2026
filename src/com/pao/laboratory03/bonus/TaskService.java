package com.pao.laboratory03.bonus;

import java.util.*;
import java.util.stream.Collectors;

public class TaskService {
    private static TaskService instance;

    private final Map<String, Task> tasksById;
    private final Map<Priority, List<Task>> tasksByPriority;
    private final List<String> auditLog;
    private int nextId = 1;

    private TaskService() {
        tasksById = new LinkedHashMap<>(); // Păstrează ordinea inserării pentru afișări mai curate
        tasksByPriority = new EnumMap<>(Priority.class);
        for (Priority p : Priority.values()) {
            tasksByPriority.put(p, new ArrayList<>());
        }
        auditLog = new ArrayList<>();
    }

    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }

    public Task addTask(String title, Priority priority) {
        String id = String.format("T%03d", nextId++);
        return addTaskWithCustomId(id, title, priority);
    }

    // Metodă adăugată pentru a putea testa DuplicateTaskException (Cerința 9)
    public Task addTaskWithCustomId(String id, String title, Priority priority) {
        if (tasksById.containsKey(id)) {
            throw new DuplicateTaskException("Task-ul cu ID-ul '" + id + "' există deja!");
        }

        Task task = new Task(id, title, Status.TODO, priority, null);
        tasksById.put(id, task);
        tasksByPriority.get(priority).add(task);

        auditLog.add("[ADD] " + task.getId() + ": '" + task.getTitle() + "' (" + task.getPriority() + ")");
        return task;
    }

    public void assignTask(String taskId, String assignee) {
        Task task = getTaskOrThrow(taskId);
        task.setAssignee(assignee);
        auditLog.add("[ASSIGN] " + taskId + " → " + assignee);
    }

    public void changeStatus(String taskId, Status newStatus) {
        Task task = getTaskOrThrow(taskId);
        Status oldStatus = task.getStatus();

        if (!oldStatus.canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(oldStatus, newStatus);
        }

        task.setStatus(newStatus);
        auditLog.add("[STATUS] " + taskId + ": " + oldStatus + " → " + newStatus);
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return new ArrayList<>(tasksByPriority.get(priority));
    }

    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new LinkedHashMap<>();
        for (Status s : Status.values()) {
            long count = tasksById.values().stream()
                    .filter(t -> t.getStatus() == s)
                    .count();
            summary.put(s, count);
        }
        return summary;
    }

    public List<Task> getUnassignedTasks() {
        return tasksById.values().stream()
                .filter(t -> t.getAssignee() == null)
                .collect(Collectors.toList());
    }

    public void printAuditLog() {
        for (String log : auditLog) {
            System.out.println(log);
        }
    }

    public double getTotalUrgencyScore(int baseDays) {
        return tasksById.values().stream()
                .filter(t -> t.getStatus() != Status.DONE && t.getStatus() != Status.CANCELLED)
                .mapToDouble(t -> t.getPriority().calculateScore(baseDays))
                .sum();
    }

    private Task getTaskOrThrow(String taskId) {
        Task task = tasksById.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost găsit");
        }
        return task;
    }
}