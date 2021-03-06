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

package uk.gov.gchq.koryphe.example.binaryoperator;

import uk.gov.gchq.koryphe.example.KorypheBinaryOperatorExample;
import uk.gov.gchq.koryphe.example.annotation.Example;
import uk.gov.gchq.koryphe.impl.binaryoperator.Min;
import uk.gov.gchq.koryphe.impl.binaryoperator.Product;
import uk.gov.gchq.koryphe.impl.binaryoperator.Sum;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.tuple.binaryoperator.TupleAdaptedBinaryOperatorComposite;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

@Example(name = "Complex object composite binary operator",
         description = "Applies a composite of adapted binary operators to multiple fields in a stream of input tuples, " +
         "producing a single aggregate output tuple containing the results of the operators.")
public class TupleCompositeAggregationExample extends KorypheBinaryOperatorExample<Tuple<String>> {
    @Override
    public Stream<Tuple<String>> getInput() {
        List<Tuple<String>> inputTuples = Arrays.asList(
                createMapTuple(1, 2, 3),
                createMapTuple(4, 5, 6),
                createMapTuple(7, 8, 9));
        return inputTuples.stream();
    }

    @Override
    public BinaryOperator<Tuple<String>> getBinaryOperator() {
        return new TupleAdaptedBinaryOperatorComposite.Builder<String>()
                .select(new String[]{"A"}).execute(new Product())
                .select(new String[]{"B"}).execute(new Sum())
                .select(new String[]{"C"}).execute(new Min())
                .build();
    }
}
