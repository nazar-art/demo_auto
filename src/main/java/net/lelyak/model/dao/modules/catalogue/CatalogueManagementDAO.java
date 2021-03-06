package net.lelyak.model.dao.modules.catalogue;

import net.lelyak.core.common.XlsReader;
import net.lelyak.core.datafactory.RandomDataSource;
import net.lelyak.model.dao.modules.XlsHelper;
import net.lelyak.model.dto.CatalogueManagementDTO;

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
        data.fillEntity(managementDTO);

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
                data.fillEntity(managementDTO);

                catalogueData.add(managementDTO);
            }
            return catalogueData;
        }
        return null;
    }
}
