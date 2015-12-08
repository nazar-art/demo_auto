package com.epam.tests.impl;

import com.epam.business.AdminBO;
import com.epam.core.datafactory.RandomDataSource;
import com.epam.core.logging.Logger;
import com.epam.dp.impl.AdminDP;
import com.epam.dp.modules.GeneratorDP;
import com.epam.model.dto.AdminDTO;
import com.epam.model.dto.CatalogueManagementDTO;
import com.epam.tests.TestBase;
import org.testng.annotations.Test;

public class FirstDummyITCase extends TestBase {

    @Test(dataProviderClass = AdminDP.class, dataProvider = "ValidateLogin")
    public void validateLoginAndPassFieldFormat(AdminDTO adminDTO) {
        AdminBO adminBO = new AdminBO();
        asserter.assertFail(adminBO.validateLoginField(adminDTO),
                "Fail: Login field is not valid.",
                "Pass: Login field is valid.");

        // usage of random generator
        CatalogueManagementDTO userDTO = GeneratorDP.getCatalogueDTO();
        Logger.logInfo("HERE IS DESCRIPTION - " + userDTO.getDescription());

        // or explicit
        RandomDataSource randomDataSource = new RandomDataSource();
        randomDataSource.fillEntity(adminDTO);
    }

    @Test(dataProviderClass = AdminDP.class, dataProvider = "ValidateLogin")
    public void validatePanelsModulePage(AdminDTO adminDTO) {
        AdminBO adminBO = new AdminBO();
        asserter.assertFail(adminBO.validateLoginField(adminDTO),
                "Fail: Login field is not valid.",
                "Pass: Login field is valid.");

        asserter.assertFail(adminBO.isNewsPanelsModuleOpen(),
                "Fail: News Panels module is not open.",
                "Pass: News Panels module is open.");
    }

    @Test(dataProviderClass = AdminDP.class, dataProvider = "ValidateLogin")
    public void validateSeasonModulePage(AdminDTO adminDTO) {
        AdminBO adminBO = new AdminBO();
        asserter.assertFail(adminBO.validateLoginField(adminDTO),
                "Fail: Login field is not valid.",
                "Pass: Login field is valid.");

        asserter.assertFail(adminBO.isSeasonsModuleOpen(),
                "Fail: Seasons module is not open.",
                "Pass: Seasons module is open.");

    }



}
