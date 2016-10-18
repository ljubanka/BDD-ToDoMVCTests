package ua.net.itlabs;

import com.codeborne.selenide.Selenide;
import org.openqa.selenium.WebElement;

public class Helpers {
    public static void doubleClick(WebElement onElement) {
        Selenide.actions().doubleClick(onElement).perform();
    }
}
