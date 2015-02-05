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
" "	{}
\n	{newline();}
","	{return tok(sym.COMMA, null);}

// Operators
<YYINITIAL> "=" { return tok(sym.EQ, null); }
<YYINITIAL> "+" { return tok(sym.ADD, null); }
<YYINITIAL> "-" { return tok(sym.MINUS, null); }
<YYINITIAL> "*" { return tok(sym.TIMES, null); }
<YYINITIAL> "/" { return tok(sym.DIVIDE, null); }
<YYINITIAL> "<>" { return tok(sym.NEQ, null); }
<YYINITIAL> ">" { return tok(sym.GT, null); }
<YYINITIAL> "<" { return tok(sym.LT, null); }
<YYINITIAL> ">=" { return tok(sym.GE, null); }
<YYINITIAL> "<=" { return tok(sym.LE, null); }
<YYINITIAL> "&" { return tok(sym.AND, null); }
<YYINITIAL> "|" { return tok(sym.OR, null); }
<YYINITIAL> ":=" { return tok(sym.ASSIGN, null); }

. { err("Illegal character: " + yytext()); }
