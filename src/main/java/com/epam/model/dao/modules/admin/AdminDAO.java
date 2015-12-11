package com.epam.model.dao.modules.admin;

import com.epam.core.common.XlsReader;
import com.epam.model.dao.modules.XlsHelper;
import com.epam.model.dto.AdminDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminDAO implements IAdminDAO {

    private XlsReader xls;

    @Override
    public AdminDTO findById(String id) {
        xls = new XlsReader("AdminInputData.xlsx", "Admin");
        Map<String, String> testData = xls.getDataById(id);

        AdminDTO adminNew = new AdminDTO();
        XlsHelper.fillObject(adminNew, testData);

        return adminNew;
    }

    @Override
    public List<AdminDTO> findListById(String id) {
        xls = new XlsReader("AdminInputData.xlsx", "Admin");
        List<Map<String, String>> testData = xls.getDataListById(id);
        if (testData != null && !testData.isEmpty()) {
            List<AdminDTO> adminData = new ArrayList<AdminDTO>();
            for (Map<String, String> dataItem : testData) {
                AdminDTO adminNew = new AdminDTO();
                XlsHelper.fillObject(adminNew, dataItem);

                adminData.add(adminNew);
            }
            return adminData;
        }
        return null;
    }
}
