/*
 * Copyright 2023 Evgenii Plugatar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.plugatar.xteps2.core;

/**
 * Keyword.
 */
public interface Keyword {

  /**
   * Returns keyword string representation.
   *
   * @return string representation
   */
  @Override
  String toString();

  /**
   * Default {@code Keyword} implementation.
   */
  class Of implements Keyword {
    private final String name;

    /**
     * Ctor.
     *
     * @param name the name
     * @throws XtepsException if {@code name} arg is null
     */
    public Of(final String name) {
      if (name == null) { throw new XtepsException("name arg is null"); }
      this.name = name;
    }

    @Override
    public final String toString() {
      return this.name;
    }
  }
}
