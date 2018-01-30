/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2018 piotrkot
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

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Tests for {@link Option} class.
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class OptionTest {
    /**
     * Can return empty arguments when there are none.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void returnEmptyArguments() throws Exception {
        MatcherAssert.assertThat(
            new Option("empty", Collections.emptyList()).arguments()
                .iterator().hasNext(),
            Matchers.is(false)
        );
    }

    /**
     * Can return one argument with parameter defined afterwards.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void returnOneArgumentWithParamAfter() throws Exception {
        final String file = "filee";
        final Iterator<String> args = new Option(
            "a", Arrays.asList(file, "par=10")
        ).arguments().iterator();
        MatcherAssert.assertThat(args.next(), Matchers.is(file));
        MatcherAssert.assertThat(args.hasNext(), Matchers.is(false));
    }

    /**
     * Can return one argument with parameter defined before.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void returnOneArgumentWithParamBefore() throws Exception {
        final String file = "newfile";
        final Iterator<String> args = new Option(
            "b",
            Arrays.asList("pp=xx", file)
        ).arguments().iterator();
        MatcherAssert.assertThat(args.next(), Matchers.is(file));
        MatcherAssert.assertThat(args.hasNext(), Matchers.is(false));
    }

    /**
     * Can return two arguments.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void returnTwoArguments() throws Exception {
        final String fone = "newfile1";
        final String ftwo = "newfile2";
        final Iterator<String> args = new Option(
            "two", Arrays.asList(fone, ftwo)
        ).arguments().iterator();
        MatcherAssert.assertThat(args.next(), Matchers.is(fone));
        MatcherAssert.assertThat(args.next(), Matchers.is(ftwo));
        MatcherAssert.assertThat(args.hasNext(), Matchers.is(false));
    }

    /**
     * Can return empty value.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void returnEmptyValue() throws Exception {
        MatcherAssert.assertThat(
            new Option("", Collections.emptyList()).value(),
            Matchers.is("")
        );
        MatcherAssert.assertThat(
            new Option("", Collections.singletonList("arg")).value(),
            Matchers.is("")
        );
    }

    /**
     * Can return single value.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void returnSingleValue() throws Exception {
        final String val = "value";
        MatcherAssert.assertThat(
            new Option(val, Collections.emptyList()).value(),
            Matchers.is(val)
        );
        MatcherAssert.assertThat(
            new Option(val, Collections.singletonList("ar")).value(),
            Matchers.is(val)
        );
    }

    /**
     * Can return exceptional value.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void returnExceptionalValue() throws Exception {
        MatcherAssert.assertThat(
            new Option("par1=", Collections.emptyList()).value(),
            Matchers.is("")
        );
        MatcherAssert.assertThat(
            new Option("=val1", Collections.emptyList()).value(),
            Matchers.is("val1")
        );
    }

    /**
     * Can return empty key.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void returnEmptyKey() throws Exception {
        MatcherAssert.assertThat(
            new Option("", Collections.emptyList()).key(),
            Matchers.is("")
        );
        MatcherAssert.assertThat(
            new Option("", Collections.singletonList("key")).key(),
            Matchers.is("")
        );
        MatcherAssert.assertThat(
            new Option("kval", Collections.singletonList("kk")).key(),
            Matchers.is("")
        );
    }

    /**
     * Can return non empty key.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void returnKey() throws Exception {
        final String key = "k";
        MatcherAssert.assertThat(
            new Option("K", Collections.singletonList("k=val")).key(),
            Matchers.is("Kk")
        );
        MatcherAssert.assertThat(
            new Option("", Arrays.asList("k=vval", "file")).key(),
            Matchers.is(key)
        );
        MatcherAssert.assertThat(
            new Option("", Arrays.asList("file.x", "k=xx")).key(),
            Matchers.is(key)
        );
        MatcherAssert.assertThat(
            new Option("k=xad", Collections.emptyList()).key(),
            Matchers.is(key)
        );
    }

    /**
     * Can return exceptional key.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void returnExceptionalKey() throws Exception {
        MatcherAssert.assertThat(
            new Option("=val", Collections.emptyList()).key(),
            Matchers.is("")
        );
        MatcherAssert.assertThat(
            new Option("key1=", Collections.emptyList()).key(),
            Matchers.is("key1")
        );
    }
}
