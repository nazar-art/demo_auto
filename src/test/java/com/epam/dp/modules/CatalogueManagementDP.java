package com.epam.dp.modules;

import com.epam.dp.BaseDP;
import com.epam.dp.DataProviderHelper;
import com.epam.model.dao.modules.catalogue.ICatalogueManagementDAO;
import com.epam.model.dto.CatalogueManagementDTO;
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


}
