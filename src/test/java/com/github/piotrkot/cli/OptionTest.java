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

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import org.junit.Assert;
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
        Assert.assertFalse(
            "Arguments exist",
            new Option("empty", Collections.<String>emptyList()).arguments()
                .iterator().hasNext()
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
            "a",
            Arrays.asList(file, "par=10")
        ).arguments().iterator();
        Assert.assertEquals(
            "Argument file correct",
            file,
            args.next()
        );
        Assert.assertFalse("Param after is an argument", args.hasNext());
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
        Assert.assertEquals(
            "Argument newfile correct",
            file,
            args.next()
        );
        Assert.assertFalse("Param before is an argument", args.hasNext());
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
            "two",
            Arrays.asList(fone, ftwo)
        ).arguments().iterator();
        Assert.assertEquals(
            "Argument first correct",
            fone,
            args.next()
        );
        Assert.assertEquals(
            "Argument second correct",
            ftwo,
            args.next()
        );
        Assert.assertFalse("More arguments", args.hasNext());
    }

    /**
     * Can return empty value.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void returnEmptyValue() throws Exception {
        Assert.assertTrue(
            "Non empty value",
            new Option("", Collections.<String>emptyList()).value().isEmpty()
        );
        Assert.assertTrue(
            "Non empty value with arg",
            new Option("", Collections.singletonList("arg")).value().isEmpty()
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
        Assert.assertEquals(
            "Wrong value",
            val,
            new Option(val, Collections.<String>emptyList()).value()
        );
        Assert.assertEquals(
            "Wrong value with arg",
            val,
            new Option(val, Collections.singletonList("ar")).value()
        );
    }

    /**
     * Can return exceptional value.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void returnExceptionalValue() throws Exception {
        Assert.assertEquals(
            "Non empty exception value",
            "",
            new Option("par1=", Collections.<String>emptyList()).value()
        );
        Assert.assertEquals(
            "Wrong value with no key",
            "val1",
            new Option("=val1", Collections.<String>emptyList()).value()
        );
    }

    /**
     * Can return empty key.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void returnEmptyKey() throws Exception {
        Assert.assertTrue(
            "Non empty key",
            new Option("", Collections.<String>emptyList()).key().isEmpty()
        );
        Assert.assertTrue(
            "Non empty key with arg",
            new Option("", Collections.singletonList("key")).key().isEmpty()
        );
        Assert.assertTrue(
            "Non empty key with value",
            new Option("kval", Collections.singletonList("kk")).key().isEmpty()
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
        Assert.assertEquals(
            "Empty key",
            "Kk",
            new Option("K", Collections.singletonList("k=val")).key()
        );
        Assert.assertEquals(
            "Empty key with arg",
            key,
            new Option("", Arrays.asList("k=vval", "file")).key()
        );
        Assert.assertEquals(
            "Empty key with arg reordered",
            key,
            new Option("", Arrays.asList("file.x", "k=xx")).key()
        );
        Assert.assertEquals(
            "Empty key in main part",
            key,
            new Option("k=xad", Collections.<String>emptyList()).key()
        );
    }

    /**
     * Can return exceptional key.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void returnExceptionalKey() throws Exception {
        Assert.assertEquals(
            "Non empty exception key",
            "",
            new Option("=val", Collections.<String>emptyList()).key()
        );
        Assert.assertEquals(
            "Wrong key with no value",
            "key1",
            new Option("key1=", Collections.<String>emptyList()).key()
        );
    }
}
