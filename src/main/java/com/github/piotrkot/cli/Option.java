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
import java.util.List;

/**
 * Command Line option found within the command line.
 * Option may have a key, a value and arguments.
 *
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
public final class Option {
    /**
     * Equals sign.
     */
    private static final String EQ = "=";
    /**
     * Main option part.
     */
    private final transient String prime;
    /**
     * Command line parameters.
     */
    private final transient Iterable<String> params;

    /**
     * Class constructor.
     *
     * @param main Main option part.
     * @param parameters Command line parameters.
     */
    public Option(final String main, final Iterable<String> parameters) {
        this.prime = main;
        this.params = parameters;
    }

    /**
     * Options arguments. These are the additional arguments for the option
     * unless they contain equals '=' sign. Example: <br>
     * When command line option is {@code -D p10x}, then
     * option name {@code D} has argument {@code p10x}.<br>
     * When command line option is {@code -D p=10x}, then
     * option name {@code D} has no arguments (it has key {@code p}
     * and value {@code 10x}, instead).
     *
     * @return List of String arguments.
     */
    public Iterable<String> arguments() {
        final List<String> args = new ArrayList<>(0);
        for (final String par : this.params) {
            if (!par.contains(EQ)) {
                args.add(par);
            }
        }
        return args;
    }

    /**
     * Option key. This is the part of option right after option name
     * and before equals '=' sign. Example: <br>
     * When command line option is {@code -Dpar=10x}, then
     * option name {@code D} has key {@code par}.
     *
     * @return Option key if exists or empty string.
     */
    public String key() {
        String key = "";
        if (this.prime.contains(EQ)) {
            key = this.prime.split(EQ)[0];
        } else {
            for (final String par : this.params) {
                if (par.contains(EQ)) {
                    key = this.prime + par.split(EQ)[0];
                    break;
                }
            }
        }
        return key;
    }

    /**
     * Option value. This is the part of option right after option name
     * if there is no equals '=' sign, or after equals '=' sign if it exists.
     * Example: <br>
     * When command line option is {@code -Dpar=10x}, then
     * option name {@code D} has value {@code 10x}.<br>
     * When command line option is {@code -Dpar10x}, then
     * option name {@code D} has value {@code par10x}.
     *
     * @return Option value if exists or empty string.
     */
    public String value() {
        String val = "";
        if (this.prime.contains(EQ)) {
            if (this.prime.split(EQ).length == 2) {
                val = this.prime.split(EQ)[1];
            }
        } else {
            val = this.prime;
            for (final String par : this.params) {
                if (par.contains(EQ)) {
                    //@checkstyle NestedIfDepthCheck (1 line)
                    if (par.split(EQ).length == 2) {
                        val = par.split(EQ)[1];
                    }
                    break;
                }
            }
        }
        return val;
    }
}
