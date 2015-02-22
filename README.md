# Tiger Compiler

This is a repo for a compiler implementation for a small language called Tiger.

### Setup

Create a .env file and add your LSU login ID and LSU serve name.
This setup script will download the needed JLex and JavaCup class files.
```bash
./setup
source .env
source .aliases
prog<1-2> 
```
Your working directory should be ready to build the project.

### Running

To run the lexer by itself:
```bash
prog1
cd src
make
```
This will build the JLex lexer that can then be tested.

To run the parser:
```bash
prog2
cd src
make
```
This will build the Java CUP parser that should then be tested.  
**_Note:_** _Using `make clean` will remove old lexer and parser class files._

### Testing

##### Prog1
To test your lexer against the one provided from class:
```bash
cd src
chmod +x diff
./diff <testname>
```
For example:
```bash
./diff test1
```
This will run test1.tig first using your lexer and then with the provided lexer.
The two outputs will be diffed and that output will be displayed to you.  

To recursively run all the test at once run:
```bash
./diff all
```
##### Prog2
To test your parser against the one provided from class:
```bash
cd src
chmod u+x diff
./diff <testname>
./diff all
```

### Help

If things go wrong make sure your .profile and .env files are setup correctly and have been sourced.
Also, make sure your directory structure is setup correctly.
