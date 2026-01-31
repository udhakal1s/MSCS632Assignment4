# Umesh Dhakal
#1/28/2026
#MSCS632A4
import random

dayList = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
shiftList = ["MORNING", "AFTERNOON", "EVENING"]

MAX_PER_SHIFT = 2


class Employee:
    def __init__(self, empName):
        self.empName = empName
        self.dayPref = {}        # day -> [p1, p2, p3]
        self.workedDay = set()   # max 5 days


def createEmptySchedule():
    schedule = {}
    for day in dayList:
        schedule[day] = {}
        for shift in shiftList:
            schedule[day][shift] = []
    return schedule


def parseEmployeeNames(line):
    names = []
    parts = line.split(",")
    for p in parts:
        name = p.strip()
        if name:
            names.append(name)
    return names


def readThreeShift(msg):
    #user types 3 shifts in a day
    while True:
        line = input(msg).strip().upper().replace(",", " ")
        parts = line.split()

        if len(parts) != 3: 
            print("Enter 3 shifts like: MORNING AFTERNOON EVENING")
            continue

        if parts[0] in shiftList and parts[1] in shiftList and parts[2] in shiftList:
            return parts

        print("Only use MORNING, AFTERNOON, EVENING")


def canAssign(schedule, emp, day, shift):
    if len(emp.workedDay) >= 5:
        return False
    if day in emp.workedDay:
        return False
    if len(schedule[day][shift]) >= MAX_PER_SHIFT:
        return False
    return True


def assign(schedule, emp, day, shift):
    schedule[day][shift].append(emp.empName)
    emp.workedDay.add(day)


def buildSchedule(empList):
    schedule = createEmptySchedule()

    #preference first, then fix conflict
    for i in range(len(dayList)):
        day = dayList[i]

        for emp in empList:
            if day in emp.workedDay:
                continue
            if len(emp.workedDay) >= 5:
                continue

            done = False

            #try their 3 preferences
            for shift in emp.dayPref[day]:
                if canAssign(schedule, emp, day, shift):
                    assign(schedule, emp, day, shift)
                    done = True
                    break

            #try any shift same day
            if not done:
                for shift in shiftList:
                    if canAssign(schedule, emp, day, shift):
                        assign(schedule, emp, day, shift)
                        done = True
                        break

            if not done and i + 1 < len(dayList):
                nextDay = dayList[i + 1]
                for shift in shiftList:
                    if canAssign(schedule, emp, nextDay, shift):
                        assign(schedule, emp, nextDay, shift)
                        break

    #making minimum 2 employees per shift per day
    for day in dayList:
        for shift in shiftList:
            while len(schedule[day][shift]) < 2:
                available = [e for e in empList if canAssign(schedule, e, day, shift)]
                if not available:
                    break
                pick = random.choice(available)
                assign(schedule, pick, day, shift)

    return schedule


def printWeeklySchedule(schedule, empList):
    for emp in empList:
        print(f"\nWeekly Schedule for {emp.empName.upper()} is:")
        for day in dayList:
            empShift = "OFF"
            for shift in shiftList:
                if emp.empName in schedule[day][shift]:
                    empShift = shift
                    break
            print(f"{day} - {empShift}")


def printCompanySchedule(schedule):
    print("\nThe company schedule is:\n")

    for day in dayList:
        print(day)

        for shift in shiftList:
            names = schedule[day][shift]
            if names:
                print(f"{shift} : {', '.join(names)}")
            else:
                print(f"{shift} -")

        print()


def main():
    empCount = int(input("How many employee are there in a company - ").strip())
    
#Firstname1 Lastname1, Firstname2 Lastname2
    nameLine = input("Enter all the employee : ").strip()
    nameList = parseEmployeeNames(nameLine)

    while len(nameList) < empCount:
        extra = input("Add employee name : ").strip()
        if extra:
            nameList.append(extra)

    nameList = nameList[:empCount]

    empList = []
    for name in nameList:
        empList.append(Employee(name))

    for emp in empList:
        for day in dayList:
            msg = f"What is the preference schedule for {emp.empName} {day}: "
            emp.dayPref[day] = readThreeShift(msg)

    finalSchedule = buildSchedule(empList)

    printWeeklySchedule(finalSchedule, empList)
    printCompanySchedule(finalSchedule)


if __name__ == "__main__":
    main()
