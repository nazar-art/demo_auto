package net.lelyak.dp.impl;

import net.lelyak.dp.BaseDP;
import net.lelyak.dp.DataProviderHelper;
import net.lelyak.model.dao.modules.admin.IAdminDAO;
import net.lelyak.model.dto.AdminDTO;
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
