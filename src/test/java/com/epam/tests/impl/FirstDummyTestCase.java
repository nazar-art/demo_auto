package com.epam.tests.impl;

import com.epam.business.AdminBO;
import com.epam.dp.impl.AdminDP;
import com.epam.tests.TestBase;
import com.epam.model.dto.AdminDTO;
import org.testng.annotations.Test;

public class FirstDummyTestCase extends TestBase {

    @Test(dataProviderClass = AdminDP.class, dataProvider = "ValidateLogin")
    public void validateLoginAndPassFieldFormat(AdminDTO adminDTO) {
        AdminBO adminBO = new AdminBO();
        asserter.assertFail(adminBO.validateLoginField(adminDTO),
                "Fail: Login field is not valid.",
                "Pass: Login field is valid.");

        asserter.assertFail(adminBO.isNewsPanelsModuleOpen(),
                "Fail: News Panels module is not open.",
                "Pass: News Panels module is open.");

        asserter.assertFail(adminBO.isSeasonsModuleOpen(),
                "Fail: Seasons module is not open.",
                "Pass: Seasons module is open.");

    }

}
