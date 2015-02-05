package Parse;
import ErrorMsg.ErrorMsg;


class Yylex implements Lexer {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final char YYEOF = '\uFFFF';

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
private int commentDepth = 0;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		76
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private char yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YYEOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YYEOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_start () {
		++yychar;
		++yy_buffer_start;
	}
	private void yy_pushback () {
		--yy_buffer_end;
	}
	private void yy_mark_start () {
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
private int [][] unpackFromString(int size1, int size2, String st)
    {
      int colonIndex = -1;
      String lengthString;
      int sequenceLength = 0;
      int sequenceInteger = 0;
      int commaIndex;
      String workString;
      int res[][] = new int[size1][size2];
      for (int i= 0; i < size1; i++)
	for (int j= 0; j < size2; j++)
	  {
	    if (sequenceLength == 0) 
	      {	
		commaIndex = st.indexOf(',');
		if (commaIndex == -1)
		  workString = st;
		else
		  workString = st.substring(0, commaIndex);
		st = st.substring(commaIndex+1);
		colonIndex = workString.indexOf(':');
		if (colonIndex == -1)
		  {
		    res[i][j] = Integer.parseInt(workString);
		  }
		else 
		  {
		    lengthString = workString.substring(colonIndex+1);  
		    sequenceLength = Integer.parseInt(lengthString);
		    workString = workString.substring(0,colonIndex);
		    sequenceInteger = Integer.parseInt(workString);
		    res[i][j] = sequenceInteger;
		    sequenceLength--;
		  }
	      }
	    else 
	      {
		res[i][j] = sequenceInteger;
		sequenceLength--;
	      }
	  }
      return res;
    }
	private int yy_acpt[] = {
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NOT_ACCEPT,
		YY_NOT_ACCEPT,
		YY_NOT_ACCEPT,
		YY_NOT_ACCEPT,
		YY_NOT_ACCEPT,
		YY_NOT_ACCEPT,
		YY_NOT_ACCEPT,
		YY_NOT_ACCEPT,
		YY_NOT_ACCEPT,
		YY_NOT_ACCEPT,
		YY_NOT_ACCEPT,
		YY_NOT_ACCEPT,
		YY_NOT_ACCEPT,
		YY_NOT_ACCEPT
	};
	private int yy_cmap[] = {
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 1, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		2, 0, 0, 0, 0, 0, 3, 0,
		0, 0, 4, 5, 6, 7, 0, 8,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 9, 0, 10, 11, 12, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 13, 14, 15, 16, 17, 18, 0,
		19, 20, 0, 21, 22, 0, 23, 24,
		25, 0, 26, 27, 28, 29, 30, 31,
		0, 32, 0, 0, 33, 0, 0, 0
		
	};
	private int yy_rmap[] = {
		0, 1, 1, 1, 1, 1, 1, 1,
		1, 2, 3, 1, 4, 1, 1, 1,
		1, 1, 1, 1, 1, 1, 1, 1,
		1, 1, 1, 1, 1, 1, 1, 1,
		1, 1, 1, 1, 1, 1, 5, 6,
		7, 8, 9, 10, 11, 12, 13, 14,
		15, 16, 17, 18, 19, 20, 21, 22,
		23, 24, 25, 26, 27, 28, 29, 30,
		31, 32, 33, 34, 35, 36, 37, 38,
		39, 40, 41, 42, 43, 44, 45 
	};
	private int yy_nxt[][] = unpackFromString(46,34,
"1,2,3,4,5,6,7,8,9,39,10,11,12,42,45,1,47,49,51,1,53,1,55,57,1:4,59,1,61,63,1,13,-1:38,14,-1:40,16,17,-1:32,18,-1:48,62,-1:18,15,-1:30,36,-1:42,78,-1:42,38,-1:11,37,-1:56,64,-1:32,41,-1:23,23,-1:41,19,-1:35,24,-1:29,44,46,-1:33,65,-1:34,48,-1:4,50,-1:32,25,-1:23,20,-1:4,21,-1:32,26,-1:28,52,-1:41,67,-1:28,54,-1:39,27,-1:26,77,-1:4,22,-1:7,56,-1:21,68,-1:26,58,-1:33,69,-1:39,60,-1:31,28,-1:31,71,-1:41,29,-1:27,30,-1:38,72,-1:43,31,-1:22,32,-1:40,73,-1:22,33,-1:36,74,-1:37,75,-1:32,34,-1:10,35,-1,35:2,40,35:3,43,35:25,-1:17,66,-1:29,70,-1:20");
	public java_cup.runtime.Symbol nextToken ()
		throws java.io.IOException {
		char yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			if (YYEOF != yy_lookahead) {
				yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YYEOF == yy_lookahead && true == yy_initial) {

	{
	 return tok(sym.EOF, null);
        }
				}
				else if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_to_mark();
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_pushback();
					}
					if (0 != (YY_START & yy_anchor)) {
						yy_move_start();
					}
					switch (yy_last_accept_state) {
					case 1:
						{ err("Illegal character: " + yytext()); }
					case -2:
						break;
					case 2:
						{ newline(); }
					case -3:
						break;
					case 3:
						{}
					case -4:
						break;
					case 4:
						{ return tok(sym.AND, null); }
					case -5:
						break;
					case 5:
						{ return tok(sym.TIMES, null); }
					case -6:
						break;
					case 6:
						{ return tok(sym.PLUS, null); }
					case -7:
						break;
					case 7:
						{ return tok(sym.COMMA, null); }
					case -8:
						break;
					case 8:
						{ return tok(sym.MINUS, null); }
					case -9:
						break;
					case 9:
						{ return tok(sym.DIVIDE, null); }
					case -10:
						break;
					case 10:
						{ return tok(sym.LT, null); }
					case -11:
						break;
					case 11:
						{ return tok(sym.EQ, null); }
					case -12:
						break;
					case 12:
						{ return tok(sym.GT, null); }
					case -13:
						break;
					case 13:
						{ return tok(sym.OR, null); }
					case -14:
						break;
					case 14:
						{ yybegin(COMMENT); }
					case -15:
						break;
					case 15:
						{ return tok(sym.ASSIGN, null); }
					case -16:
						break;
					case 16:
						{ return tok(sym.LE, null); }
					case -17:
						break;
					case 17:
						{ return tok(sym.NEQ, null); }
					case -18:
						break;
					case 18:
						{ return tok(sym.GE, null); }
					case -19:
						break;
					case 19:
						{ return tok(sym.DO); }
					case -20:
						break;
					case 20:
						{ return tok(sym.IF); }
					case -21:
						break;
					case 21:
						{ return tok(sym.IN); }
					case -22:
						break;
					case 22:
						{ return tok(sym.TO); }
					case -23:
						break;
					case 23:
						{ return tok(sym.END); }
					case -24:
						break;
					case 24:
						{ return tok(sym.FOR); }
					case -25:
						break;
					case 25:
						{ return tok(sym.LET); }
					case -26:
						break;
					case 26:
						{ return tok(sym.NIL); }
					case -27:
						break;
					case 27:
						{ return tok(sym.VAR); }
					case -28:
						break;
					case 28:
						{ return tok(sym.ELSE); }
					case -29:
						break;
					case 29:
						{ return tok(sym.THEN); }
					case -30:
						break;
					case 30:
						{ return tok(sym.TYPE); }
					case -31:
						break;
					case 31:
						{ return tok(sym.ARRAY); }
					case -32:
						break;
					case 32:
						{ return tok(sym.BREAK); }
					case -33:
						break;
					case 33:
						{ return tok(sym.WHILE); }
					case -34:
						break;
					case 34:
						{ return tok(sym.FUNCTION); }
					case -35:
						break;
					case 35:
						{ }
					case -36:
						break;
					case 36:
						{
	if (commentDepth <= 0)
		yybegin(YYINITIAL);
	else
		commentDepth--;
}
					case -37:
						break;
					case 37:
						{
	commentDepth++;
	yybegin(COMMENT);
}
					case -38:
						break;
					case 39:
						{ err("Illegal character: " + yytext()); }
					case -39:
						break;
					case 40:
						{ }
					case -40:
						break;
					case 42:
						{ err("Illegal character: " + yytext()); }
					case -41:
						break;
					case 43:
						{ }
					case -42:
						break;
					case 45:
						{ err("Illegal character: " + yytext()); }
					case -43:
						break;
					case 47:
						{ err("Illegal character: " + yytext()); }
					case -44:
						break;
					case 49:
						{ err("Illegal character: " + yytext()); }
					case -45:
						break;
					case 51:
						{ err("Illegal character: " + yytext()); }
					case -46:
						break;
					case 53:
						{ err("Illegal character: " + yytext()); }
					case -47:
						break;
					case 55:
						{ err("Illegal character: " + yytext()); }
					case -48:
						break;
					case 57:
						{ err("Illegal character: " + yytext()); }
					case -49:
						break;
					case 59:
						{ err("Illegal character: " + yytext()); }
					case -50:
						break;
					case 61:
						{ err("Illegal character: " + yytext()); }
					case -51:
						break;
					case 63:
						{ err("Illegal character: " + yytext()); }
					case -52:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
					}
				}
			}
		}
	}
}
