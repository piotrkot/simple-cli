/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 piotrkot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.piotrkot.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Main Command Line class which finds options in simple and object oriented
 * fashion.
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class CommandLineArgs {
    /**
     * Command line arguments.
     */
    private final transient Collection<String> args;

    /**
     * Class constructor.
     *
     * @param arguments Command line arguments.
     */
    public CommandLineArgs(final String... arguments) {
        this.args = Arrays.asList(arguments);
    }

    /**
     * Finds option by a name.
     *
     * @param name Option name, that is a string right after the option dash.
     * @return List of Options found for given name.
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public Iterable<Option> findOption(final String name) {
        final List<Option> options = new ArrayList<>(0);
        final String main = String.format("^-+%s.*", Pattern.quote(name));
        final String dash = String.format("^-+%s", Pattern.quote(name));
        final FetchSubList<String> params = new FetchSubList<>(this.args);
        for (final String param : params) {
            if (param.matches(main)) {
                options.add(
                    new Option(
                        param.replaceFirst(dash, ""),
                        params.fetchUntil(param, s -> s.matches(main))
                    )
                );
            }
        }
        return options;
    }

    /**
     * Gets all options provided.
     *
     * @return List of Options found.
     */
    public Iterable<Option> getOptions() {
        return this.findOption("");
    }

    /**
     * Linked list that can fetch elements according to given predicate.
     *
     * @param <E> Type of elements in the list.
     */
    class FetchSubList<E> extends LinkedList<E> {
        /**
         * Class constructor.
         *
         * @param coll Collection of elements to be placed in the list.
         */
        public FetchSubList(final Collection<? extends E> coll) {
            super(coll);
        }

        /**
         * Fetches elements from the list starting at given element. Elements
         * are fetched until the predicate is satisfied.
         *
         * @param elem Starting element.
         * @param pred Predicate for fetch.
         * @return Collection of elements satisfying predicate.
         */
        public Collection<E> fetchUntil(final E elem, final Predicate<E> pred) {
            final Collection<E> collection = new ArrayList<>(0);
            final ListIterator<E> iter =
                this.listIterator(this.indexOf(elem) + 1);
            while (iter.hasNext()) {
                final E next = iter.next();
                if (pred.test(next)) {
                    break;
                } else {
                    collection.add(next);
                }
            }
            return collection;
        }
    }
}
