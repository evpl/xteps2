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
package com.plugatar.xteps2.core;

import com.plugatar.xteps2.core.function.ThRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.plugatar.xteps2.core.HookPriority.MAX_HOOK_PRIORITY;
import static com.plugatar.xteps2.core.HookPriority.MIN_HOOK_PRIORITY;

/**
 * Hook container.
 */
public interface HookContainer {

  /**
   * Adds given hook to this container.
   *
   * @param priority the priority
   * @param hook     the hook
   * @throws XtepsException if {@code hook} is null
   *                        or if {@code priority} is not in the range {@link HookPriority#MIN_HOOK_PRIORITY} to
   *                        {@link HookPriority#MAX_HOOK_PRIORITY}
   */
  void addHook(final int priority,
               final ThRunnable<?> hook);

  /**
   * Calls all hooks in this container.
   */
  void callHooks();

  /**
   * Calls all hooks in this container. Exceptions will be added to the
   * given exception as suppressed exceptions.
   *
   * @param baseException the base exception
   * @throws XtepsException if {@code baseException} is null
   */
  void callHooks(Throwable baseException);

  /**
   * Default {@code HookContainer} implementation.
   */
  class Default implements HookContainer {
    private final Queue<HookItem> hookItems;

    /**
     * Ctor.
     */
    public Default() {
      this.hookItems = new ConcurrentLinkedQueue<>();
    }

    @Override
    public final void addHook(final int priority,
                              final ThRunnable<?> hook) {
      if (hook == null) { throw new XtepsException("hook arg is null"); }
      if (priority < MIN_HOOK_PRIORITY || priority > MAX_HOOK_PRIORITY) {
        throw new XtepsException("priority arg not in the range " + MIN_HOOK_PRIORITY + " to " + MAX_HOOK_PRIORITY);
      }
      this.hookItems.add(new HookItem(priority, hook));
    }

    @Override
    public final void callHooks() {
      if (!this.hookItems.isEmpty()) {
        final List<Throwable> exceptions = new ArrayList<>();
        for (final HookItem hookItem : sortedItems(this.hookItems)) {
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
            throw sneakyThrow(exceptions.get(0));
          default:
            final Iterator<Throwable> iterator = exceptions.iterator();
            final Throwable firstEx = iterator.next();
            iterator.forEachRemaining(firstEx::addSuppressed);
            throw sneakyThrow(firstEx);
        }
      }
    }

    @Override
    public final void callHooks(final Throwable baseException) {
      if (baseException == null) { throw new XtepsException("baseException arg is null"); }
      if (!this.hookItems.isEmpty()) {
        for (final HookItem hookItem : sortedItems(this.hookItems)) {
          try {
            hookItem.hook.run();
          } catch (final Throwable ex) {
            baseException.addSuppressed(ex);
          }
        }
      }
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

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> RuntimeException sneakyThrow(final Throwable exception) throws E {
      throw (E) exception;
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
}
