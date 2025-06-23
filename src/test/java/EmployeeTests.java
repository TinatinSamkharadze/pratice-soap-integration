import ge.tbc.testautomation.EmployeeSteps;
import org.testng.annotations.Test;

import static ge.tbc.testautomation.ConstantsSoap.UPDATED_NAME;

public class EmployeeTests {
    EmployeeSteps steps = new EmployeeSteps();

    @Test
    public void addEmployeeTest()
    {
        steps
                .prepareEmployeeInfo()
                .insertEmployeeIntoDB()
                .prepareGetEmployeeByIdRequestBody(15)
                .sendGetEmployeeByIdRequest()
                .validateStatusCode()
                .prepareEmployeeInfoForUpdate()
                .prepareUpdateRequestBody()
                .sendRequestForUpdate()
                .selectEmployeeByID(15)
                .validateEmployeeIsUpdated()
                .prepareEmployeeToUpdateInDB()
                .updatePersonInDB()
                .prepareGetEmployeeByIdRequestBody(15)
                .sendGetEmployeeByIdRequest()
                .validateStatusCode()
                .validateReturnedEmployeeName(UPDATED_NAME)
                .prepareDeleteRequest(15)
                .sendRequestForDelete()
                .validateStatusCode()
                .validateDeleteMessage()
                .prepareGetEmployeeByIdRequestBody(15)
                .sendGetEmployeeByIdRequest()
                .validateStatusCode500()
                .validateEmployeeDeletedToDB(15);

    }
}
