package tests;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.Page_Home;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidateDataOfTableTest {
    public static WebDriver driver;
    public static String homeUrl = "https://testpages.herokuapp.com/styled/tag/dynamic-table.html";

    @BeforeTest
    public void launchHomePage() {
        try {
            ChromeOptions option = new ChromeOptions();
            option.addArguments("--remote-allow-origins=*");
            System.setProperty("webdriver.chrome.driver",
                    System.getProperty("user.dir") + "\\src\\main\\resources\\chromedriver.exe");
            driver = new ChromeDriver(option);
            driver.manage().window().maximize();
            driver.get(homeUrl);
            Thread.sleep(5000);

            String expectedUrl = driver.getCurrentUrl();
            Assert.assertEquals(expectedUrl, homeUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void inputTableData() {
        Page_Home pageHome = new Page_Home(driver);
        try {
            pageHome.button_table.click();
            // Read JSON data from file
            String jsonFilePath = "C:\\Users\\Anushka Garg\\Desktop\\Assignment_2\\src\\main\\resources\\data.json";
            String jsonData = readJsonFile(jsonFilePath);

            //input data in textbox
            pageHome.input_data.clear();
            pageHome.input_data.sendKeys(jsonData);
            pageHome.button_refreshTable.click();
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Test
    public void validateTableData() {
        try {

            // Read JSON data from file
            String jsonFilePath = "C:\\Users\\Anushka Garg\\Desktop\\Assignment_2\\src\\main\\resources\\data.json";
            String jsonData = readJsonFile(jsonFilePath);

            // Parse JSON and extract relevant information
            List<Map<String,String>> uiTableData = getUITableData(driver);
            List<Map<String,String>> jsonTableData = parseJsonAndGetTableData(jsonData);
            Thread.sleep(5000);
            boolean resultMatch = assertTableData(uiTableData, jsonTableData);
            Assert.assertTrue(resultMatch,"All entries matched");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterTest
    public void quitDriver() {
        driver.quit();
    }

    private static String readJsonFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<Map<String, String>> getUITableData(WebDriver driver) {
        Page_Home pageHome = new Page_Home(driver);
        List<Map<String, String>> uiData = new ArrayList<>();

        // List<WebElement> rows = driver.findElements(By.xpath("//table[@id='dynamictable']/tr"));
        try{
            for (int i = 1; i < pageHome.table_data.size(); i++) {
                WebElement row = pageHome.table_data.get(i);
                List<WebElement> columns = row.findElements(By.tagName("td"));

                // Assuming columns order in the UI table matches the order in JSON
                String name = columns.get(0).getText();
                String age = columns.get(1).getText();
                String gender = columns.get(2).getText();
                Map<String, String> dataMap = new HashMap<>();
                dataMap.put("name", name);
                dataMap.put("age", age);
                dataMap.put("gender", gender);

                uiData.add(dataMap);
            }
            System.out.println("UI Data: "+uiData);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error getting ui data " + e.getMessage());
        }
        return uiData;

    }

    private static List<Map<String, String>> parseJsonAndGetTableData(String jsonData) {
        List<Map<String, String>> jsonTableData = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonData);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Map<String, String> dataMap = new HashMap<>();
                dataMap.put("name", jsonObject.getString("name"));
                dataMap.put("age", String.valueOf(jsonObject.getInt("age")));
                dataMap.put("gender", jsonObject.getString("gender"));

                jsonTableData.add(dataMap);
            }
            System.out.println("Json data: "+jsonData);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error parsing JSON: " + e.getMessage());
        }

        return jsonTableData;
    }


    private static boolean assertTableData(List<Map<String,String>> uiTableData, List<Map<String,String>> jsonTableData) {
        // Compare the data from the UI table with the data from the JSON
        boolean dataMatched = false;
        if (uiTableData.equals(jsonTableData)) {
            dataMatched = true;
            System.out.println("Data from UI table matches data from JSON.");
        } else {
            System.out.println("Data from UI table does not match data from JSON.");
        }
        return dataMatched;
    }
}
