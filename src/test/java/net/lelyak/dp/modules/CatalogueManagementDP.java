package net.lelyak.dp.modules;

import net.lelyak.dp.BaseDP;
import net.lelyak.dp.DataProviderHelper;
import net.lelyak.model.dao.modules.catalogue.ICatalogueManagementDAO;
import net.lelyak.model.dto.CatalogueManagementDTO;
import org.testng.annotations.DataProvider;

import java.util.Iterator;
import java.util.List;

public class CatalogueManagementDP {

    private static ICatalogueManagementDAO catalogueDAO = BaseDP.daoFactory.getCatalogueManagementDAO();

    @DataProvider(parallel = true)
    public static Iterator<Object[]> ViewCatalogueManagementPage() {
        List<CatalogueManagementDTO> testData = catalogueDAO.findListById("ViewCatalogueManagementPage");
        return DataProviderHelper.toListObject(testData);
    }

    @DataProvider(parallel = true)
    public static Iterator<Object[]> AddNewCatalogue() {
        List<CatalogueManagementDTO> testData = catalogueDAO.findListById("AddNewCatalogue");
        return DataProviderHelper.toListObject(testData);
    }

    @DataProvider(parallel = true)
    public static Iterator<Object[]> DeleteCatalogue() {
        List<CatalogueManagementDTO> testData = catalogueDAO.findListById("DeleteCatalogue");
        return DataProviderHelper.toListObject(testData);
    }

}
