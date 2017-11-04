package net.lelyak.model.dao.modules;

import net.lelyak.model.dao.modules.admin.IAdminDAO;
import net.lelyak.model.dao.modules.catalogue.ICatalogueManagementDAO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
public class DAOFactory {

    @Autowired
    private IAdminDAO adminDAO;

    @Autowired
    private ICatalogueManagementDAO catalogueManagementDAO;

}
