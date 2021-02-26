# Import
This repository contains the software used to tackle the database import for the Fontys Mario Pizza case. Below you
will find the appropriate documentation on how to get started, how to compile the software, run unit & integration 
tests, etc.

## Compile & build Docker image:
To simply compile the software;
```bash
make
```

## Run unit tests:
To run the unit tests (so, excluding integration tests), run;
```bash
make test
```

## Run an import:
To run a full import, you can use the below command. There is currently no support for delta imports, so you must
manually truncate or delete the database after each run.
```bash
make run
```

## Teardown
Once you're finished running the import & the applicable databases, use the below command to shut everything down;
```bash
make stop
```