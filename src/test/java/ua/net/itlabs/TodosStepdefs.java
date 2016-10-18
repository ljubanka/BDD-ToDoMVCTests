package ua.net.itlabs;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import cucumber.api.PendingException;
import cucumber.api.java.en.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.refresh;
import static org.openqa.selenium.Keys.ENTER;
import static org.openqa.selenium.Keys.ESCAPE;
import static org.openqa.selenium.Keys.TAB;
import static ua.net.itlabs.CustomConditions.exactTexts;
import static ua.net.itlabs.Helpers.doubleClick;

public class TodosStepdefs {

    ElementsCollection tasks = $$("#todo-list>li");

    @Given("^open TodoMVC page at All filter$")
    public void open_TodoMVC_page() {
        open("https://todomvc4tasj.herokuapp.com/");
    }

    @When("^add tasks (.*)$")
    public void addTasks(List<String> taskTexts) {
        for (String text: taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    @Then("^tasks are: (.*)$")
    public void tasksAre(List<String> taskTexts) {
        tasks.shouldHave(exactTexts(taskTexts));
    }

    @And("^delete task (.*)$")
    public void deleteTask(String taskText) {
        tasks.find(exactText(taskText)).hover();
        tasks.find(exactText(taskText)).find(".destroy").click();
    }

    @And("^toggle task (.*)")
    public void toggleTask(String taskText) {
        tasks.find(exactText(taskText)).find(".toggle").click();
    }

    @And("^toggle tasks (.*)$")
    public void toggleTasks(List<String> taskTexts) {
        for (String text: taskTexts) {
            toggleTask(text);
        }
    }

    @And("^toggle all tasks$")
    public void toggleAllTasks() {
        $("#toggle-all").click();
    }

//    @But("^(.) item(s) left$")
//    public void itemsLeft(int count) {
//        $("#todo-count>strong").shouldHave(exactText(Integer.toString(count)));
//    }

    @When("^add completed tasks$")
    public void addCompletedTasks(List<String> taskTexts) {
        addTasks(taskTexts);
        toggleAllTasks();
    }



    @And("^clear completed tasks$")
    public void clearCompletedTasks() {
        $("#clear-completed").click();
    }

    @Then("^notasks$")
    public void noTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

    @When("^edit task '(.*)' to '(.*)' and press Escape$")
    public void editTaskAndPressEscape(String oldTask, String newTask) {
        doubleClick(tasks.find(exactText(oldTask)).find("label"));
        tasks.find(cssClass("editing")).find(".edit").setValue(newTask).sendKeys(ESCAPE);
    }

    @But("^(\\d+) item\\(s\\) left$")
    public void itemsLeft(int count) {
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(count)));
    }


    @Given("^added (.*) tasks (.*)$")
    public void givenTasks(TaskType taskType, List<String> taskTexts) {
        givenAtAll(aTasks(taskType, taskTexts.toArray(new String[0])));
    }

    public void givenAtAll(Task... tasks) {
        String strJS = "localStorage.setItem(\"todos-troopjs\", \"[  ";
        for (Task task : tasks) {
            strJS += task;
        }
        strJS = strJS.substring(0, strJS.length()-2);
        strJS = strJS + "]\")";
        executeJavaScript(strJS);
        refresh();
    }

    public void givenAtAll(TaskType taskType, String... taskTexts) {
        givenAtAll(aTasks(taskType, taskTexts));
    }

    public Task[] aTasks(TaskType taskType, String... taskTexts) {
        Task tasksArray[] = new Task[taskTexts.length];
        for (int i=0; i<taskTexts.length; i++) {
            tasksArray[i] = aTask(taskTexts[i], taskType);
        }
        return tasksArray;
    }

    public Task aTask(String name, TaskType type) {
        return new Task(name, type);
    }

    @When("^edit task '(.*)' to '(.*)' and press Enter$")
    public void editTaskAndPressEnter(String oldTask, String newTask) {
        doubleClick(tasks.find(exactText(oldTask)).find("label"));
        tasks.find(cssClass("editing")).find(".edit").setValue(newTask).sendKeys(ENTER);
    }

    @When("^edit task '(.*)' to '(.*)' and press Tab$")
    public void editTaskAndPressTab(String oldTask, String newTask) {
        doubleClick(tasks.find(exactText(oldTask)).find("label"));
        tasks.find(cssClass("editing")).find(".edit").setValue(newTask).sendKeys(TAB);
    }

    public class Task {
        String name;
        TaskType type;

        public Task(String name, TaskType type) {
            this.name = name;
            this.type = type;
        }

        public String toString() {
            return "{\\\"completed\\\":" + type + ", \\\"title\\\":\\\"" + name + "\\\"}, ";
        }
    }

    public enum TaskType {
        ACTIVE("false"),
        COMPLETED("true");

        public final String isCompletedValue;

        TaskType(String isCompletedValue) {
            this.isCompletedValue = isCompletedValue;
        }

        public String toString() {
            return this.isCompletedValue;
        }
    }
}
