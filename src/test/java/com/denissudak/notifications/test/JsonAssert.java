package com.denissudak.notifications.test;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.ReadContext;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.StringAssert;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public class JsonAssert extends StringAssert {
    private JsonAssert(String actual) {
        super(actual);
    }

    public <T> JsonAssert jsonPath(String jsonPath, Matcher<T> matcher) {
        if (StringUtils.isBlank(actual)) {
            throw new AssertionError("json string is empty");
        }
        ReadContext ctx = JsonPath.parse(actual);

        try {
            Object result = ctx.read(jsonPath);
            if (!matcher.matches(result)) {
                Description errorDescription = new StringDescription();
                errorDescription.appendText("Json path " + jsonPath + " expected value ");
                errorDescription.appendDescriptionOf(matcher);
                errorDescription.appendText(" but ");
                matcher.describeMismatch(result, errorDescription);
                throw new AssertionError(errorDescription.toString());
            }
        } catch (JsonPathException e) {
            throw new AssertionError(e.getMessage());
        }

        return this;
    }

    public static JsonAssert assertThat(String actual) {
        return new JsonAssert(actual);
    }
}
