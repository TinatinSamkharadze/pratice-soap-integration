package ge.tbc.testautomation;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface EmployeeMapper {

    @Insert("INSERT INTO employee (birth_date, salary, employee_id, address, department, email, name, phone) " +
            "VALUES (#{birthDate}, #{salary}, #{employeeId}, #{address}, #{department}, #{email}, #{name}, #{phone})")
    void insertEmployee(Employee employee);

    @Select("DELETE FROM employee WHERE employee_Id = #{employee_Id}")
    void deletePerson(int id);


    @Select("SELECT * FROM employee WHERE employee_Id = #{employee_Id}")
    Employee selectById(long employeeId);


    @Select("SELECT * FROM employee")
    List<Employee> selectAll();

    @Select("SELECT * FROM employee WHERE name = #{name}")
    List<Employee> selectByName(String name);


    @Select("SELECT COUNT(*) FROM Employee")
    int count();


    @Select("UPDATE Employee SET " +
            "name = #{name}, department = #{department}, " +
            "phone = #{phone}, address = #{address}, email = #{email} WHERE employee_Id = #{employeeId}")
    void updatePerson(Employee employee);

    @Select("SELECT COUNT(*) FROM Employee WHERE employee_Id = #{employeeId}")
    int countEmployeeById(long employeeId);
}