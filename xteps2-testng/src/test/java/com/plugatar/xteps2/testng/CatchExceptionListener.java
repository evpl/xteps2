/*
 * Copyright 2023 Evgenii Plugatar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.plugatar.xteps2.testng;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.lang.reflect.InvocationTargetException;

public class CatchExceptionListener implements IInvokedMethodListener {
  private static volatile Throwable lastException = null;

  public CatchExceptionListener() {
  }

  @Override
  public final void afterInvocation(final IInvokedMethod method,
                                    final ITestResult testResult) {
    final Throwable testEx = trueTestException(testResult);
    lastException = testEx;
    if (testEx instanceof Exception) {
      testResult.setStatus(ITestResult.SUCCESS);
      testResult.setThrowable(null);
    }
  }

  public static Throwable lastException() {
    return lastException;
  }

  private static Throwable trueTestException(final ITestResult testResult) {
    final Throwable rawTestEx = testResult.getThrowable();
    return rawTestEx instanceof InvocationTargetException
      ? ((InvocationTargetException) rawTestEx).getTargetException()
      : rawTestEx;
  }

  public static class Exception extends RuntimeException {

    public Exception(final String message) {
      super(message);
    }
  }
}
