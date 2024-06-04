package tqs.myvet.BDDTests.pages.AdminInterface;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AllAccountsPage {
    @FindBy(xpath = "/html/body/div/div[1]/main/div/div/div[4]/div[4]")
    public WebElement userCard;

    @FindBy(id = "radix-:r9d:")
    public WebElement modal;

    @FindBy(xpath = "/html/body/div[3]/div[2]/button")
    public WebElement addPetButton;

    @FindBy(id = ":r9n:-form-item")
    public WebElement petNameInput;

    @FindBy(xpath = "/html/body/div[3]/div[2]/div[4]/form/div[2]/button")
    public WebElement petGenreButton;

    @FindBy(xpath = "//span[text()='Male']")
    private WebElement petGenreSelectOption;

    @FindBy(xpath = "/html/body/div[3]/div[2]/div[4]/form/div[3]/button")
    public WebElement petSpeciesButton;

    @FindBy(xpath = "//span[text()='Dog']")
    private WebElement petSpeciesSelectOption;

    public AllAccountsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void addPet(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(modal));

        addPetButton.click();
    }

    public void setPetName(String name) {
        petNameInput.sendKeys(name);
    }

    public void setPetGenre() {
        petGenreButton.click();
        petGenreSelectOption.click();
    }

    public void setPetSpecies() {
        petSpeciesButton.click();
        petSpeciesSelectOption.click();
    }

    public void selectUserCard() {
        userCard.click();
    }

}
