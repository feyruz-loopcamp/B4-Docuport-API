package app.docuport.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProfilePage extends BasePage {

    @FindBy (xpath = "//div[@class='mt-2 col col-12']")
    public WebElement fullName;


    @FindBy (xpath = "//div[@class='col col-12']/span")
    public WebElement roleName;

}
