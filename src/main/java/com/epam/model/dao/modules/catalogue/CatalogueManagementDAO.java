package com.epam.model.dao.modules.catalogue;

import com.epam.core.common.XlsReader;
import com.epam.core.datafactory.RandomDataSource;
import com.epam.model.dao.modules.XlsHelper;
import com.epam.model.dto.CatalogueManagementDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CatalogueManagementDAO implements ICatalogueManagementDAO {

    private XlsReader xls;
    private RandomDataSource data = new RandomDataSource();

    @Override
    public CatalogueManagementDTO findById(String id) {
        xls = new XlsReader("AdminInputData.xlsx", "CatalogueManagement");
        Map<String, String> testData = xls.getDataById(id);

        CatalogueManagementDTO managementDTO = new CatalogueManagementDTO();
        XlsHelper.fillObject(managementDTO, testData);

        return managementDTO;
    }

    @Override
    public List<CatalogueManagementDTO> findListById(String id) {
        xls = new XlsReader("AdminInputData.xlsx", "CatalogueManagement");
        List<Map<String, String>> testData = xls.getDataListById(id);

        if (testData != null && !testData.isEmpty()) {
            List<CatalogueManagementDTO> catalogueData = new ArrayList<CatalogueManagementDTO>();
            for (Map<String, String> dataItem : testData) {
                CatalogueManagementDTO managementDTO = new CatalogueManagementDTO();
                XlsHelper.fillObject(managementDTO, dataItem);
                // fill with random generated data
                data.fillEntity(managementDTO);

                catalogueData.add(managementDTO);
            }
            return catalogueData;
        }
        return null;
    }
}
