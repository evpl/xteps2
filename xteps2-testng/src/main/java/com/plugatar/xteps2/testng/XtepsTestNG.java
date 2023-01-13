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

import com.plugatar.xteps2.XtepsBase;
import com.plugatar.xteps2.core.TestHookContainer;
import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.function.ThRunnable;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import static com.plugatar.xteps2.core.HookPriority.MAX_HOOK_PRIORITY;
import static com.plugatar.xteps2.core.HookPriority.MIN_HOOK_PRIORITY;

public class XtepsTestNG implements TestHookContainer, IInvokedMethodListener {
  private static final Map<String, Queue<HookItem>> HOOKS = new ConcurrentHashMap<>();
  private static final ThreadLocal<String> CURRENT_TEST_ID = new ThreadLocal<>();

  public XtepsTestNG() {
  }

  @Override
  public final void addHook(final int priority,
                            final ThRunnable<?> hook) {
    if (hook == null) { throw new XtepsException("hook arg is null"); }
    if (priority < MIN_HOOK_PRIORITY || priority > MAX_HOOK_PRIORITY) {
      throw new XtepsException("priority arg not in the range " + MIN_HOOK_PRIORITY + " to " + MAX_HOOK_PRIORITY);
    }
    final String testId = CURRENT_TEST_ID.get();
    if (testId == null) {
      throw new XtepsException("Current test not found");
    }
    HOOKS.computeIfAbsent(testId, t -> new ConcurrentLinkedQueue<>()).add(new HookItem(priority, hook));
  }

  @Override
  public final void beforeInvocation(final IInvokedMethod method,
                                     final ITestResult testResult) {
    if (testResult.getMethod().isTest()) {
      CURRENT_TEST_ID.set(UUID.randomUUID().toString());
    }
  }

  @Override
  public final void afterInvocation(final IInvokedMethod method,
                                    final ITestResult testResult) {
    if (method.getTestMethod().isTest()) {
      final String testId = CURRENT_TEST_ID.get();
      CURRENT_TEST_ID.remove();
      if (testId != null) {
        final Throwable testEx = trueTestException(testResult);
        if (testEx != null) {
          callHooks(testId, testEx);
          handleException(testEx);
        } else {
          final Throwable hookEx = callHooks(testId);
          if (hookEx != null) {
            handleException(hookEx);
            testResult.setStatus(ITestResult.FAILURE);
            testResult.setThrowable(hookEx);
          }
        }
      }
    }
  }

  private static void handleException(final Throwable exception) {
    XtepsBase.exceptionHandler().handle(exception);
  }

  private static Throwable trueTestException(final ITestResult testResult) {
    final Throwable rawTestEx = testResult.getThrowable();
    return rawTestEx instanceof InvocationTargetException
      ? ((InvocationTargetException) rawTestEx).getTargetException()
      : rawTestEx;
  }

  private static void callHooks(final String testId,
                                final Throwable testEx) {
    HOOKS.computeIfPresent(testId, (id, hookItems) -> {
      for (final HookItem hookItem : sortedItems(hookItems)) {
        try {
          hookItem.hook.run();
        } catch (final Throwable ex) {
          testEx.addSuppressed(ex);
        }
      }
      return null;
    });
  }

  private static Throwable callHooks(final String testId) {
    final AtomicReference<Throwable> resultRef = new AtomicReference<>();
    HOOKS.computeIfPresent(testId, (id, hookItems) -> {
      final List<Throwable> exceptions = new ArrayList<>();
      for (final HookItem hookItem : sortedItems(hookItems)) {
        try {
          hookItem.hook.run();
        } catch (final Throwable ex) {
          exceptions.add(ex);
        }
      }
      switch (exceptions.size()) {
        case 0:
          break;
        case 1:
          resultRef.set(exceptions.get(0));
          break;
        default:
          final Iterator<Throwable> iterator = exceptions.iterator();
          final Throwable firstEx = iterator.next();
          iterator.forEachRemaining(firstEx::addSuppressed);
          resultRef.set(firstEx);
      }
      return null;
    });
    return resultRef.get();
  }

  private static HookItem[] sortedItems(final Queue<HookItem> hookItems) {
    final int size = hookItems.size();
    final HookItem[] array = new HookItem[size];
    HookItem val = hookItems.poll();
    for (int idx = 0; idx < size && val != null; ++idx, val = hookItems.poll()) {
      array[idx] = val;
    }
    Arrays.sort(array);
    return array;
  }

  private static final class HookItem implements Comparable<HookItem> {
    private final int priority;
    private final ThRunnable<?> hook;

    private HookItem(final int priority,
                     final ThRunnable<?> hook) {
      this.priority = priority;
      this.hook = hook;
    }

    @Override
    public int compareTo(final HookItem another) {
      return Integer.compare(another.priority, this.priority);
    }
  }
}
