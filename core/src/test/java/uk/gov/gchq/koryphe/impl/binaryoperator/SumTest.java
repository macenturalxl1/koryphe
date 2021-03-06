/*
 * Copyright 2017-2020 Crown Copyright
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

package uk.gov.gchq.koryphe.impl.binaryoperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.binaryoperator.BinaryOperatorTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SumTest extends BinaryOperatorTest {

    private Number state;

    @BeforeEach
    public void before() {
        state = null;
    }

    @Test
    public void testAggregateShorts() {
        // Given
        final Sum sum = new Sum();

        // When 1
        state = sum.apply((short) 1, state);

        // Then 1
        assertTrue(state instanceof Short);
        assertEquals((short) 1, state);

        // When 2
        state = sum.apply((short) 3, state);

        // Then 2
        assertTrue(state instanceof Short);
        assertEquals((short) 4, state);

        // When 3
        state = sum.apply((short) 2, state);

        // Then 3
        assertTrue(state instanceof Short);
        assertEquals((short) 6, state);

        // When 4 - check it cannot exceed MAX_VALUE
        state = sum.apply((short) (Short.MAX_VALUE - (short) state + 1), state);

        // Then 4
        assertTrue(state instanceof Short);
        assertEquals(Short.MAX_VALUE, state);
    }

    @Test
    public void testAggregateInIntMode() {
        // Given
        final Sum sum = new Sum();

        // When 1
        state = sum.apply(1, state);

        // Then 1
        assertTrue(state instanceof Integer);
        assertEquals(1, state);

        // When 2
        state = sum.apply(3, state);

        // Then 2
        assertTrue(state instanceof Integer);
        assertEquals(4, state);

        // When 3
        state = sum.apply(2, state);

        // Then 3
        assertTrue(state instanceof Integer);
        assertEquals(6, state);
    }

    @Test
    public void testAggregateInIntModeMixedInput() {
        // Given
        final Sum sum = new Sum();

        // When 1
        state = sum.apply(1, state);

        // Then 1
        assertTrue(state instanceof Integer);
        assertEquals(1, state);

        // When 2
        assertThrows(ClassCastException.class, () -> state = sum.apply(2.7d, state));

        // Then 2
        assertTrue(state instanceof Integer);
        assertEquals(1, state);

        // When 3
        assertThrows(ClassCastException.class, () -> state = sum.apply(1L, state));

        // Then 3
        assertTrue(state instanceof Integer);
        assertEquals(1, state);
    }

    @Test
    public void testAggregateInLongMode() {
        // Given
        final Sum sum = new Sum();

        // When 1
        state = sum.apply(2L, state);

        // Then 1
        assertTrue(state instanceof Long);
        assertEquals(2L, state);

        // When 2
        state = sum.apply(1L, state);

        // Then 2
        assertTrue(state instanceof Long);
        assertEquals(3L, state);

        // When 3
        state = sum.apply(3L, state);

        // Then 3
        assertTrue(state instanceof Long);
        assertEquals(6L, state);
    }

    @Test
    public void testAggregateInLongModeMixedInput() {
        // Given
        final Sum sum = new Sum();
        state = 0L;

        // When 1
        assertThrows(ClassCastException.class, () -> state = sum.apply(1, state));

        // Then 1
        assertEquals(0L, state);

        // When 2
        state = sum.apply(3L, state);

        // Then 2
        assertTrue(state instanceof Long);
        assertEquals(3L, state);

        // When 3
        assertThrows(ClassCastException.class, () -> state = sum.apply(2.5d, state));

        // Then 3
        assertTrue(state instanceof Long);
        assertEquals(3L, state);
    }

    @Test
    public void testAggregateInFloatMode() {
        // Given
        final Sum sum = new Sum();

        // When 1
        state = sum.apply(1.1f, state);

        // Then 1
        assertTrue(state instanceof Float);
        assertEquals(1.1f, state);

        // When 2
        state = sum.apply(2f, state);

        // Then 2
        assertTrue(state instanceof Float);
        assertEquals(3.1f, state);

        // When 3
        state = sum.apply(1.5f, state);

        // Then 3
        assertTrue(state instanceof Float);
        assertEquals(4.6f, state);
    }

    @Test
    public void testAggregateInDoubleMode() {
        // Given
        final Sum sum = new Sum();

        // When 1
        state = sum.apply(1.1d, state);

        // Then 1
        assertTrue(state instanceof Double);
        assertEquals(1.1d, state);

        // When 2
        state = sum.apply(2.1d, state);

        // Then 2
        assertTrue(state instanceof Double);
        assertEquals(3.2d, state);

        // When 3
        state = sum.apply(1.5d, state);

        // Then 3
        assertTrue(state instanceof Double);
        assertEquals(4.7d, state);
    }

    @Test
    public void testAggregateInDoubleModeMixedInput() {
        // Given
        final Sum sum = new Sum();
        state = 0d;

        // When 1
        assertThrows(ClassCastException.class, () -> state = sum.apply(1, state));

        // Then 1
        assertEquals(0d, state);

        // When 2
        assertThrows(ClassCastException.class, () -> state = sum.apply(3L, state));

        // Then 2
        assertEquals(0d, state);

        // When 3
        state = sum.apply(2.1d, state);

        // Then 3
        assertTrue(state instanceof Double);
        assertEquals(2.1d, state);
    }

    @Test
    public void testAggregateInAutoModeIntInputFirst() {
        // Given
        final Sum sum = new Sum();
        state = 0;

        // When 1
        state = sum.apply(1, state);

        // Then 1
        assertTrue(state instanceof Integer);
        assertEquals(1, state);

        // When 2
        assertThrows(ClassCastException.class, () -> state = sum.apply(3L, state));

        // Then 2
        assertTrue(state instanceof Integer);
        assertEquals(1, state);

        // When 3
        assertThrows(ClassCastException.class, () -> state = sum.apply(2.1d, state));

        // Then 3
        assertTrue(state instanceof Integer);
        assertEquals(1, state);
    }

    @Test
    public void testAggregateInAutoModeLongInputFirst() {
        // Given
        final Sum sum = new Sum();
        state = 0L;

        // When 1
        state = sum.apply(1L, state);

        // Then 1
        assertTrue(state instanceof Long);
        assertEquals(1L, state);

        // When 2
        assertThrows(ClassCastException.class, () -> state = sum.apply(3, state));

        // Then 2
        assertTrue(state instanceof Long);
        assertEquals(1L, state);

        // When 3
        assertThrows(ClassCastException.class, () -> state = sum.apply(2.1d, state));

        // Then 3
        assertTrue(state instanceof Long);
        assertEquals(1L, state);
    }

    @Test
    public void testAggregateInAutoModeDoubleInputFirst() {
        // Given
        final Sum sum = new Sum();
        state = 0d;

        // When 1
        state = sum.apply(1.1d, state);

        // Then 1
        assertTrue(state instanceof Double);
        assertEquals(1.1d, state);

        // When 2
        assertThrows(ClassCastException.class, () -> state = sum.apply(2, state));

        // Then 2
        assertTrue(state instanceof Double);
        assertEquals(1.1d, state);

        // When 3
        assertThrows(ClassCastException.class, () -> state = sum.apply(1L, state));

        // Then 3
        assertTrue(state instanceof Double);
        assertEquals(1.1d, state);
    }

    @Test
    public void testAggregateWhenStateIsNullShouldReturnNull() {
        // Given
        final Sum sum = new Sum();

        // When
        state = sum.apply(null, state);

        // Then
        assertNull(state);
    }

    @Test
    public void testAggregateInAutoModeIntInputFirstNullInputSecond() {
        // Given
        final Sum sum = new Sum();

        // When 1
        int firstValue = 1;
        state = sum.apply(firstValue, state);

        // Then
        assertTrue(state instanceof Integer);
        assertEquals(firstValue, state);

        // When 2
        state = sum.apply(null, state);
        // Then
        assertTrue(state instanceof Integer);
        assertEquals(firstValue, state);
    }

    @Test
    public void testAggregateInAutoModeLongInputFirstNullInputSecond() {
        // Given
        final Sum sum = new Sum();

        // When 1
        long firstValue = 1L;
        state = sum.apply(firstValue, state);

        // Then
        assertTrue(state instanceof Long);
        assertEquals(firstValue, state);

        // When 2
        state = sum.apply(null, state);
        // Then
        assertTrue(state instanceof Long);
        assertEquals(firstValue, state);
    }

    @Test
    public void testAggregateInAutoModeDoubleInputFirstNullInputSecond() {
        // Given
        final Sum sum = new Sum();

        // When 1
        double firstValue = 1.0f;
        state = sum.apply(firstValue, state);

        // Then
        assertTrue(state instanceof Double);
        assertEquals(firstValue, state);

        // When 2
        state = sum.apply(null, state);

        // Then
        assertTrue(state instanceof Double);
        assertEquals(firstValue, state);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Sum aggregator = new Sum();

        // When 1
        final String json = JsonSerialiser.serialise(aggregator);

        // Then 1
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.binaryoperator.Sum\"%n" +
                "}"), json);

        // When 2
        final Sum deserialisedAggregator = JsonSerialiser.deserialise(json, Sum.class);

        // Then 2
        assertNotNull(deserialisedAggregator);
    }

    @Override
    protected Sum getInstance() {
        return new Sum();
    }

    @Override
    protected Class<Sum> getFunctionClass() {
        return Sum.class;
    }
}
