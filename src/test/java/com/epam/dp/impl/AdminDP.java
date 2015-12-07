package com.epam.dp.impl;

import com.epam.dp.BaseDP;
import com.epam.dp.DataProviderHelper;
import com.epam.model.dao.modules.admin.IAdminDAO;
import com.epam.model.dto.AdminDTO;
import org.testng.annotations.DataProvider;

import java.util.Iterator;
import java.util.List;

public class AdminDP {

    private static IAdminDAO adminDAO = BaseDP.daoFactory.getAdminDAO();

    @DataProvider(parallel = true)
    public static Iterator<Object[]> ValidateLogin() {
        List<AdminDTO> testData = adminDAO.findListById("ValidateLogin");
        return DataProviderHelper.toListObject(testData);
    }
}
