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

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Tests for {@link TextFormatter}.
 */
final class TextFormatterTest {
  private static final Pattern DEFAULT_PATTEN = Pattern.compile("\\{([^}]*)}");

  @Test
  void ctorThrowsExceptionForNullPatternArg() {
    assertThatCode(() -> new TextFormatter.Default((Pattern) null, true, true))
      .isInstanceOf(XtepsException.class);
  }

  @Test
  void formatMethodForObject() {
    final TextFormatter formatter = new TextFormatter.Default(DEFAULT_PATTEN, false, false);
    final Map<String, Object> replacements = new HashMap<>();
    replacements.put("rep", new Obj());

    assertThat(formatter.format("Text {rep}", replacements))
      .isEqualTo("Text obj string");
  }

  @Test
  void formatMethodForArray() {
    final TextFormatter formatter = new TextFormatter.Default(DEFAULT_PATTEN, false, false);
    final Map<String, Object> replacements = new HashMap<>();
    replacements.put("rep", new Object[]{'a', 'b', 'c'});

    assertThat(formatter.format("Text {rep}", replacements))
      .isEqualTo("Text [a, b, c]");
  }

  @Test
  void formatMethodFieldForceAccessTrue() {
    final TextFormatter formatter = new TextFormatter.Default(DEFAULT_PATTEN, true, false);
    final Map<String, Object> replacements = new HashMap<>();
    replacements.put("rep", new Obj());

    assertThat(formatter.format("Text {rep.publicIntField} {rep.privateIntField}", replacements))
      .isEqualTo("Text 1 2");
  }

  @Test
  void formatMethodFieldForceAccessFalse() {
    final TextFormatter formatter = new TextFormatter.Default(DEFAULT_PATTEN, false, false);
    final Map<String, Object> replacements = new HashMap<>();
    replacements.put("rep", new Obj());

    assertThat(formatter.format("Text {rep.publicIntField}", replacements))
      .isEqualTo("Text 1");
  }

  @Test
  void formatMethodFieldForceAccessFalseException() {
    final TextFormatter formatter = new TextFormatter.Default(DEFAULT_PATTEN, false, false);
    final Map<String, Object> replacements = new HashMap<>();
    replacements.put("rep", new Obj());

    assertThatCode(() -> formatter.format("Text {rep.privateIntField}", replacements))
      .isInstanceOf(TextFormatException.class);
  }

  @Test
  void formatMethodMethodForceAccessTrue() {
    final TextFormatter formatter = new TextFormatter.Default(DEFAULT_PATTEN, false, true);
    final Map<String, Object> replacements = new HashMap<>();
    replacements.put("rep", new Obj());

    assertThat(formatter.format("Text {rep.publicIntMethod()} {rep.privateIntMethod()}", replacements))
      .isEqualTo("Text 3 4");
  }

  @Test
  void formatMethodMethodForceAccessFalse() {
    final TextFormatter formatter = new TextFormatter.Default(DEFAULT_PATTEN, false, false);
    final Map<String, Object> replacements = new HashMap<>();
    replacements.put("rep", new Obj());

    assertThat(formatter.format("Text {rep.publicIntMethod()}", replacements))
      .isEqualTo("Text 3");
  }

  @Test
  void formatMethodMethodForceAccessFalseException() {
    final TextFormatter formatter = new TextFormatter.Default(DEFAULT_PATTEN, false, false);
    final Map<String, Object> replacements = new HashMap<>();
    replacements.put("rep", new Obj());

    assertThatCode(() -> formatter.format("Text {rep.privateIntMethod()}", replacements))
      .isInstanceOf(TextFormatException.class);
  }

  @Test
  void formatMethodGetArrayElement() {
    final TextFormatter formatter = new TextFormatter.Default(DEFAULT_PATTEN, false, false);
    final Map<String, Object> replacements = new HashMap<>();
    replacements.put("rep", new Object[]{'a', 'b', 'c'});

    assertThat(formatter.format("Text {rep.[1]}", replacements))
      .isEqualTo("Text b");
  }

  @Test
  void formatMethodGetIterableElement() {
    final TextFormatter formatter = new TextFormatter.Default(DEFAULT_PATTEN, false, false);
    final Map<String, Object> replacements = new HashMap<>();
    replacements.put("rep", Arrays.asList('a', 'b', 'c'));

    assertThat(formatter.format("Text {rep.[1]}", replacements))
      .isEqualTo("Text b");
  }

  @Test
  void longReplacement() {
    final TextFormatter formatter = new TextFormatter.Default(DEFAULT_PATTEN, false, false);
    final Map<String, Object> replacements = new HashMap<>();
    replacements.put("rep", Arrays.asList('a', 'b', new Obj()));

    assertThat(formatter.format("Text {rep.[2].returnThis().returnThis().publicIntField}", replacements))
      .isEqualTo("Text 1");
  }

  static final class Obj {
    public int publicIntField = 1;
    private int privateIntField = 2;

    public int publicIntMethod() {
      return 3;
    }

    private int privateIntMethod() {
      return 4;
    }

    public Obj returnThis() {
      return this;
    }

    @Override
    public String toString() {
      return "obj string";
    }
  }
}
