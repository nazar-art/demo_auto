package com.epam.model.dao.modules.catalogue;

import com.epam.model.dao.IDao;
import com.epam.model.dto.CatalogueManagementDTO;

import java.util.List;

public interface ICatalogueManagementDAO extends IDao<CatalogueManagementDTO> {
    @Override
    CatalogueManagementDTO findById(String id);

    @Override
    List<CatalogueManagementDTO> findListById(String id);
}
