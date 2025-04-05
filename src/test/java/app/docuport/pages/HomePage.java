package app.docuport.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    @FindBy (xpath = "//button[@role='button']")
    public WebElement selfButton;


    @FindBy (linkText = "Profile")
    public WebElement profileButton;


    public void navigateToProfilePage (){
        selfButton.click();
        profileButton.click();
    }


}
