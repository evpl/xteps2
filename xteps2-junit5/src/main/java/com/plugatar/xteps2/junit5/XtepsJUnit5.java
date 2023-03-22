/*
 * Copyright 2022 Evgenii Plugatar
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

import com.plugatar.xteps2.XtepsBase;
import com.plugatar.xteps2.core.TestHookContainer;
import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.function.ThRunnable;
import org.junit.jupiter.api.extension.DynamicTestInvocationContext;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicReference;

import static com.plugatar.xteps2.core.HookPriority.MAX_HOOK_PRIORITY;
import static com.plugatar.xteps2.core.HookPriority.MIN_HOOK_PRIORITY;

public class XtepsJUnit5 implements TestHookContainer, InvocationInterceptor {
  private static final Map<String, Set<HookItem>> HOOKS = new ConcurrentHashMap<>();
  private static final ThreadLocal<String> CURRENT_TEST_ID = new ThreadLocal<>();

  public XtepsJUnit5() {
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
      throw new XtepsException("No running test");
    }
    HOOKS.computeIfAbsent(testId, t -> new ConcurrentSkipListSet<>()).add(new HookItem(priority, hook));
  }

  @Override
  public final void interceptTestMethod(final Invocation<Void> invocation,
                                        final ReflectiveInvocationContext<Method> invocationContext,
                                        final ExtensionContext extensionContext) throws Throwable {
    invoke(invocation);
  }

  @Override
  public final void interceptTestTemplateMethod(final Invocation<Void> invocation,
                                                final ReflectiveInvocationContext<Method> invocationContext,
                                                final ExtensionContext extensionContext) throws Throwable {
    invoke(invocation);
  }

  @Override
  public final void interceptDynamicTest(final Invocation<Void> invocation,
                                         final DynamicTestInvocationContext invocationContext,
                                         final ExtensionContext extensionContext) throws Throwable {
    invoke(invocation);
  }

  private static <T> T invoke(final Invocation<T> invocation) throws Throwable {
    final String testId = UUID.randomUUID().toString();
    CURRENT_TEST_ID.set(testId);
    T returnedValue = null;
    Throwable testEx = null;
    try {
      returnedValue = invocation.proceed();
    } catch (final Throwable ex) {
      testEx = ex;
    }
    CURRENT_TEST_ID.remove();
    if (testEx == null) {
      final Throwable hookEx = callHooks(testId);
      if (hookEx == null) {
        return returnedValue;
      } else {
        handleException(hookEx);
        throw hookEx;
      }
    } else {
      callHooks(testId, testEx);
      handleException(testEx);
      throw testEx;
    }
  }

  private static void handleException(final Throwable exception) {
    XtepsBase.exceptionHandler().handle(exception);
  }

  private static void callHooks(final String testId,
                                final Throwable testEx) {
    HOOKS.computeIfPresent(testId, (id, hookItems) -> {
      for (final HookItem hookItem : hookItems) {
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
      for (final HookItem hookItem : hookItems) {
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
