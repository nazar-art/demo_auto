package com.epam.pages.adidas;

import com.epam.core.annotations.Page;
import com.epam.core.components.WebFieldDecorator;
import com.epam.core.components.element.Button;
import com.epam.core.components.element.Image;
import com.epam.core.components.element.NavigationLink;
import com.epam.pages.PageObject;
import com.epam.pages.adidas.productfeeds.SeasonsPage;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

@Page(title = "WelcomePage")
public class WelcomePage extends PageObject {

    public WelcomePage() {
        successor = new HashMap<Class<? extends PageObject>, Button>();
        PageFactory.initElements(new WebFieldDecorator(getDriver()), this);
    }

    @Override
    public boolean exist() {
        return linkFeedback.isPresent()
                && linkIncidents.isPresent()
                && logoAdidas.isPresent();
    }


    @FindBy(id = "new-feedback-button")
    protected NavigationLink linkFeedback;

    @FindBy(id = "new-incident-button")
    protected NavigationLink linkIncidents;

    @FindBy(css = ".brand-logo")
    protected Image logoAdidas;

    // NAVIGATION BAR
    @FindBy(linkText = "Scheduling")
    private NavigationLink linkToScedulingModule;

    @FindBy(linkText = "Templates")
    private NavigationLink linkToTemplatesModule;

    @FindBy(linkText = "Catalogue Management")
    private NavigationLink linkToCatalogueManagementModule;

    @FindBy(linkText = "CMS Configurations")
    private NavigationLink linkToCMSConfigurationsModule;

    @FindBy(linkText = "Consumers")
    private NavigationLink linkToConsumersModule;

    @FindBy(xpath = "//a[contains(text(), 'Seasons')]")
    private NavigationLink linkToProductFeedsModuleSeasons;




    @FindBy(linkText = "News Panels")
    private NavigationLink linkToNewsPanelsModule;



    public NewsPanelsPage openNewsPanelsModule() {
        linkToNewsPanelsModule.click();
        NewsPanelsPage panelsPage = new NewsPanelsPage();
        panelsPage.waitPageLoad();
        return panelsPage;
    }

    public SeasonsPage openProductFeedsModuleSeasons() {
        linkToProductFeedsModuleSeasons.click();
        SeasonsPage seasonsPage = new SeasonsPage();
        seasonsPage.waitPageLoad();
        return seasonsPage;
    }



    public void clickLogo() {
        logoAdidas.click();
    }
}
