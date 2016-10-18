package ua.net.itlabs;

import com.codeborne.selenide.Configuration;
import cucumber.api.java.After;
import org.junit.Before;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;

public class TodosStepHooks {

    @After("@clean")
    public void clearData() {
        executeJavaScript("localStorage.clear()");
        open("https://todomvc4tasj.herokuapp.com/");
    }

    @Before
    public void setup() {
        Configuration.timeout = 10000;
    }
}
