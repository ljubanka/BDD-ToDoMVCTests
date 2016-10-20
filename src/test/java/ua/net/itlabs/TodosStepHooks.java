package ua.net.itlabs;

import com.codeborne.selenide.Configuration;
import cucumber.api.java.After;
import cucumber.api.java.Before;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;

public class TodosStepHooks {

    @Before("@clean")
    public void setup() {
        if (!(Configuration.timeout >= 10000)) {
            Configuration.timeout = 10000;
        }
    }

    @After("@clean")
    public void clearData() {
        executeJavaScript("localStorage.clear()");
        open("https://todomvc4tasj.herokuapp.com/");
    }


}
