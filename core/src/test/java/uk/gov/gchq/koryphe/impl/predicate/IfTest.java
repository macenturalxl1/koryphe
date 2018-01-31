/*
 * Copyright 2017 Crown Copyright
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
package uk.gov.gchq.koryphe.impl.predicate;

import org.junit.Test;

import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.tuple.ArrayTuple;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IfTest extends PredicateTest {

    @Override
    protected If<Object> getInstance() {
        return new If<>(true, new IsA(String.class), new IsA(Integer.class));
    }

    private If<Comparable> getAltInstance() {
        return new If<>(new IsA(Integer.class), new IsLessThan(3), new IsA(String.class));
    }

    @Override
    protected Class<? extends Predicate> getPredicateClass() {
        return If.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final If ifPredicate = getInstance();

        // When
        final String json = JsonSerialiser.serialise(ifPredicate);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.If\",%n" +
                "  \"condition\" : true,%n" +
                "  \"then\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsA\",%n" +
                "    \"type\" : \"java.lang.String\"%n" +
                "  },%n" +
                "  \"otherwise\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsA\",%n" +
                "    \"type\" : \"java.lang.Integer\"%n" +
                "  }%n" +
                "}"), json);

        // When 2
        final If deserialised = JsonSerialiser.deserialise(json, If.class);

        // Then 2
        assertNotNull(deserialised);
        assertTrue(deserialised.getCondition());
        assertEquals(String.class.getName(), ((IsA) deserialised.getThen()).getType());
        assertEquals(Integer.class.getName(), ((IsA) deserialised.getOtherwise()).getType());
    }

    @Test
    public void shouldJsonSerialiseAndDeserialiseAlternative() throws IOException {
        // Given
        final If ifAltPredicate = getAltInstance();

        // When
        final String json = JsonSerialiser.serialise(ifAltPredicate);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.If\",%n" +
                "  \"predicate\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsA\",%n" +
                "    \"type\" : \"java.lang.Integer\"%n" +
                "  },%n" +
                "  \"then\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsLessThan\",%n" +
                "    \"value\" : 3,%n" +
                "    \"orEqualTo\" : false%n" +
                "  },%n" +
                "  \"otherwise\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsA\",%n" +
                "    \"type\" : \"java.lang.String\"%n" +
                "  }%n" +
                "}"), json);

        // When 2
        final If deserialised = JsonSerialiser.deserialise(json, If.class);

        // Then 2
        assertNotNull(deserialised);
        assertEquals(Integer.class.getName(), ((IsA) deserialised.getPredicate()).getType());
        assertEquals(3, ((IsLessThan) deserialised.getThen()).getControlValue());
        assertEquals(String.class.getName(), ((IsA) deserialised.getOtherwise()).getType());
    }

    @Test
    public void shouldReturnTrueWithCorrectArguments() {
        // Given
        final IsA then = new IsA(String.class);
        final IsA otherwise = new IsA(Integer.class);
        final If<Object> filter = new If<>(true, then, otherwise);

        // When
        final boolean accepted = filter.test("test value");

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldReturnFalseWithIncorrectArguments() {
        // Given
        final IsA then = new IsA(String.class);
        final IsA otherwise = new IsA(Integer.class);
        final If<Object> filter = new If<>(true, then, otherwise);

        // When
        final boolean denied = filter.test(6);

        // Then
        assertFalse(denied);
    }

    @Test
    public void shouldRejectValueWithNullFunctions() {
        // Given
        final If<Object> filter = new If<>();

        // When
        final boolean denied = filter.test("test value");

        // Then
        assertFalse(denied);
    }

    @Test
    public void shouldApplyPredicateAndPassBothConditions() {
        // Given
        final If<Comparable> filter = new If<>(new IsLessThan(3), new IsEqual(2), new IsIn(3, 4, 5));

        // When
        final boolean firstConditional = filter.test(2);

        // Then
        assertTrue(firstConditional);
    }

    @Test
    public void shouldApplyPredicateAndPassSecondCondition() {
        // Given
        final If<Comparable> filter = new If<>(new IsLessThan(3), new IsEqual(2), new IsIn(3, 4, 5));

        // When
        final boolean secondConditional = filter.test(4);

        // Then
        assertTrue(secondConditional);
    }

    @Test
    public void shouldApplyPredicateAndPassFirstButNotSecondCondition() {
        // Given
        final If<Comparable> filter = new If<>(new IsLessThan(3), new IsEqual(1), new IsIn(3, 4, 5));
        // When
        final boolean firstConditional2 = filter.test(2);

        // Then
        assertFalse(firstConditional2);
    }

    @Test
    public void shouldApplyPredicateButFailBothConditions() {
        // Given
        final If<Object> filter = new If<>(new IsA(Comparable.class), new IsA(Integer.class), new IsA(String.class));

        // When
        final boolean secondConditional2 = filter.test(new ArrayList<>());

        // Then
        assertFalse(secondConditional2);
    }

    @Test
    public void shouldReturnFalseForNullInput() {
        // Given
        final If<Comparable> filter = new If<>(new IsLessThan(3), new IsIn(0, 1, 2));

        // When
        final boolean result = filter.test(null);

        // Then
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseForNullConditionalPredicate() {
        // Given
        final If<Object> filter = new If<>(null, new IsA(String.class));

        // When
        final boolean result = filter.test("testValue");

        // Then
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseForNullThenOrOtherwise() {
        // Given
        final If<Object> filter = new If<>(new Exists(), null, null);

        // When
        final boolean result = filter.test("testValue");

        // Then
        assertFalse(result);
    }

    @Test
    public void shouldApplyPredicatesToSelectionWithIfBuilder() {
        // Given
        final If<Tuple<Integer>> filter = new If.SelectedBuilder()
                .predicate(new IsA(String.class), 0)
                .then(new StringContains("test"), 1)
                .otherwise(new IsMoreThan(3), 2)
                .build();

        // When
        final boolean result = filter.test(new ArrayTuple("prop", "test", 4));

        // Then
        assertTrue(result);
    }

    @Test
    public void shouldApplyPredicatesToMultipleSelectionsWithIfBuilder() {
        // Given
        final If<Tuple<Integer>> filter = new If.SelectedBuilder()
                .predicate(new IsA(String.class), 0)
                .then(new IsXLessThanY(), 1, 2)
                .otherwise(new IsXMoreThanY(), 1, 2)
                .build();

        // When
        final boolean result = filter.test(new ArrayTuple("prop", "test", "testValue"));

        // Then
        assertTrue(result);
    }
}
