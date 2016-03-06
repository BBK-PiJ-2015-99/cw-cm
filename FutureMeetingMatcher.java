package com.asfaha;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
 
 
public class FutureMeetingMatcher {
    public static Matcher<FutureMeeting> hasId(final int id) {
         return new TypeSafeMatcher<FutureMeeting>() {
             @Override
             public void describeTo(final Description description) {
                 description.appendText("expected result from hasId(): ")
                 .appendValue(id);
             }
             @Override
             public boolean matchesSafely(final FutureMeeting fm) {
                return id == fm.getId();
             }
             /*
             @Override
             public void describeMismatchSafely(final Meeting fm,
             final Description mismatchDescription) {
                 mismatchDescription.appendText("was ").appendValue(
                    fm.getId());
             }*/
             
         };
    }
}