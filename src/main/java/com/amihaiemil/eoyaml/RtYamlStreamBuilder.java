/**
 * Copyright (c) 2016-2020, Mihai Emil Andronache
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.amihaiemil.eoyaml;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * YamlStreamBuilder implementation.
 * This class is immutable and thread-safe.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 3.1.1
 */
final class RtYamlStreamBuilder implements YamlStreamBuilder {

    /**
     * Added nodes.
     */
    private final List<YamlNode> documents;

    /**
     * Default ctor.
     */
    RtYamlStreamBuilder() {
        this(new LinkedList<YamlNode>());
    }

    /**
     * Constructor.
     * @param documents YAML documents used in building the YamlStream.
     */
    RtYamlStreamBuilder(final List<YamlNode> documents) {
        this.documents = documents;
    }

    @Override
    public YamlStreamBuilder add(final YamlNode document) {
        final List<YamlNode> list = new LinkedList<>();
        list.addAll(this.documents);
        list.add(document);
        return new RtYamlStreamBuilder(list);
    }

    @Override
    public YamlStream build() {
        return new BuiltYamlStream(this.documents);
    }
    
    /**
     * Built YamlStream.
     * @checkstyle FinalParameters (200 lines)
     * @checkstyle JavadocMethod (200 lines)
     * @checkstyle LineLength (200 lines)
     * @checkstyle ParameterName (200 lines)
     * @checkstyle FinalParameters (200 lines)
     * @author Mihai Andronache (amihaiemil@gmail.com)
     * @version $Id$
     * @since 3.1.1
     */
    static class BuiltYamlStream extends ComparableYamlStream {

        /**
         * Documents as a List.
         */
        private Collection<YamlNode> documents;
        
        /**
         * The Stream of YAML Documents.
         */
        private Stream<YamlNode> documentStream;
        
        BuiltYamlStream(final Collection<YamlNode> documents) {
            this.documents = documents;
            this.documentStream = documents.stream();
        }
        
        public Iterator<YamlNode> iterator() {
            return this.documentStream.iterator();
        }

        public Spliterator<YamlNode> spliterator() {
            return this.documentStream.spliterator();
        }

        public boolean isParallel() {
            return this.documentStream.isParallel();
        }

        public Stream<YamlNode> sequential() {
            return this.documentStream.sequential();
        }

        public Stream<YamlNode> parallel() {
            return this.documentStream.parallel();
        }

        public Stream<YamlNode> unordered() {
            return this.documentStream.unordered();
        }

        public Stream<YamlNode> onClose(Runnable closeHandler) {
            return this.documentStream.onClose(closeHandler);
        }

        public void close() {
            this.documentStream.close();
        }

        public Stream<YamlNode> filter(Predicate<? super YamlNode> predicate) {
            return this.documentStream.filter(predicate);
        }

        public <R> Stream<R> map(Function<? super YamlNode, ? extends R> mapper) {
            return this.documentStream.map(mapper);
        }

        public IntStream mapToInt(ToIntFunction<? super YamlNode> mapper) {
            return this.documentStream.mapToInt(mapper);
        }

        public LongStream mapToLong(ToLongFunction<? super YamlNode> mapper) {
            return this.documentStream.mapToLong(mapper);
        }

        public DoubleStream mapToDouble(ToDoubleFunction<? super YamlNode> mapper) {
            return this.documentStream.mapToDouble(mapper);
        }

        public <R> Stream<R> flatMap(Function<? super YamlNode, ? extends Stream<? extends R>> mapper) {
            return this.documentStream.flatMap(mapper);
        }

        public IntStream flatMapToInt(Function<? super YamlNode, ? extends IntStream> mapper) {
            return this.documentStream.flatMapToInt(mapper);
        }

        public LongStream flatMapToLong(Function<? super YamlNode, ? extends LongStream> mapper) {
            return this.documentStream.flatMapToLong(mapper);
        }

        public DoubleStream flatMapToDouble(Function<? super YamlNode, ? extends DoubleStream> mapper) {
            return this.documentStream.flatMapToDouble(mapper);
        }

        public Stream<YamlNode> distinct() {
            return this.documentStream.distinct();
        }

        public Stream<YamlNode> sorted() {
            return this.documentStream.sorted();
        }

        public Stream<YamlNode> sorted(Comparator<? super YamlNode> comparator) {
            return this.documentStream.sorted(comparator);
        }

        public Stream<YamlNode> peek(Consumer<? super YamlNode> action) {
            return this.documentStream.peek(action);
        }

        public Stream<YamlNode> limit(long maxSize) {
            return this.documentStream.limit(maxSize);
        }

        public Stream<YamlNode> skip(long n) {
            return this.documentStream.skip(n);
        }

        public void forEach(Consumer<? super YamlNode> action) {
            this.documentStream.forEach(action);
        }

        public void forEachOrdered(Consumer<? super YamlNode> action) {
            this.documentStream.forEachOrdered(action);
        }

        public Object[] toArray() {
            return this.documentStream.toArray();
        }

        public <A> A[] toArray(IntFunction<A[]> generator) {
            return this.documentStream.toArray(generator);
        }

        public YamlNode reduce(YamlNode identity, BinaryOperator<YamlNode> accumulator) {
            return this.documentStream.reduce(identity, accumulator);
        }

        public Optional<YamlNode> reduce(BinaryOperator<YamlNode> accumulator) {
            return this.documentStream.reduce(accumulator);
        }

        public <U> U reduce(U identity, BiFunction<U, ? super YamlNode, U> accumulator, BinaryOperator<U> combiner) {
            return this.documentStream.reduce(identity, accumulator, combiner);
        }

        public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super YamlNode> accumulator,
                BiConsumer<R, R> combiner) {
            return this.documentStream.collect(supplier, accumulator, combiner);
        }

        public <R, A> R collect(Collector<? super YamlNode, A, R> collector) {
            return this.documentStream.collect(collector);
        }

        public Optional<YamlNode> min(Comparator<? super YamlNode> comparator) {
            return this.documentStream.min(comparator);
        }

        public Optional<YamlNode> max(Comparator<? super YamlNode> comparator) {
            return this.documentStream.max(comparator);
        }

        public long count() {
            return this.documentStream.count();
        }

        public boolean anyMatch(Predicate<? super YamlNode> predicate) {
            return this.documentStream.anyMatch(predicate);
        }

        public boolean allMatch(Predicate<? super YamlNode> predicate) {
            return this.documentStream.allMatch(predicate);
        }

        public boolean noneMatch(Predicate<? super YamlNode> predicate) {
            return this.documentStream.noneMatch(predicate);
        }

        public Optional<YamlNode> findFirst() {
            return this.documentStream.findFirst();
        }

        public Optional<YamlNode> findAny() {
            return this.documentStream.findAny();
        }

        @Override
        public Collection<YamlNode> values() {
            return this.documents;
        }

        @Override
        public String toString() {
            return this.indent(0);
        }
    }
    
}
