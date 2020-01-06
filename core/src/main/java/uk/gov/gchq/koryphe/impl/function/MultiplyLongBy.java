/*
 * Copyright 2019 Crown Copyright
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

package uk.gov.gchq.koryphe.impl.function;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

/**
 * A {@code MultiplyLongBy} is a {@link java.util.function.Function} that takes in
 * an {@link Long} and returns the result of multiplying this long by a pre-configured
 * value.
 */
@Since("1.8.2")
@Summary("Multiplies a long by a provided long")
public class MultiplyLongBy extends KorypheFunction<Long, Long> {
    private long by = 1L;

    public MultiplyLongBy() {
    }

    public MultiplyLongBy(final long by) {
        setBy(by);
    }

    public long getBy() {
        return by;
    }

    public void setBy(final long by) {
        this.by = by;
    }

    public Long apply(final Long input) {
        if (null == input) {
            return null;
        } else {
            return input * by;
        }
    }
}