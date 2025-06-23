package ge.tbc.testautomation;

import com.example.springboot.soap.interfaces.*;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import static ge.tbc.testautomation.ConstantsSoap.*;
import static ge.tbc.testautomation.Marshall.marshallSoapRequest;
import static io.restassured.RestAssured.given;
import static io.restassured.config.XmlConfig.xmlConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasXPath;

public class EmployeeSteps {
    ObjectFactory objectFactory = new ObjectFactory();
    Response response;
    String body;
    EmployeeInfo employeeInfo;
    public Employee employee;
    Faker faker = new Faker();
    Employee dbEmployee;
    Employee employeeToUpdate;
    @Step("Prepare employee info for addition")
    public EmployeeSteps prepareEmployeeInfo() {
        employee = new Employee()
                .withEmployeeId(15)
                .withName("Saba")
                .withAddress("Sorcerer Kingdom")
                .withEmail("Saba_Kvekvesqiri@gmail.com")
                .withDepartment("Sales")
                .withBirthDate(Date.valueOf(LocalDate.of(2004, 1, 11)))
                .withPhone("12345678")
                .withSalary(BigDecimal.valueOf(50000));
        return this;
    }

    @Step("Insert employee into H2 database")
    public EmployeeSteps insertEmployeeIntoDB() {
        EmployeeMapper mapper = DataBaseConfig.dbMapper();
        mapper.insertEmployee(employee);
        return this;
    }


    @Step("Validate the status code of the response")
    public EmployeeSteps validateStatusCode() {
        response.then().statusCode(200);
        return this;
    }


    @Step("Prepare request body to get employee by ID: {0}")
    public EmployeeSteps prepareGetEmployeeByIdRequestBody(long id) {
        GetEmployeeByIdRequest getEmployeeByIdRequest = objectFactory.createGetEmployeeByIdRequest();
        getEmployeeByIdRequest.setEmployeeId(id);
        this.body = marshallSoapRequest(getEmployeeByIdRequest);
        return this;
    }


    @Step("Send request to get employee by ID")
    public EmployeeSteps sendGetEmployeeByIdRequest() {
        response = given()
                .contentType("text/xml")
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", ACTION_GET_EMPLOYEE_BY_ID)
                .body(body)
                .when()
                .post(LOCALHOST_BASE_URI);
        return this;
    }

    @Step("Validate returned employee name: {0}")
    public EmployeeSteps validateReturnedEmployeeName(String name) {
        response.then()
                .assertThat()
                .body(hasXPath("//*[local-name()='name' and text()='" + name + "']"));
        return this;
    }


    @Step("Prepare employee info for updating")
    public EmployeeSteps prepareEmployeeInfoForUpdate() {
        employeeInfo = objectFactory.createEmployeeInfo();
        employeeInfo.setEmployeeId(15);
        employeeInfo.setName(UPDATED_NAME);
        employeeInfo.setEmail(UPDATED_EMAIL);
        employeeInfo.setAddress(UPDATED_ADDRESS);
        return this;
    }

    @Step("Create update employee request body")
    public EmployeeSteps prepareUpdateRequestBody() {
        UpdateEmployeeRequest updateEmployeeRequest = objectFactory.createUpdateEmployeeRequest();
        updateEmployeeRequest.setEmployeeInfo(employeeInfo);
        body = marshallSoapRequest(updateEmployeeRequest);
        return this;
    }

    @Step("Send request to update employee")
    public EmployeeSteps sendRequestForUpdate() {
        response = given()
                .config(RestAssured.config().xmlConfig(xmlConfig()
                        .declareNamespace(PREFIX_NS2, NAMESPACE_URI_SOAP_SPRINGBOOT)
                        .declareNamespace(PREFIX_SOAP_ENV, NAMESPACE_URI_SOAP_ENVELOPE)))
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", ACTION_UPDATE_EMPLOYEE)
                .body(body)
                .post(ConstantsSoap.LOCALHOST_BASE_URI);
        return this;
    }

    @Step("Get employee by id through database")
    public EmployeeSteps selectEmployeeByID(int id)
    {
        EmployeeMapper employeeMapper = DataBaseConfig.dbMapper();
        dbEmployee = employeeMapper.selectById(id);
        return this;
    }

    @Step("validate employee is updated")
    public EmployeeSteps validateEmployeeIsUpdated()
    {
        assertThat(dbEmployee.getName(), equalTo(UPDATED_NAME));
        return this;
    }

    @Step("Update employee in database")
    public EmployeeSteps prepareEmployeeToUpdateInDB() {
      employeeToUpdate = new Employee()
                .withEmployeeId(15)
                .withName(UPDATED_NAME)
                .withEmail(UPDATED_EMAIL)
                .withAddress(UPDATED_ADDRESS)
                .withDepartment("Updated Department")
                .withPhone("Updated Phone");
            return this;
    }

    @Step("Update person in DB")
    public EmployeeSteps updatePersonInDB()
    {
        EmployeeMapper mapper = DataBaseConfig.dbMapper();
        mapper.updatePerson(employeeToUpdate);
        return this;
    }


    @Step("Prepare request body for deleting employee with ID: {0}")
    public EmployeeSteps prepareDeleteRequest(long id) {
        DeleteEmployeeRequest deleteEmployeeRequest = objectFactory.createDeleteEmployeeRequest();
        deleteEmployeeRequest.setEmployeeId(id);
        body = marshallSoapRequest(deleteEmployeeRequest);
        return this;
    }

    @Step("Send request to delete employee")
    public EmployeeSteps sendRequestForDelete() {
        response = given()
                .config(RestAssured.config().xmlConfig(xmlConfig()
                        .declareNamespace(PREFIX_NS2, NAMESPACE_URI_SOAP_SPRINGBOOT)
                        .declareNamespace(PREFIX_SOAP_ENV, NAMESPACE_URI_SOAP_ENVELOPE)))
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", ACTION_DELETE_EMPLOYEE)
                .body(body)
                .post(ConstantsSoap.LOCALHOST_BASE_URI);
        return this;
    }

    @Step("Validate the delete confirmation message in response")
    public EmployeeSteps validateDeleteMessage() {
        response.then()
                .assertThat()
                .body(hasXPath("//*[local-name()='message' and text()='" + CONTENT_DELETED_SUCCESSFULLY + "']"));
        return this;
    }

    @Step("Validate the status code of the response is 500")
    public EmployeeSteps validateStatusCode500() {
        response.then().statusCode(500);
        return this;
    }

    @Step("Validate employee was added to database")
    public EmployeeSteps validateEmployeeDeletedToDB(int id) {
        EmployeeMapper mapper = DataBaseConfig.dbMapper();
        int specificCount = mapper.countEmployeeById(id);
        assertThat(specificCount, equalTo(0));
        return this;
    }

}