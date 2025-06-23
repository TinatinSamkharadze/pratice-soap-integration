package ge.tbc.testautomation;

import com.example.springboot.soap.interfaces.EmployeeInfo;
import lombok.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {



        public Date birthDate;
        public BigDecimal salary;
        public long employeeId;
        public String address;
        public String department;
        public String email;
        public String name;
        public String phone;

        // Manual getters with camelCase names for MyBatis
        public Date getBirthDate() { return birthDate; }
        public long getEmployeeId() { return employeeId; }


    // Static factory method for fluent creation from EmployeeInfo
    public static Employee fromEmployeeInfo(Object employeeInfo) {
        // Assuming employeeInfo has similar getter methods
        try {
            // Cast to your actual EmployeeInfo type
            // Replace 'EmployeeInfo' with your actual SOAP generated class name
            var info = (EmployeeInfo) employeeInfo; // Replace with actual type

            return Employee.builder()
                    .employeeId(info.getEmployeeId())
                    .name(info.getName())
                    .address(info.getAddress())
                    .email(info.getEmail())
                    .department(info.getDepartment())
                    .birthDate(convertXMLGregorianCalendarToSqlDate(info.getBirthDate()))
                    .phone(info.getPhone())
                    .salary(info.getSalary())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert EmployeeInfo to Employee", e);
        }
    }

    // Helper method to convert XMLGregorianCalendar to SQL Date
    private static Date convertXMLGregorianCalendarToSqlDate(XMLGregorianCalendar xmlDate) {
        if (xmlDate == null) return null;

        LocalDate localDate = LocalDate.of(
                xmlDate.getYear(),
                xmlDate.getMonth(),
                xmlDate.getDay()
        );
        return Date.valueOf(localDate);
    }

    // Fluent interface methods for chaining
    public Employee withEmployeeId(long employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public Employee withName(String name) {
        this.name = name;
        return this;
    }

    public Employee withAddress(String address) {
        this.address = address;
        return this;
    }

    public Employee withEmail(String email) {
        this.email = email;
        return this;
    }

    public Employee withDepartment(String department) {
        this.department = department;
        return this;
    }

    public Employee withBirthDate(Date birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public Employee withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Employee withSalary(BigDecimal salary) {
        this.salary = salary;
        return this;
    }
}