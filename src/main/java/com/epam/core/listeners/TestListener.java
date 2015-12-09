package com.epam.core.listeners;


import com.epam.core.logging.Logger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.util.ArrayList;
import java.util.List;

public class TestListener implements IInvokedMethodListener {

    private static List<String> exceptions;
    private String currentMessage = "";

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        String methodName = iTestResult.getMethod().getMethodName();
        if (methodName != null && !exceptionsList().contains(methodName)) {
            if (!currentMessage.equals("START TEST CASE: " + methodName)) {
                currentMessage = "START TEST CASE: " + methodName;
                Logger.logDebug(currentMessage);
                Logger.setTest(true);
            }
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        String methodName = iTestResult.getMethod().getMethodName();
        if (methodName != null && !exceptionsList().contains(methodName)) {
            if (!currentMessage.equals("END TEST CASE: " + methodName)) {
                currentMessage = "END TEST CASE: " + methodName;
                Logger.logDebug(currentMessage);
            }
        }
    }

    private List<String> exceptionsList() {
        if (exceptions == null) {
            exceptions = new ArrayList<String>();
            exceptions.add("checkAllPages");
            exceptions.add("start");
            exceptions.add("end");
            exceptions.add("createUsers");
            exceptions.add("springTestContextPrepareTestInstance");
            exceptions.add("springTestContextBeforeTestMethod");
            exceptions.add("springTestContextBeforeTestClass");
            exceptions.add("springTestContextAfterTestMethod");
            exceptions.add("springTestContextAfterTestClass");
        }
        return exceptions;
    }
}
