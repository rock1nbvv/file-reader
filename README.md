### Common

* To limit memory heap: `-Xmx30m`
* To store garbage collector logs: `-Xlog:gc:file=log.gc`
* To expose jconsole port:

```
-Dcom.sun.management.jmxremote=true
-Dcom.sun.management.jmxremote.port=1199
-Dcom.sun.management.jmxremote.local.only=false
-Dcom.sun.management.jmxremote.authenticate=false
-Dcom.sun.management.jmxremote.ssl=false
```

### Problem

When we need to parse non-primitive to read file containing non-primitive types Scanner is not a solution and at the
same time we don't want to keep the whole parsed file in memory.

### Goal

* Find the best solutions for reading files in terms of memory usage, speed and
  compliance with modern java libraries(E.G. avoid using _java.io_) package).
* Provide a comparison of implemented approaches

### Prep

* Using `CreateFile` generate .csv with sequence of numbers the size of 131810190 bytes
* `scannerMethod` used as benchmark requires `-Xmx2050k` to work


### References
* [Java Object Size: Estimating, Measuring, and Verifying via Profiling](https://dzone.com/articles/java-object-size-estimation-measuring-verifying)