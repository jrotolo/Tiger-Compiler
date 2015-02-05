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
<YYINITIAL> " "	{}
<YYINITIAL> \n	{ newline(); }
<YYINITIAL> ","	{ return tok(sym.COMMA, null); }

<YYINITIAL> "while"      { return tok(sym.WHILE); }
<YYINITIAL> "for"        { return tok(sym.FOR); }
<YYINITIAL> "to"         { return tok(sym.TO); }
<YYINITIAL> "break"      { return tok(sym.BREAK); }
<YYINITIAL> "let"        { return tok(sym.LET); }
<YYINITIAL> "in"         { return tok(sym.IN); }
<YYINITIAL> "end"        { return tok(sym.END); }
<YYINITIAL> "function"   { return tok(sym.FUNCTION); }
<YYINITIAL> "var"        { return tok(sym.VAR); }
<YYINITIAL> "type"       { return tok(sym.TYPE); }
<YYINITIAL> "array"      { return tok(sym.ARRAY); }
<YYINITIAL> "if"         { return tok(sym.IF); }
<YYINITIAL> "then"       { return tok(sym.THEN); }
<YYINITIAL> "else"       { return tok(sym.ELSE); }
<YYINITIAL> "do"         { return tok(sym.DO); }
<YYINITIAL> "nil"        { return tok(sym.NIL); }

. { err("Illegal character: " + yytext()); }
