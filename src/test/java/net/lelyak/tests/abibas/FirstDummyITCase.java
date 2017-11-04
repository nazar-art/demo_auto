package net.lelyak.tests.abibas;

import net.lelyak.business.AdminBO;
import net.lelyak.dp.impl.AdminDP;
import net.lelyak.model.dto.AdminDTO;
import net.lelyak.tests.TestBase;
import org.testng.annotations.Test;

public class FirstDummyITCase extends TestBase {

    @Test(dataProviderClass = AdminDP.class, dataProvider = "ValidateLogin")
    public void validateLoginAndPassFieldFormat(AdminDTO adminDTO) {
        AdminBO adminBO = new AdminBO();
        asserter.assertFail(adminBO.validateLoginField(adminDTO),
                "Fail: Login field is not valid.",
                "Pass: Login field is valid.");
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
