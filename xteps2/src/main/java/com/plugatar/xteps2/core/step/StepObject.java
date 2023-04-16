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
package com.plugatar.xteps2.core.step;

import com.plugatar.xteps2.core.XtepsException;

import java.util.Optional;

/**
 * Step object.
 */
public interface StepObject {

  /**
   * Returns step object with given artifact.
   *
   * @param name  the artifact name
   * @param value the artifact value
   * @return step object with given artifact
   * @throws XtepsException if {@code value} arg is null
   */
  StepObject withArtifact(String name,
                          Object value);

  /**
   * Returns artifact by name.
   *
   * @param name the artifact name
   * @return optional artifact value
   * @throws XtepsException if {@code name} arg is null
   */
  Optional<Object> artifact(String name);
}
