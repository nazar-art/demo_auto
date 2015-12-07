package com.epam.model.dao.modules.admin;


import com.epam.model.dao.IDao;
import com.epam.model.dto.AdminDTO;

import java.util.List;

public interface IAdminDAO extends IDao<AdminDTO> {
    @Override
    AdminDTO findById(String id);

    @Override
    List<AdminDTO> findListById(String id);
}
