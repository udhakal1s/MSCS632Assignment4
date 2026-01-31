package com.company.scheduler;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        // User Input
        System.out.print("How many employees are working this week? ");
        int totalEmp = Integer.parseInt(input.nextLine().trim());

        System.out.print("What are all the employee for this week: ");
        List<String> nameList = splitNames(input.nextLine());

        while (nameList.size() < totalEmp) {
            System.out.print("Enter another employee name: ");
            nameList.add(input.nextLine().trim());
        }

        if (nameList.size() > totalEmp) {
            nameList = nameList.subList(0, totalEmp);
        }

        List<Employee> staff = new ArrayList<>();
        for (String n : nameList) staff.add(new Employee(n));

        // Preferences 3 shifts in one line
        for (Employee e : staff) {
            for (String d : Scheduler.days) {
                System.out.print("Shift choices for " + e.name + " on " + d + ": ");
                e.prefMap.put(d, readShifts(input));
            }
        }

        Scheduler sched = new Scheduler();
        sched.createSchedule(staff);
        sched.printCompanySchedule();
    }

    private static List<String> splitNames(String line) {
        List<String> out = new ArrayList<>();
        for (String p : line.split(",")) {
            String v = p.trim();
            if (!v.isEmpty()) out.add(v);
        }
        return out;
    }

    private static List<String> readShifts(Scanner input) {
        while (true) {
            String line = input.nextLine().trim().toUpperCase();
            String[] parts = line.split("\\s+");

            if (parts.length != 3) {
                System.out.println("Please enter 3 shift in a day");
                continue;
            }
// If user enter differnt name or just two shift
            if (!valid(parts[0]) || !valid(parts[1]) || !valid(parts[2])) {
                System.out.println("Please enter the Valid shifts like MORNING, AFTERNOON, EVENING");
                continue;
            }

            return List.of(parts[0], parts[1], parts[2]);
        }
    }

    private static boolean valid(String s) {
        return s.equals("MORNING") || s.equals("AFTERNOON") || s.equals("EVENING");
    }
}
