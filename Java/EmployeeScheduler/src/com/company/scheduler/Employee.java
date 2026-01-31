package com.company.scheduler;

import java.util.*;

public class Employee {
    // employee full name
    String name;
    Map<String, List<String>> prefMap = new HashMap<>();
    // max 5 days
    Set<String> daysWorked = new HashSet<>();

    public Employee(String name) {
        this.name = name;
    }
    public boolean canStillWork() {
        return daysWorked.size() < 5;
    }
    public boolean alreadyWorked(String day) {
        return daysWorked.contains(day);
    }
}
