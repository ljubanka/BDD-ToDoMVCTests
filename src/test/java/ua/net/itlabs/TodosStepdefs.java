package ua.net.itlabs;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.*;

import java.util.ArrayList;
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

    @When("^add tasks: (.*)$")
    public void addTasks(List<String> taskTexts) {
        for (String text: taskTexts) {
            $("#new-todo").setValue(text).pressEnter();
        }
    }

    @When("^edit task '(.*)' to have text '(.*)' and press Enter$")
    public void editTaskAndPressEnter(String oldText, String newText) {
        startEdit(oldText, newText).sendKeys(ENTER);
    }

    @When("^edit task '(.*)' to have text '(.*)' and press Tab$")
    public void editTaskAndPressTab(String oldText, String newText) {
        startEdit(oldText, newText).sendKeys(TAB);
    }

    @When("^edit task '(.*)' to have text '(.*)' and press Escape$")
    public void editTaskAndPressEscape(String oldText, String newText) {
        startEdit(oldText, newText).sendKeys(ESCAPE);
    }

    public SelenideElement startEdit(String oldText, String newText) {
        doubleClick(tasks.find(exactText(oldText)).find("label"));
        return tasks.find(cssClass("editing")).find(".edit").setValue(newText);
    }

    @And("^delete task '(.*)'$")
    public void deleteTask(String taskText) {
        tasks.find(exactText(taskText)).hover();
        tasks.find(exactText(taskText)).find(".destroy").click();
    }

    @And("^toggle task '(.*)'")
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

//    @When("^add completed tasks$")
//    public void addCompletedTasks(List<String> taskTexts) {
//        addTasks(taskTexts);
//        toggleAllTasks();
//    }

    @And("^clear completed tasks$")
    public void clearCompletedTasks() {
        $("#clear-completed").click();
    }

    @Then("^no tasks left$")
    public void noTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

    @Then("^tasks are: (.*)$")
    public void tasksAre(List<String> taskTexts) {
        tasks.shouldHave(exactTexts(taskTexts));
    }

    @And("^(\\d+) item\\(s\\) left$")
    public void itemsLeft(int count) {
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(count)));
    }

    @And("^clear completed button is not shown$")
    public void clearCompletedButtonIsNotShown() {
        $("#clear-completed").shouldNotBe(visible);
    }

    @Given("^added (.*) tasks: (.*)$")
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

    @Given("^tasks:$")
    public void givenTasks(DataTable data) {
        Map<String, TaskType> givenTasks = data.asMap(String.class, TaskType.class);
        ArrayList<String> names = new ArrayList<String>(givenTasks.keySet());
        ArrayList<TaskType> statuses = new ArrayList<TaskType>(givenTasks.values());

        Task tasksArray[] = new Task[names.size()];
        for (int i=0; i<names.size(); i++) {
            tasksArray[i] = aTask(names.get(i), statuses.get(i));
        }
        givenAtAll(tasksArray);

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
