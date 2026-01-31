package com.company.scheduler;

import java.util.*;
public class Scheduler {
// List of days
    static final List<String> days =
            List.of("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday");
// List of Duty in a Day
    static final List<String> shifts =
            List.of("MORNING","AFTERNOON","EVENING");
    static final int SHIFT_LIMIT = 2;
    Map<String, Map<String, List<Employee>>> weekPlan = new LinkedHashMap<>();
    Random random = new Random();
    public Scheduler() {
        for (String d : days) {
            Map<String, List<Employee>> map = new LinkedHashMap<>();
            for (String s : shifts) map.put(s, new ArrayList<>());
            weekPlan.put(d, map);
        }
    }
    public void createSchedule(List<Employee> staff) {

        // preference for 1st round
        for (int i = 0; i < days.size(); i++) {
            String day = days.get(i);

            for (Employee e : staff) {
                if (!e.canStillWork() || e.alreadyWorked(day)) continue;

                boolean placed = placeByPref(e, day);

                if (!placed) placed = placeAnywhere(e, day);

                if (!placed && i + 1 < days.size()) {
                    String nextDay = days.get(i + 1);
                    if (e.canStillWork() && !e.alreadyWorked(nextDay)) {
                        placeAnywhere(e, nextDay);
                    }
                }
            }
        }

        //  2  shift per day
        for (String d : days) {
            for (String s : shifts) {
                while (weekPlan.get(d).get(s).size() < 2) {
                    Employee pick = randomAvailable(staff, d, s);
                    if (pick == null) break;
                    assign(pick, d, s);
                }
            }
        }
    }
    private boolean placeByPref(Employee e, String day) {
        for (String s : e.prefMap.get(day)) {
            if (canAssign(e, day, s)) {
                assign(e, day, s);
                return true;
            }
        }
        return false;
    }
    private boolean placeAnywhere(Employee e, String day) {
        for (String s : shifts) {
            if (canAssign(e, day, s)) {
                assign(e, day, s);
                return true;
            }
        }
        return false;
    }
    private boolean canAssign(Employee e, String day, String shift) {
        if (!e.canStillWork()) return false;
        if (e.alreadyWorked(day)) return false;
        if (weekPlan.get(day).get(shift).size() >= SHIFT_LIMIT) return false;
        return true;
    }
    private void assign(Employee e, String day, String shift) {
        weekPlan.get(day).get(shift).add(e);
        e.daysWorked.add(day);
    }
    private Employee randomAvailable(List<Employee> staff, String day, String shift) {
        List<Employee> ok = new ArrayList<>();
        for (Employee e : staff) {
            if (canAssign(e, day, shift)) ok.add(e);
        }
        if (ok.isEmpty()) return null;
        return ok.get(random.nextInt(ok.size()));
    }
    public void printCompanySchedule() {
        System.out.println("\nFollowing is the weekly schedule for this week:\n");

        for (String d : days) {
            for (String s : shifts) {
                System.out.print(d + " " + pretty(s) + " - ");

                List<Employee> list = weekPlan.get(d).get(s);
                if (list.isEmpty()) {
                    System.out.println("-");
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        System.out.print(list.get(i).name);
                        if (i < list.size() - 1) System.out.print(", ");
                    }
                    System.out.println();
                }
            }
            System.out.println();
        }
    }

    private String pretty(String s) {
        if (s.equals("MORNING")) return "Morning";
        if (s.equals("AFTERNOON")) return "Afternoon";
        return "Evening";
    }
}
