package com.epam.model.dao.generator;


import com.epam.core.datafactory.RandomDataSource;
import com.epam.model.dto.CatalogueManagementDTO;

public class GeneratorDAO implements IGeneratorDAO {

    private RandomDataSource data = new RandomDataSource();

    public CatalogueManagementDTO getNewCatalogue() {
        CatalogueManagementDTO managementDTO = new CatalogueManagementDTO();
        data.fillEntity(managementDTO);
        return managementDTO;
    }

}
