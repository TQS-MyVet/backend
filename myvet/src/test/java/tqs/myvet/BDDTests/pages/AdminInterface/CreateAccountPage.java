package tqs.myvet.BDDTests.pages.AdminInterface;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateAccountPage {
    private WebDriver driver;

    @FindBy(xpath = "/html/body/div/div[1]/main/div/div/div[1]/div/h1")
    private WebElement title;

    @FindBy(name = "ownerName")
    private WebElement nameInput;

    @FindBy(name = "email")
    private WebElement emailInput;

    @FindBy(name = "phone")
    private WebElement phoneInput;

    @FindBy(name = "petName")
    private WebElement petNameInput;

    @FindBy(xpath = "/html/body/div/div[1]/main/div/div/div[3]/div/form/div[2]/div[1]/button")
    private WebElement selectPetGenreButton;

    @FindBy(xpath = "//span[text()='Male']")
    private WebElement petGenreSelectOption;

    @FindBy(xpath = "/html/body/div/div[1]/main/div/div/div[3]/div/form/div[2]/div[2]/button")
    private WebElement selectPetSpeciesButton;

    @FindBy(xpath = "//span[text()='Dog']")
    private WebElement petSpeciesSelectOption;

    @FindBy(xpath = "//button[contains(@class, 'inline-flex items-center justify-center')]")
    private WebElement calendarButton;

    @FindBy(xpath = "//button[text()='3']")
    private WebElement petDateOfBirth;

    @FindBy(xpath = "/html/body/div/div[1]/main/div/div/div[3]/div/form/div[3]/button")
    private WebElement submitButton;

    @FindBy(xpath = "/html/body/div/div[2]/ol")
    private WebElement popUp;

    public CreateAccountPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public String getTitle() {
        return title.getText();
    }

    public void sendFormData(String name, String email, String phone, String petName, String petGenre,
            String petSpecies) {
        nameInput.sendKeys(name);
        emailInput.sendKeys(email);
        phoneInput.sendKeys(phone);
        petNameInput.sendKeys(petName);

        selectPetGenreButton.click();

        // Aguarda até que o dropdown esteja aberto
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.attributeToBe(selectPetGenreButton, "aria-expanded", "true"));

        petGenreSelectOption.click();

        selectPetSpeciesButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.attributeToBe(selectPetSpeciesButton, "aria-expanded", "true"));

        petSpeciesSelectOption.click();

        calendarButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.attributeToBe(calendarButton, "aria-expanded", "true"));

        petDateOfBirth.click();
    }

    public void submitForm() {
        submitButton.click();
    }

    public boolean isPopUpVisible() {
        return popUp.isDisplayed();
    }
}
