package ua.net.itlabs;

import com.codeborne.selenide.CollectionCondition;

import java.util.List;

public class CustomConditions {
    public static CollectionCondition exactTexts(List<String> expectedTexts) {
        String[] texts = expectedTexts.toArray(new String[0]);
        return CollectionCondition.exactTexts(texts);
    }
}
