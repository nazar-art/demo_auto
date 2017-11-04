package net.lelyak.model.dao.modules.admin;


import net.lelyak.model.dao.IDao;
import net.lelyak.model.dto.AdminDTO;

import java.util.List;

public interface IAdminDAO extends IDao<AdminDTO> {
    @Override
    AdminDTO findById(String id);

    @Override
    List<AdminDTO> findListById(String id);
}
