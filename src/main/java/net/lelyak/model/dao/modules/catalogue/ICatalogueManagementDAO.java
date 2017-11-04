package net.lelyak.model.dao.modules.catalogue;

import net.lelyak.model.dao.IDao;
import net.lelyak.model.dto.CatalogueManagementDTO;

import java.util.List;

public interface ICatalogueManagementDAO extends IDao<CatalogueManagementDTO> {
    @Override
    CatalogueManagementDTO findById(String id);

    @Override
    List<CatalogueManagementDTO> findListById(String id);
}
