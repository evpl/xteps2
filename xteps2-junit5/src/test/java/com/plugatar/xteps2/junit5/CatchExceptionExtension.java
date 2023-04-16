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
package com.plugatar.xteps2.junit5;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class CatchExceptionExtension implements TestExecutionExceptionHandler {
  private volatile Throwable exception = null;

  public CatchExceptionExtension() {
  }

  @Override
  public final void handleTestExecutionException(final ExtensionContext context,
                                                 final Throwable throwable) throws Throwable {
    this.exception = throwable;
    if (!(throwable instanceof Exception)) {
      throw throwable;
    }
  }

  public final Throwable exception() {
    return this.exception;
  }

  public static class Exception extends RuntimeException {

    public Exception(final String message) {
      super(message);
    }
  }
}
