package tqs.myvet.BDDTests.pages.UserInterface;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BookingPage {

    @FindBy(xpath = "/html/body/div/div[1]/div/div/div[1]/div/h1")
    private WebElement title;

    @FindBy(xpath = "/html/body/div/div[1]/div/div/div[3]/div/div/div/div[2]/div[6]")
    private WebElement selectBookingDay;

    @FindBy(id = "radix-:rj:")
    private WebElement modal;

    @FindBy(name = "title")
    private WebElement titleInput;

    @FindBy(xpath = "/html/body/div[3]/p/form/div[1]/div[2]/button")
    private WebElement typeButton;

    @FindBy(xpath = "//span[text()='Clinical analysis']")
    private WebElement typeSelectOption;

    @FindBy(xpath = "/html/body/div[3]/p/form/div[1]/div[3]/button")
    private WebElement doctorButton;

    @FindBy(xpath = "//span[text()='John Doe']")
    private WebElement doctorSelectOption;

    @FindBy(xpath = "/html/body/div[3]/p/form/div[1]/div[4]/button")
    private WebElement ownerButton;

    @FindBy(xpath = "//span[text()='User Doe']")
    private WebElement ownerSelectOption;

    @FindBy(xpath = "/html/body/div[3]/p/form/div[2]/div[1]/button")
    private WebElement petButton;

    @FindBy(xpath = "//span[text()='Bobi']")
    private WebElement petSelectOption;

    @FindBy(id = ":r31:-form-item")
    private WebElement dateInput;

    @FindBy(xpath = "//li[text()='5:30 PM']")
    private WebElement timeListItem;

    @FindBy(id = ":r35:-form-item")
    private WebElement estimatedDurationInput;

    @FindBy(xpath = "/html/body/div[3]/p/form/div[3]/button[1]")
    private WebElement submitButton;

    public BookingPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public String getTitle() {
        return title.getText();
    }

    public void selectBookingDay() {
        selectBookingDay.click();
    }

    public void setTitle(String title, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(modal));

        titleInput.sendKeys(title);
    }

    public void selectType() {
        typeButton.click();
        typeSelectOption.click();
    }

    public void selectDoctor() {
        doctorButton.click();
        doctorSelectOption.click();
    }

    public void selectOwner() {
        ownerButton.click();
        ownerSelectOption.click();
    }

    public void selectPet() {
        petButton.click();
        petSelectOption.click();
    }

    public void selectDate() {
        dateInput.click();
        timeListItem.click();
    }

    public void setEstimatedDuration(String duration) {
        estimatedDurationInput.sendKeys(duration);
    }

    public void submit() {
        submitButton.click();
    }
}
