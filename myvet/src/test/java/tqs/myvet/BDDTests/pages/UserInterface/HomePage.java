package tqs.myvet.BDDTests.pages.UserInterface;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(xpath = "/html/body/div/nav/div/div/div[3]/a/span")
    public WebElement loginButton;

    @FindBy(xpath = "/html/body/div/nav/div/div/div[2]/div/div/a[1]")
    public WebElement bookingButton;

    public HomePage(WebDriver driver) {
        driver.get("http://localhost/user/");
        PageFactory.initElements(driver, this);
    }

    public void login() {
        loginButton.click();
    }

    public void booking() {
        bookingButton.click();
    }
}
