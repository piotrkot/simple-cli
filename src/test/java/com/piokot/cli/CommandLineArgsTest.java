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
package com.piokot.cli;

import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link CommandLineArgs} class.
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
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
        Assert.assertFalse(
            "Options found",
            new CommandLineArgs().getOptions().iterator().hasNext()
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
        Assert.assertEquals(
            "Short option incorrect",
            "x",
            options.next().value()
        );
        Assert.assertEquals(
            "Short POSIX option incorrect",
            "fvx",
            options.next().value()
        );
        Assert.assertEquals(
            "Long option incorrect",
            "help",
            options.next().value()
        );
        Assert.assertEquals(
            "Short with param option incorrect",
            "3",
            options.next().value()
        );
        final Option withfile = options.next();
        Assert.assertEquals(
            "Short with arg option incorrect",
            "f",
            withfile.value()
        );
        Assert.assertEquals(
            "Short with arg argument incorrect",
            file,
            withfile.arguments().iterator().next()
        );
        Assert.assertEquals(
            "Long with param option incorrect",
            "10",
            options.next().value()
        );
    }

    /**
     * Can find POSIX option.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void findPOSIXOption() throws Exception {
        final String tar = "foo.tar.gz";
        final Option option = new CommandLineArgs("-zxvf", tar)
            .getOptions().iterator().next();
        Assert.assertEquals("Posix opt found", "zxvf", option.value());
        Assert.assertEquals(
            "Tar file not found",
            tar,
            option.arguments().iterator().next()
        );
    }

    /**
     * Can find GNU option.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void findGNUOption() throws Exception {
        final CommandLineArgs cli = new CommandLineArgs(
            "--human-readable",
            "--max-depth=1"
        );
        Assert.assertTrue(
            "Gnu opt not found",
            cli.findOption("human-readable").iterator().hasNext()
        );
        Assert.assertEquals(
            "Gnu opt value not found",
            "1",
            cli.findOption("max-depth").iterator().next().value()
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
        Assert.assertEquals(
            "Java like opt 1 key not found",
            "java.awt.headless",
            opt.key()
        );
        Assert.assertEquals(
            "Java like opt 1 value not found",
            truee,
            opt.value()
        );
        opt = iter.next();
        Assert.assertEquals(
            "Java like opt 2 key not found",
            "java.net.useSystemProxies",
            opt.key()
        );
        Assert.assertEquals(
            "Java like opt 2 value not found",
            truee,
            opt.value()
        );
    }

    /**
     * Can find short option with value.
     *
     * @throws Exception When it fails.
     */
    @Test
    public void findShortOptionWithValue() throws Exception {
        final CommandLineArgs cli = new CommandLineArgs("-O2");
        Assert.assertEquals(
            "Opt not found by short name",
            "2",
            cli.findOption("O").iterator().next().value()
        );
        Assert.assertEquals(
            "Opt not found by all",
            "O2",
            cli.getOptions().iterator().next().value()
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
        Assert.assertEquals("Key is not empty", "", opt.key());
        Assert.assertEquals("Version not found", "4.2", opt.value());
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
        Assert.assertEquals("Key dash found", "process", dashopt.key());
        Assert.assertEquals("Val dash found", "do", dashopt.value());
        Assert.assertFalse(
            "Opt equals found",
            cli.findOption("=").iterator().hasNext()
        );
        Assert.assertFalse(
            "Opt dot star found",
            cli.findOption(".*").iterator().hasNext()
        );
    }
}
