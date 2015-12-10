package com.epam.tests.modules.catalogue;

import com.epam.business.CatalogueManagementBO;
import com.epam.dp.modules.CatalogueManagementDP;
import com.epam.model.dto.CatalogueManagementDTO;
import com.epam.tests.TestBase;
import org.testng.annotations.Test;

public class CatalogueManagementITCase extends TestBase {

    @Test(dataProviderClass = CatalogueManagementDP.class, dataProvider = "ViewCatalogueManagementPage")
    public void isCatalogueManagementPageOpenTest(CatalogueManagementDTO managementDTO) throws Exception {
        CatalogueManagementBO managementBO = new CatalogueManagementBO();
        asserter.assertFail(managementBO.isCatalogueManagementPageOpen(managementDTO),
                "Fail: Catalogue Management page is not open.",
                "Pass: Catalogue Management page is open.");
    }

    @Test(dataProviderClass = CatalogueManagementDP.class, dataProvider = "AddNewCatalogue")
    public void testAddNewCatalogue(CatalogueManagementDTO managementDTO) throws Exception {
        CatalogueManagementBO managementBO = new CatalogueManagementBO();
        asserter.assertFail(managementBO.isNewCatalogueCanceled(managementDTO),
                "Fail: New Catalogue is added.",
                "Pass: New Catalogue is not added.");

        asserter.assertFail(managementBO.isNewCatalogueAdded(managementDTO),
                "Fail: New Catalogue is not added.",
                "Pass: New Catalogue is added.");

        managementBO.deleteCatalogue(managementDTO.getShortName());
    }

    @Test(dataProviderClass = CatalogueManagementDP.class, dataProvider = "DeleteCatalogue")
    public void testDeleteCatalogue(CatalogueManagementDTO managementDTO) throws Exception {
        CatalogueManagementBO managementBO = new CatalogueManagementBO();

        managementBO.openCatalogueManagementPage(managementDTO);
        asserter.assertFail(managementBO.isNewCatalogueDeleted(managementDTO),
                "Fail: New Catalogue is not deleted.",
                "Pass: New Catalogue is deleted.");

    }
}
