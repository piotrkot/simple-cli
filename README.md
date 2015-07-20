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
others. Can we get any better?

<a name="MarkKidd">1</a>: Lorenz, Mark, and Jeff Kidd. Object-Oriented
Software Metrics. Englewood Cliffs, NJ: Prentice Hall. 1994. ISBN 0-13-179292-X
