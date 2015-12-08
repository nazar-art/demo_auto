package com.epam.dp.modules;

import com.epam.dp.BaseDP;
import com.epam.dp.DataProviderHelper;
import com.epam.model.dao.generator.IGeneratorDAO;
import com.epam.model.dto.CatalogueManagementDTO;
import org.testng.annotations.DataProvider;

public class GeneratorDP extends BaseDP {

    private static IGeneratorDAO generator = daoFactory.getGeneratorDAO();

    @DataProvider(parallel = true)
    public static Object[][] catalogueDTO() {
        CatalogueManagementDTO testData = generator.getNewCatalogue();
        return DataProviderHelper.toObject(testData);
    }

    public static CatalogueManagementDTO getCatalogueDTO() {
        return generator.getNewCatalogue();
    }
}
