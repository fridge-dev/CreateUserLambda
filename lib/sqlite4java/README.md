Binaries downloaded from https://bitbucket.org/almworks/sqlite4java. The downloaded zip is in this directory as well.

Original guide followed from https://stackoverflow.com/a/35353377

Also, adding this to any UT and stepping through debugger can be helpful:

```
System.setProperty("sqlite4java.debug", "true");
com.almworks.sqlite4java.SQLite.loadLibrary();
```

