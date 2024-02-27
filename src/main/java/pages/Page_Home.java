package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class Page_Home {
    WebDriver driver ;
    public Page_Home(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    @FindBy(xpath = "//div[@class='centered']//summary")
    public WebElement button_table;

    @FindBy(xpath = "//textarea[@id='jsondata']")
    public WebElement input_data;
    @FindBy(xpath = "//button[@id='refreshtable']")
    public WebElement button_refreshTable;
    @FindBy(xpath = "//table[@id='dynamictable']/tr")
    public List<WebElement> table_data;
}
