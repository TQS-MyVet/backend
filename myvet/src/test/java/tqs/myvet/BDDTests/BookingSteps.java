package tqs.myvet.BDDTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import tqs.myvet.BDDTests.pages.AdminInterface.AdminBookingPage;
import tqs.myvet.BDDTests.pages.AdminInterface.AdminHomePage;
import tqs.myvet.BDDTests.pages.AdminInterface.AdminLoginPage;
import tqs.myvet.BDDTests.pages.AdminInterface.CreateAccountPage;
import tqs.myvet.BDDTests.pages.UserInterface.BookingPage;
import tqs.myvet.BDDTests.pages.UserInterface.HomePage;
import tqs.myvet.BDDTests.pages.UserInterface.LoginPage;

public class BookingSteps {
    private WebDriver driver;
    private LoginPage loginPage;
    private AdminLoginPage adminLoginPage;
    private HomePage homePage;
    private AdminHomePage adminHomePage;
    private BookingPage bookingPage;
    private AdminBookingPage adminBookingPage;
    private CreateAccountPage createAccountPage;

    @Given("the pet owner is on the User Interface home page")
    public void thePetOwnerIsOnTheUserInterfaceHomePage() {
        driver = WebDriverManager.firefoxdriver().create();
        driver.manage().window().maximize();
        driver.get("http://localhost/user/");
        homePage = new HomePage(driver);
    }

    @And("clicks on the Login button")
    public void clicksOnTheLoginButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/nav/div/div/div[3]/a/span")));

        homePage.login();
    }

    @And("enters the email and password")
    public void andEntersTheEmailAndPassword() {
        loginPage = new LoginPage(driver);
        assertEquals("Login", loginPage.getTitle());

        loginPage.setEmail("user@email.com");
        loginPage.setPassword("password");
    }

    @And("clicks on the Login submit button")
    public void clicksOnTheLoginSubmitButton() {
        loginPage.submit();
    }

    @And("clicks on the Booking button")
    public void clicksOnTheBookingButton() {
        homePage.booking();
    }

    @And("is presented with a calendar of available dates")
    public void isPresentedWithACalendarOfAvailableDates() {
        bookingPage = new BookingPage(driver);
        assertEquals("Booking Appointment", bookingPage.getTitle());
    }

    @When("the pet owner selects a date")
    public void whenThePetOwnerSelectsADate() {
        bookingPage.selectBookingDay();
    }

    @And("fills the form")
    public void fillsTheForm() {
        bookingPage.setTitle("Test Booking", driver);
        bookingPage.selectType();
        bookingPage.selectDoctor();
        bookingPage.selectOwner();
    }

    @Given("the receptionist is on the login page")
    public void theReceptionistIsOnTheLoginPage() {
        driver = WebDriverManager.firefoxdriver().create();
        driver.manage().window().maximize();
        driver.get("http://localhost/admin/login");
        adminLoginPage = new AdminLoginPage(driver);
    }

    @When("the receptionist clicks on the Create Account button")
    public void theReceptionistClicksOnTheCreateAccountButton() {
        adminHomePage = new AdminHomePage(driver);
        assertEquals("Home Page", adminHomePage.getTitle(driver));
        adminHomePage.createAccount();
    }

    @And("fills in the pet owner's information and their pet's information")
    public void fillsInThePetOwnerSInformationAndTheirPetSInformation() {
        createAccountPage = new CreateAccountPage(driver);
        assertEquals("Account Creation for Pets", createAccountPage.getTitle());

        createAccountPage.sendFormData("Joe", "test@gmail.com", "919191919", "Bobi",
                "male", "dog");
    }

    @And("clicks on the Create Account button")
    public void clicksOnTheCreateAccountButton() {
        createAccountPage.submitForm();
    }

    @Then("the pet owner and their pet are registered")
    public void thePetOwnerAndTheirPetAreRegistered() {
        assertTrue(createAccountPage.isPopUpVisible());
        driver.quit();
    }

    @And("logs in with valid credentials")
    public void logsInWithValidCredentials() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));

        adminLoginPage.setEmail("receptionist@email.com");
        adminLoginPage.setPassword("password");
        adminLoginPage.submit();
    }

    @And("clicks on the Book Appointment button")
    public void clicksOnTheBookAppointmentButton() {
        adminHomePage = new AdminHomePage(driver);
        assertEquals("Home Page", adminHomePage.getTitle(driver));
        adminHomePage.bookAppointment();
    }

    @And("the receptionist is presented with a calendar of available dates")
    public void theReceptionistIsPresentedWithACalendarOfAvailableDates() {
        adminBookingPage = new AdminBookingPage(driver);
        assertEquals("Booking Appointment", adminBookingPage.getTitle(driver));
    }

    @When("the receptionist selects a date")
    public void theReceptionistSelectsADate() {
        adminBookingPage.selectBookingDay();
    }

    @And("the receptionist fills the form")
    public void theReceptionistFillsTheForm() {
        adminBookingPage.setTitle("Test Booking", driver);
        adminBookingPage.selectType();
        adminBookingPage.selectDoctor();
        adminBookingPage.selectOwner();
        adminBookingPage.selectPet();
        adminBookingPage.selectDate();
        adminBookingPage.setEstimatedDuration("0130");
    }

    @And("the receptionist clicks on the Submit button")
    public void theReceptionistClicksOnTheSubmitButton() {
        adminBookingPage.submit();
    }

}
