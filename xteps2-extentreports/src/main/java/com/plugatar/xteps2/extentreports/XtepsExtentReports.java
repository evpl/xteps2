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
package com.plugatar.xteps2.extentreports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Test;
import com.plugatar.xteps2.XtepsBase;
import com.plugatar.xteps2.core.Keyword;
import com.plugatar.xteps2.core.StepListener;
import com.plugatar.xteps2.core.TextFormatter;
import com.plugatar.xteps2.core.XtepsException;

import java.util.LinkedList;
import java.util.Map;

import static com.plugatar.xteps2.XtepsBase.textFormatter;

/**
 * {@link StepListener} implementation for Extent Reports.
 */
public class XtepsExtentReports implements StepListener {
  private static final ThreadLocal<LinkedList<ExtentTest>> NODES = new InheritableThreadLocal<LinkedList<ExtentTest>>() {
    @Override
    protected LinkedList<ExtentTest> initialValue() {
      return new LinkedList<>();
    }

    @Override
    protected LinkedList<ExtentTest> childValue(final LinkedList<ExtentTest> parentValue) {
      return new LinkedList<>(parentValue);
    }
  };
  private final String emptyNameReplacement;

  /**
   * Zero-argument public ctor.
   */
  public XtepsExtentReports() {
    final Map<String, String> properties = XtepsBase.properties();
    this.emptyNameReplacement = properties.getOrDefault("xteps.extentreports.emptyNameReplacement", "Step");
  }

  /**
   * Registers given test for current thread.
   *
   * @param test the test
   */
  public static void registerTest(final ExtentTest test) {
    final LinkedList<ExtentTest> nodes = NODES.get();
    if (!nodes.isEmpty()) {
      final LinkedList<ExtentTest> newNodes = new LinkedList<>();
      newNodes.add(test);
      NODES.set(newNodes);
    } else {
      nodes.add(test);
    }
  }

  /**
   * Unregisters current test for current thread.
   */
  public static void unregisterTest() {
    NODES.remove();
  }

  @Override
  public final void stepStarted(final Map<String, ?> artifacts) {
    final TextFormatter formatter = textFormatter();
    final Keyword keyword = Utils.getKeyword(artifacts);
    final String name = formatter.format(Utils.getName(artifacts));
    final String desc = formatter.format(Utils.getDesc(artifacts));
    final Map<String, Object> params = Utils.getParams(artifacts);
    final LinkedList<ExtentTest> nodes = NODES.get();
    if (nodes.isEmpty()) {
      throw new XtepsException("Not found current test");
    }
    final ExtentTest previousNode = nodes.getLast();
    final String keywordStr = keyword.toString();
    final ExtentTest stepNode;
    if (previousNode.getModel().isBDD() && !keywordStr.isEmpty()) {
      final GherkinKeyword gherkinKeyword;
      try {
        gherkinKeyword = new GherkinKeyword(keywordStr);
      } catch (final Exception ex) {
        throw new XtepsException("Can not use keyword " + keywordStr, ex);
      }
      stepNode = previousNode.createNode(gherkinKeyword, name.isEmpty() ? this.emptyNameReplacement : name);
    } else {
      stepNode = previousNode.createNode(Utils.getNameWithKeyword(name, keyword, this.emptyNameReplacement));
    }
    final Test stepModel = stepNode.getModel();
    stepModel.getAuthorSet().clear();
    stepModel.getCategorySet().clear();
    stepModel.getDeviceSet().clear();
    if (!desc.isEmpty() || !params.isEmpty()) {
      stepNode.info(MarkupHelper.createTable(descAndParamsAsArray(desc, params)));
    }
    NODES.get().add(stepNode);
  }

  @Override
  public final void stepPassed() {
    final LinkedList<ExtentTest> nodes = NODES.get();
    switch (nodes.size()) {
      case 0:
        throw new XtepsException("Not found current test");
      case 1:
        throw new XtepsException("Not found current step");
      default:
        nodes.removeLast();
    }
  }

  @Override
  public final void stepFailed(final Throwable exception) {
    final LinkedList<ExtentTest> nodes = NODES.get();
    final ExtentTest stepNode;
    switch (nodes.size()) {
      case 0: {
        final XtepsException baseException = new XtepsException("Not found current test");
        baseException.addSuppressed(exception);
        throw baseException;
      }
      case 1: {
        final XtepsException baseException = new XtepsException("Not found current step");
        baseException.addSuppressed(exception);
        throw baseException;
      }
      default:
        stepNode = nodes.removeLast();
    }
    stepNode.fail(exception);
  }

  private String[][] descAndParamsAsArray(final String desc,
                                          final Map<String, Object> params) {
    int size = (desc.isEmpty() ? 0 : 1) + (params.isEmpty() ? 0 : params.size());
    final String[][] array = new String[size][];
    final int[] counter = {0};
    if (!desc.isEmpty()) {
      array[counter[0]++] = new String[]{"Description", desc};
    }
    final TextFormatter textFormatter = XtepsBase.textFormatter();
    params.forEach((name, value) -> array[counter[0]++] = new String[]{name, textFormatter.format(value)});
    return array;
  }
}
