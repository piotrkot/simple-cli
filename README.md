[![Build Status](https://travis-ci.org/piotrkot/simple-cli.svg?branch=master)](https://travis-ci.org/piotrkot/simple-cli)

# Inspiration

[Object thinking by David West](http://www.amazon.com/Object-Thinking-Developer-Reference-David/dp/0735619654) is an amazing read.
Author convinces that proper object-oriented design, fundamentally different
from structured design, can leverage communication among all team members
(with users included), contribute to vast product simplicity, and even produce
better developers.

While soft advantages are hard to question and prove, West also mentions
published empirical metrics of Mark Lorenz and
Jeff Kidd<sup>[1](#MarkKidd)</sup> which he interprets as
> Lines of code, for example, in a well-thought-out object application will be
at least an order of magnitude fewer (sometimes two orders of magnitude).

Well, that's impressive. Let's check that.

# Experiment

For the purpose of verifying the claim, we will try to come up with a new
idea for Command Line Interface (CLI) in Java language.

There is already a number of Java tools for recognizing command line options
passed to programs. And they all vary in size, complexity, and, sadly,
specification.

Program | Version | Size<sup>[2](#LoC)</sup>
--------|---------|-------------------------
https://commons.apache.org/proper/commons-cli/ | (version 1.4-SNAPSHOT) | 23 files (2665 loc)
http://args4j.kohsuke.org/ | (latest commit 00c192c445) | 63 files (2379 loc)
http://jewelcli.lexicalscope.com/usage.html | (latest commit d282f87b93) | 90 files (2825 loc)
http://www.cs.ubc.ca/~lloyd/java/argparser.html | n/a | 14 files (3348 loc)
http://www.urbanophile.com/arenn/hacking/download.html | (latest commit 97d3ac8d79) | 3 files (634 loc)
http://www.martiansoftware.com/jsap/ | (version 2.1) | 94 files (4888 loc)
http://pholser.github.io/jopt-simple/ |(version 4.9) | 45 files (2053 loc)
http://ostermiller.org/utils/CmdLn.html | (version 1.08.02) | 6 files (730 loc)
https://code.google.com/p/parse-cmd/ | (version 0.0.93) | 1 file (238 loc)
http://jcommander.org/ | (latest commit b02e9dee4e) | 49 files (2226 loc)

Looking closer into their documentation and test cases reveals there are
many parsers recognizing different options, among which are POSIX, GNU,
long options, short options, multivalued options and group options. As the
general purpose is to outperform others we must support all the possible ones.

As perhaps it is safe to assume the most popular tool is the Apache Commons CLI,
this will be the benchmark for supported options and test cases. This will not
however be the benchmark for the source code complexity, the smallest from
the non Object-oriented sources will be.

You can notice that some existing solutions are already a magnitude smaller in
lines of code from others. Can we get any better and still be object-oriented?

When making contrasting approach one should not look at the current
implementations. Thus, let think over the basic principles and responsibilities
of the CLI objects.

First, we must be able to **find options given list of arguments**. And we don't
care what kind of options, just options objects. After that
an **option must provide us with its _values_**. From experience on Unix
systems we know that option might be composed of a key with value
(i.e. `du --max-depth=1`), a value (i.e. `df -H`), or even an
argument (i.e. `cut -f 1,3`). Apache Commons CLI discloses POSIX options
(i.e. `tar -cf archive.tar foo bar`), GNU options (i.e. `du --human-readable`),
short options with value (i.e. `gcc -O2 foo.c`), long options with single hyphen
(i.e. `ant -projecthelp`), and alike variations. But it all should not matter as
the Option object should be concerned about the details.

One may notice it is all not that simple. Given argument of `-Dparam2` we don't
know whether it is:
* option `-D` with `param2` value or
* option `-D` with `param` key and `2` value or
* option value `-Dparam2`.

Indeed, there is ambiguity of interpretation. However, this is irrelevant as
they are different views of the same argument. And whichever form you choose
the result is positive.

Often CLI tools provide a bonus feature - generating help information. Help
information is the message displayed when user asks for help explicitly or
when given arguments are not expected for the application.
Right. How does it fit into the objects model?

Created CLI object with given command line arguments has the responsibility to
answer when asked for an option. It may return an option if it finds one,
perhaps many options if my request is too general or none if not satisfied.
However, reacting on the options found is the sole purpose of the application,
so why reacting on the error should be the responsibility of the command
line parser? And is this help flexible enough to print into different
system streams, with special formatting? And if so, is it really the right
place to put logic into?

In my opinion, it is not.

# Result

With simple, yet object-oriented design, there are **2 files with total of 113
loc (which is the smallest code base size!)** with no additional dependency.
The code follows strict quality rules
of [qulice](http://www.qulice.com/) with full test coverage
of [mutation tests](http://pitest.org/).

Application supports various options like:
* POSIX like options (i.e. `tar -zxvf foo.tar.gz`)
* GNU like long options (i.e. `du --human-readable --max-depth=1`)
* Java like properties (i.e. `java -Djava.awt.headless=true -Djava.net.useSystemProxies=true Foo`)
* Short options with value attached (i.e. `gcc -O2 foo.c`)
* Long options with single hyphen (i.e. `ant -projecthelp`)
* Multivalued options (i.e. `-file input1.txt input2.txt`)
* Properties with long format (i.e. `--property foo=bar`)
* Group options (i.e. `-Xmx2048m -Xms256m -Xdebug`) even all [JVM parameters](http://javarevisited.blogspot.com/2011/11/hotspot-jvm-options-java-examples.html)

How about the simplicity of use? Here are some examples.

```java
CommandLineArgs cli = new CommandLineArgs("--human-readable");
cli.findOption("human-readable").iterator().hasNext(); // returns true
```
```java
CommandLineArgs cli = new CommandLineArgs("--max-depth=1");
cli.findOption("max-depth").iterator().next().value(); // returns "1"
```
For ambiguous options we have an interpretation choice.
```java
CommandLineArgs cli = new CommandLineArgs("-Xmx2048m", "-Xms256m");
Iterator<Option> iter = cli.findOption("X").iterator();
iter.next().value(); // returns "mx2048m"
iter.next().value(); // returns "ms256m"
```
or
```java
CommandLineArgs cli = new CommandLineArgs("-Xmx2048m", "-Xms256m");
cli.findOption("Xmx").iterator().next().value(); // returns "2048m"
cli.findOption("Xms").iterator().next().value(); // returns "256m"
```
depending what is more preferable for the user.

In case we don't exactly know what option name to search for we may find
them all by `getOptions` with:
```java
Option option = new CommandLineArgs("-zxvf", "foo.tar.gz")
    .getOptions().iterator().next();
option.value(); // returns "zxvf"
option.arguments().iterator().next(); // returns "foo.tar.gz"
```

To get started, add dependency to your project:
```xml
        <dependency>
            <groupId>com.github.piotrkot</groupId>
            <artifactId>cli</artifactId>
            <version>1.1.0</version>
        </dependency>
```

Feel free to fork me on GitHub, report bugs or post comments.

For Pull Requests, please run `mvn clean package -Pqulice`, first.

<a name="MarkKidd"><sup>1</sup></a> Lorenz, Mark, and Jeff Kidd. Object-Oriented
Software Metrics. Englewood Cliffs, NJ: Prentice Hall. 1994. ISBN 0-13-179292-X

<a name="LoC"><sup>2</sup></a> Lines of code are calculated with
[cloc](http://cloc.sourceforge.net).
