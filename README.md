# Tiger Compiler

This is a repo for a compiler implementation for a small language called Tiger.

### Setup

Create a .env file and add your LSU login ID and LSU serve name.
This setup script will download the needed JLex and JavaCup class files.
```bash
./setup
```
Your working directory should be ready to build the project.

### Testing

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

### Help

If things go wrong make sure your .profile and .env files are setup correctly.
