package tqs.myvet.BDDTests.pages.UserInterface;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(xpath = "/html/body/div/div[1]/div/div/div/div[1]/div/div[1]/h1")
    private WebElement title;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(xpath = "/html/body/div/div[1]/div/div/div/div[1]/div/div[2]/div/form/div[2]/button")
    private WebElement submitButton;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public String getTitle() {
        return title.getText();
    }

    public void setEmail(String email) {
        emailInput.sendKeys(email);
    }

    public void setPassword(String password) {
        passwordInput.sendKeys(password);
    }

    public void submit() {
        submitButton.click();
    }

}
