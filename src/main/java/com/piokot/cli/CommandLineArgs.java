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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private final transient Iterable<String> args;

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
        final String space = " ";
        final String[] opts = space.concat(String.join(space, this.args))
            .split(" -+");
        final String[] optss = Arrays.copyOfRange(opts, 1, opts.length);
        for (final String opt : optss) {
            final String[] parts = opt.split(space);
            final String main = parts[0].replaceFirst("^-+", "");
            if (main.startsWith(name)) {
                options.add(
                    new Option(
                        main.replace(name, ""),
                        Arrays.stream(parts).skip(1)
                            .collect(Collectors.<String>toList())
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
}
