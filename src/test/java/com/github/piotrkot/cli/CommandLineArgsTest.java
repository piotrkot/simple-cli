/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2021 piotrkot
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

import java.util.Iterator;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Tests for {@link CommandLineArgs} class.
 *
 * @since 1.0
 */
public final class CommandLineArgsTest {
    /**
     * Can get options when there are none.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void getOptionsWhenNone() throws Exception {
        MatcherAssert.assertThat(
            new CommandLineArgs().getOptions(), Matchers.empty()
        );
    }

    /**
     * Can get all options.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void getAllOptions() throws Exception {
        final String file = "file.txt";
        final Iterator<Option> options = new CommandLineArgs(
            "-x",
            "-fvx",
            "--help",
            "--max-depth=3",
            "-f",
            file,
            "-D",
            "par=10"
        ).getOptions().iterator();
        MatcherAssert.assertThat(options.next().value(), Matchers.is("x"));
        MatcherAssert.assertThat(options.next().value(), Matchers.is("fvx"));
        MatcherAssert.assertThat(options.next().value(), Matchers.is("help"));
        MatcherAssert.assertThat(options.next().value(), Matchers.is("3"));
        final Option withfile = options.next();
        MatcherAssert.assertThat(withfile.value(), Matchers.is("f"));
        MatcherAssert.assertThat(
            withfile.arguments().iterator().next(), Matchers.is(file)
        );
        MatcherAssert.assertThat(options.next().value(), Matchers.is("10"));
    }

    /**
     * Can find POSIX option.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void findPosixOption() throws Exception {
        final String tar = "foo.tar.gz";
        final Option option = new CommandLineArgs("-zxvf", tar)
            .getOptions().iterator().next();
        MatcherAssert.assertThat(option.value(), Matchers.is("zxvf"));
        MatcherAssert.assertThat(
            option.arguments().iterator().next(), Matchers.is(tar)
        );
    }

    /**
     * Can find GNU option.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void findGnuOption() throws Exception {
        final CommandLineArgs cli = new CommandLineArgs(
            "--human-readable",
            "--max-depth=1"
        );
        MatcherAssert.assertThat(
            cli.findOption("human-readable").iterator().hasNext(),
            Matchers.is(true)
        );
        MatcherAssert.assertThat(
            cli.findOption("max-depth").iterator().next().value(),
            Matchers.is("1")
        );
    }

    /**
     * Can find Java-like option.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void findJavaLikeOption() throws Exception {
        final String truee = "true";
        final CommandLineArgs cli = new CommandLineArgs(
            "-Djava.awt.headless=true",
            "-Djava.net.useSystemProxies=true"
        );
        final Iterator<Option> iter = cli.findOption("D").iterator();
        Option opt = iter.next();
        MatcherAssert.assertThat(opt.key(), Matchers.is("java.awt.headless"));
        MatcherAssert.assertThat(opt.value(), Matchers.is(truee));
        opt = iter.next();
        MatcherAssert.assertThat(
            opt.key(), Matchers.is("java.net.useSystemProxies")
        );
        MatcherAssert.assertThat(opt.value(), Matchers.is(truee));
    }

    /**
     * Can find short option with value.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void findShortOptionWithValue() throws Exception {
        final CommandLineArgs cli = new CommandLineArgs("-O2");
        MatcherAssert.assertThat(
            cli.findOption("O").iterator().next().value(), Matchers.is("2")
        );
        MatcherAssert.assertThat(
            cli.getOptions().iterator().next().value(), Matchers.is("O2")
        );
    }

    /**
     * Can find partial options.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void findPartialOption() throws Exception {
        final CommandLineArgs cli = new CommandLineArgs("-ver=4.2");
        final Option opt = cli.findOption("ver").iterator().next();
        MatcherAssert.assertThat(opt.key(), Matchers.is(""));
        MatcherAssert.assertThat(opt.value(), Matchers.is("4.2"));
    }

    /**
     * Can find exceptional options.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void findExceptionOption() throws Exception {
        final CommandLineArgs cli = new CommandLineArgs("--process=do");
        final Option dashopt = cli.findOption("-").iterator().next();
        MatcherAssert.assertThat(dashopt.key(), Matchers.is("process"));
        MatcherAssert.assertThat(dashopt.value(), Matchers.is("do"));
        MatcherAssert.assertThat(
            cli.findOption("=").iterator().hasNext(), Matchers.is(false)
        );
        MatcherAssert.assertThat(
            cli.findOption(".*").iterator().hasNext(), Matchers.is(false)
        );
    }

    /**
     * Can find first option.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void findFirstOption() throws Exception {
        final CommandLineArgs cli = new CommandLineArgs(
            "--dupl=1", "--duplA=2"
        );
        final Option dupl = cli.findFirstOption("dupl");
        MatcherAssert.assertThat(dupl.value(), Matchers.is("1"));
    }

    /**
     * Can not find first option.
     *
     * @throws Exception When it fails.
     */
    @Test(expected = HelpException.class)
    public void findFirstNoneOption() throws Exception {
        final CommandLineArgs cli = new CommandLineArgs(
            "--du=1", "--duA=2"
        );
        cli.findFirstOption("duplic");
    }
}
