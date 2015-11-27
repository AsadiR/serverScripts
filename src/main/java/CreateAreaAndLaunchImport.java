import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class CreateAreaAndLaunchImport {
    private static final String propertyFilePath = "src/createAreaAndLaunchImport.property";

    private static String baseUrl;
    private static int numberOfAreasToCreate;
    private static String mainPartOfProjectName;
    private static String eclipseWorkspacePath;
    private static String importEngineName;
    private static String login;
    private static String password;

    private static WebDriver driver;
    private static WebDriverWait wait;

    public static void readConfig() throws Exception{
        Properties props = new Properties();
        props.load(new FileInputStream(new File(propertyFilePath)));

        baseUrl = String.valueOf(props.getProperty("baseUrl"));
        numberOfAreasToCreate = Integer.valueOf(props.getProperty("numberOfAreasToCreate"));
        mainPartOfProjectName = String.valueOf(props.getProperty("mainPartOfProjectName"));
        eclipseWorkspacePath = String.valueOf(props.getProperty("eclipseWorkspacePath"));
        importEngineName = String.valueOf(props.getProperty("importEngineName"));
        login = String.valueOf(props.getProperty("login"));
        password = String.valueOf(props.getProperty("password"));
    }

    public static void main(String[] args) throws Exception {
        readConfig();
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, 10);

        try {
            driver.manage().window().maximize();
            driver.get(baseUrl);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jazz_app_internal_LoginWidget_0_userId")));
            driver.findElement(By.id("jazz_app_internal_LoginWidget_0_userId")).sendKeys(login);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jazz_app_internal_LoginWidget_0_password")));
            driver.findElement(By.id("jazz_app_internal_LoginWidget_0_password")).sendKeys(password);
            driver.findElement(By.cssSelector("button")).click();

            for (int i = 0; i < numberOfAreasToCreate; i++) {
                driver.get(baseUrl);
                WebElement menuElement;
                WebElement submenuElement;
                menuElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jazz_ui_MenuPopup_3")));
                menuElement.click();
                submenuElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jazz_ui_menu_MenuItem_2_text")));
                submenuElement.click();

                WebElement nameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("name")));
                nameField.clear();
                nameField.sendKeys(mainPartOfProjectName + i);

                new Select(wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.padded > div > select")))).selectByVisibleText("Blank");

                driver.findElement(By.xpath("(//a[contains(text(),'Add...')])[1]")).click();

                //find text field for search
                WebElement searchField = driver.findElement(By.className("searchText"));
                searchField.clear();
                searchField.sendKeys(login);

                wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                By.xpath("//div[position()=5]/select[position()=1]/option[position()=1]")
                        )
                ).click();

                //add and close
                wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                By.xpath("//div[position()=7]/button[position()=1]")
                        )
                ).click();

                driver.findElement(By.xpath("//div[position()=4]/div[position()=2]/button")).click();

                wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                By.xpath("//span[@id='jazz_ui_toolbar_Button_4']/a/span[@class='button-label']")
                        )
                ).click();

                ///TODO:
                //import engine defenition
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("jazz_ui_MenuPopup_6"))).click();
                driver.findElement(By.id("jazz_ui_menu_MenuItem_0_text")).click();

                wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                By.id("rmps_importer_ImportDefinitionEditor__ChoiceSection_0_c1")
                        )
                ).click();
                driver.findElement(By.cssSelector("label[title=\"Import all the model data from an Eclipse workspace. This mode uses an installed version of Eclipse to access the workspace. \"]")).click();


                WebElement IDelem = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input.editable")));
                IDelem.clear();
                IDelem.sendKeys(importEngineName);
                driver.findElement(By.xpath("(//input[@type='text'])[4]")).clear();
                driver.findElement(By.xpath("(//input[@type='text'])[4]")).sendKeys(eclipseWorkspacePath);
                driver.findElement(By.id("rmps_ui_SimpleButton_0")).click();

                //launch import
                driver.findElement(By.cssSelector("#jazz_ui_MenuPopup_6 > span.caret.jazz-ui-MenuPopup-caret")).click();
                driver.findElement(By.id("jazz_ui_menu_MenuItem_1_text")).click();

                wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                By.id("rmps_ui_SimpleButton_1")
                        )
                ).click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(5000L);
        driver.close();
    }
}
