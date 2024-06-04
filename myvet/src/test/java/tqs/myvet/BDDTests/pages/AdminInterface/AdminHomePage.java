package tqs.myvet.BDDTests.pages.AdminInterface;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdminHomePage {
    @FindBy(xpath = "/html/body/div/div[1]/main/div/div/div[1]/div/h1")
    public WebElement title;

    @FindBy(xpath = "/html/body/div/div[1]/main/div/div/div[3]/a[1]")
    public WebElement createAccountButton;

    @FindBy(xpath = "/html/body/div/div[1]/main/div/div/div[3]/a[3]")
    public WebElement bookAppointmentButton;

    @FindBy(xpath = "/html/body/div/div[1]/main/div/div/div[3]/a[2]")
    public WebElement allAccountsButton;

    public AdminHomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public String getTitle(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // espera até 10 segundos
        wait.until(ExpectedConditions.visibilityOf(title));

        return title.getText();
    }

    public void createAccount() {
        createAccountButton.click();
    }

    public void bookAppointment() {
        bookAppointmentButton.click();
    }

    public void allAccounts() {
        allAccountsButton.click();
    }
}
