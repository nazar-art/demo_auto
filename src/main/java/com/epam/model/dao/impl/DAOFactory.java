package com.epam.model.dao.impl;

import com.epam.model.dao.impl.admin.IAdminDAO;
import org.springframework.beans.factory.annotation.Autowired;

public class DAOFactory {

    @Autowired
    private IAdminDAO adminDAO;

    public IAdminDAO getAdminDAO() {
        return adminDAO;
    }

}
