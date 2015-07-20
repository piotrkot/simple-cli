# Inspiration

[Object thinking by David West](http://www.amazon.com/Object-Thinking-Developer-Reference-David/dp/0735619654) is an amazing read.
Author convinces that proper object-oriented design, fundamentally different
from structured design, can leverage communication among all team members 
(with users included), contribute to vast product simplicity, produce
better developers.

While soft advantages are hard to question and prove, West mentions published
empirical metrics of Mark Lorenz and Jeff Kidd<sup>[1](#MarkKidd)</sup> which
he interprets as
> Lines of code, for example, in a well-thought-out object application will be
at least an order of magnitude fewer (sometimes two orders of magnitude).

Well, that's impressive. Let's look closer.

# Experiment

We will try to re-think Command Line Interface applications (CLI).
There is a number of Java tools for recognizing command line options passed
to programs:

Program | Version | Size
--------|---------|-----
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

Some existing solutions are already a magnitude smaller in lines of code from
others. Can we get any better and still be object-oriented?

When making contrasting approach one should not look at the current
implementations. Thus, let think over the basic principles and responsibilities
of the CLI objects.

We must be able to **find options given list of arguments**. After that
an **option must provide us with its _values_**. With little experience on Unix
systems we know that option might be composed of a key with value
(i.e. `du --max-depth=1`), a value (i.e. `df -H`), or even an
argument (i.e. `cut -f 1,3`). Additionally, there can be short or long options,
POSIX or GNU parser, and alike variations. But it all should not matter as
the Option object should be concerned about the details.

One may notice it is not that simple. Given argument of `-Dparam2` we don't
know whether it is:
* option `-D` with `param2` value or
* option `-D` with `param` key and `2` value or
* option value `-Dparam2` or something else.

Indeed, there is ambiguity of interpretation. However, this is irrelevant as
they are different views of the same argument. And whichever form you choose
the result is positive.

One may ask about the common generating help information. Right, it is not
supported.
Reacting on the options is the sole purpose of the application, so why reacting
on the error should be the responsibility of the command line parser? And is
this help flexible enough to print into different system streams, with special
formatting? And if so, is it really the right place to put logic into?

# How to use

The simple design makes it 2 files with 110 loc. How about the usage?

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
them all by:
```java
Option option = new CommandLineArgs("-zxvf", "foo.tar.gz")
    .getOptions().iterator().next();
option.value(); // returns "zxvf"
option.arguments().iterator().next(); // returns "foo.tar.gz"
```

Feel free to fork me on GitHub, report bugs or post comments.

For Pull Requests, please run `mvn clean package -Pqulice`, first.

<a name="MarkKidd">1</a>: Lorenz, Mark, and Jeff Kidd. Object-Oriented
Software Metrics. Englewood Cliffs, NJ: Prentice Hall. 1994. ISBN 0-13-179292-X
