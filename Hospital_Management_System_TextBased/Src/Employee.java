public class Employee {
    protected String empId, name, deptId, email, specialization, shift, contact;
    protected double salary;

    public Employee(String empId, String name, String deptId, double salary, String email, String specialization, String shift, String contact) {
        this.empId = empId;
        this.name = name;
        this.deptId = deptId;
        this.salary = salary;
        this.email = email;
        this.specialization = specialization;
        this.shift = shift;
        this.contact = contact;
    }

    public void displayInfo() {
        System.out.printf("%-10s %-25s %-15s %-10s %-10.2f %-20s %-15s %-15s %-15s%n",
                empId, name, "General Employee", deptId, salary, email, specialization, shift, contact);
    }
}

class Doctor extends Employee {
    public Doctor(String empId, String name, String deptId, double salary, String email, String specialization, String shift, String contact) {
        super(empId, name, deptId, salary, email, specialization, shift, contact);
    }

    public void displayInfo() {
        System.out.printf("%-10s %-25s %-15s %-10s %-10.2f %-20s %-15s %-15s %-15s%n",
                empId, name, "Doctor", deptId, salary, email, specialization, shift, contact);
    }
}
class Nurse extends Employee {
    public Nurse(String empId, String name, String deptId, double salary, String email, String specialization, String shift, String contact) {
        super(empId, name, deptId, salary, email, specialization, shift, contact);
    }

    public void displayInfo() {
        System.out.printf("%-10s %-25s %-15s %-10s %-10.2f %-20s %-15s %-15s%n",
                empId, name, "Nurse", deptId, salary, email, specialization, shift, contact);
    }
}

class AdminStaff extends Employee {
    public AdminStaff(String empId, String name, String deptId, double salary, String email, String specialization, String shift, String contact) {
        super(empId, name, deptId, salary, email, specialization, shift, contact);
    }

    public void displayInfo() {
        System.out.printf("%-10s %-25s %-15s %-10s %-10.2f %-20s %-15s %-15s%n",
                empId, name, "Administrator", deptId, salary, email, specialization, shift, contact);
    }
}