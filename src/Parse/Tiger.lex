package Parse;
import ErrorMsg.ErrorMsg;

%% 

%implements Lexer
%function nextToken
%type java_cup.runtime.Symbol
%char

%{
private void newline() {
  errorMsg.newline(yychar);
}

private void err(int pos, String s) {
  errorMsg.error(pos,s);
}

private void err(String s) {
  err(yychar,s);
}

private java_cup.runtime.Symbol tok(int kind) {
    return tok(kind, null);
}

private java_cup.runtime.Symbol tok(int kind, Object value) {
    return new java_cup.runtime.Symbol(kind, yychar, yychar+yylength(), value);
}

private ErrorMsg errorMsg;

Yylex(java.io.InputStream s, ErrorMsg e) {
  this(s);
  errorMsg=e;
}

%}

%eofval{
	{
	 return tok(sym.EOF, null);
        }
%eofval}       




%%
<YYINITAL> " "	{}
<YYINITAL> \n	{ newline(); }
<YYINITAL> ","	{ return tok(sym.COMMA, null); }

// Keywords
<YYINITAL> "while"      { return tok(sym.WHILE); }
<YYINITAL> "for"        { return tok(sym.FOR); }
<YYINITAL> "to"         { return tok(sym.TO); }
<YYINITAL> "break"      { return tok(sym.BREAK); }
<YYINITAL> "let"        { return tok(sym.LET); }
<YYINITAL> "in"         { return tok(sym.IN); }
<YYINITAL> "end"        { return tok(sym.END); }
<YYINITAL> "function"   { return tok(sym.FUNCTION); }
<YYINITAL> "var"        { return tok(sym.VAR); }
<YYINITAL> "type"       { return tok(sym.TYPE); }
<YYINITAL> "array"      { return tok(sym.ARRAY); }
<YYINITAL> "if"         { return tok(sym.IF); }
<YYINITAL> "then"       { return tok(sym.THEN); }
<YYINITAL> "else"       { return tok(sym.ELSE); }
<YYINITAL> "do"         { return tok(sym.DO); }
<YYINITAL> "nil"        { return tok.NIL); }

. { err("Illegal character: " + yytext()); }
