# server-reader-writer

Simple Reader, Writer, Server app
=======================

To use this application you will need to install
* zeromq
* git://github.com/zeromq/jzmq.git
* redis

In order to use the app..you will need to start the server application first
and then you can start and stop the reader/writer in any order you want.

# Compilation

* sbt 
* project reader
* stage
* project server
* stage
* project writer
* stage

# Running

### server
* cd server
* ./target/universal/stage/bin/server


### reader
* cd reader
* ./target/universal/stage/bin/reader

### writer
* cd writer
* ./target/universal/stage/bin/writer
