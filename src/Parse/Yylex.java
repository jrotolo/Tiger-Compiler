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
		35
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
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR
	};
	private int yy_cmap[] = {
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 1, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		2, 0, 0, 0, 0, 0, 3, 0,
		0, 0, 4, 5, 6, 7, 0, 8,
		9, 9, 9, 9, 9, 9, 9, 9,
		9, 9, 10, 0, 11, 12, 13, 0,
		0, 14, 14, 14, 14, 14, 14, 14,
		14, 14, 14, 14, 14, 14, 14, 14,
		14, 14, 14, 14, 14, 14, 14, 14,
		14, 14, 14, 14, 14, 14, 14, 15,
		14, 16, 17, 18, 19, 20, 21, 14,
		22, 23, 14, 24, 25, 14, 26, 27,
		28, 14, 29, 30, 31, 32, 33, 34,
		14, 35, 14, 0, 36, 0, 0, 0
		
	};
	private int yy_rmap[] = {
		0, 1, 1, 1, 1, 1, 1, 1,
		1, 2, 3, 1, 4, 1, 1, 1,
		1, 1, 1, 5, 5, 5, 5, 5,
		5, 5, 5, 5, 5, 5, 5, 5,
		5, 5, 5, 6, 1, 1, 1, 7,
		8, 9, 5, 10, 11, 12, 13, 14,
		15, 16, 17, 18, 19, 20, 21, 22,
		23, 24, 25, 26, 27, 28, 29, 30,
		31, 32, 33, 34, 35, 36, 37, 38,
		39, 40, 41, 42, 43, 44, 45, 46,
		47 
	};
	private int yy_nxt[][] = unpackFromString(48,37,
"1,2,3,4,5,6,7,8,9,39,40,10,11,12,42:2,75,76,42,44,59,60,42,45,42,61,62,42:4,46,42,63,77,42,13,-1:41,14,-1:44,16,17,-1:35,18,-1:33,39,-1:4,42:22,-1,36,-1,36:2,41,36:3,43,36:28,-1:9,39,-1:5,39,-1:33,15,-1:32,37,-1:32,38,-1:41,39,-1:4,42:13,19,42:8,-1:10,39,-1:4,42:7,20,42:4,21,42:9,-1:10,39,-1:4,42:8,65,42:4,22,42:7,66,-1:10,39,-1:4,42:5,23,42:16,-1:10,39,-1:4,42:15,24,42:6,-1:10,39,-1:4,42:17,25,42:4,-1:10,39,-1:4,42:11,26,42:10,-1:10,39,-1:4,42:15,27,42:6,-1:10,39,-1:4,42:6,28,42:15,-1:10,39,-1:4,42:12,29,42:9,-1:10,39,-1:4,42:6,30,42:15,-1:10,39,-1:4,42:21,31,-1:10,39,-1:4,42:10,32,42:11,-1:10,39,-1:4,42:6,33,42:15,-1:10,39,-1:4,42:12,34,42:9,-1:10,39,-1:4,42:11,64,47,42:9,-1:10,39,-1:4,42:13,48,42:4,80,42:3,-1:10,39,-1:4,42:6,49,42:15,-1:10,39,-1:4,42:9,50,42:12,-1:10,39,-1:4,42:2,51,42:19,-1:10,39,-1:4,42:16,52,42:5,-1:10,39,-1:4,42:6,53,42:15,-1:10,39,-1:4,42:14,54,42:7,-1:10,39,-1:4,42:2,55,42:19,-1:10,39,-1:4,42:2,56,42:19,-1:10,39,-1:4,42:11,57,42:10,-1:10,39,-1:4,42:13,58,42:8,-1:10,39,-1:4,42:15,67,42:6,-1:10,39,-1:4,42:6,68,42:15,-1:10,39,-1:4,42:9,69,42:12,-1:10,39,-1:4,42:9,70,42:12,-1:10,39,-1:4,42:15,71,42:6,-1:10,39,-1:4,42:15,72,42:6,-1:10,39,-1:4,42:8,73,42:13,-1:10,39,-1:4,42:17,74,42:4,-1:10,39,-1:4,42:4,78,42:17,-1:10,39,-1:4,42:12,79,42:9,-1");
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
					case 0:
						{ return tok(sym.ID, yytext()); }
					case -2:
						break;
					case 1:
						{ err("Illegal character: " + yytext()); }
					case -3:
						break;
					case 2:
						{ newline(); }
					case -4:
						break;
					case 3:
						{ }
					case -5:
						break;
					case 4:
						{ return tok(sym.AND, null); }
					case -6:
						break;
					case 5:
						{ return tok(sym.TIMES, null); }
					case -7:
						break;
					case 6:
						{ return tok(sym.PLUS, null); }
					case -8:
						break;
					case 7:
						{ return tok(sym.COMMA, null); }
					case -9:
						break;
					case 8:
						{ return tok(sym.MINUS, null); }
					case -10:
						break;
					case 9:
						{ return tok(sym.DIVIDE, null); }
					case -11:
						break;
					case 10:
						{ return tok(sym.LT, null); }
					case -12:
						break;
					case 11:
						{ return tok(sym.EQ, null); }
					case -13:
						break;
					case 12:
						{ return tok(sym.GT, null); }
					case -14:
						break;
					case 13:
						{ return tok(sym.OR, null); }
					case -15:
						break;
					case 14:
						{ yybegin(COMMENT); }
					case -16:
						break;
					case 15:
						{ return tok(sym.ASSIGN, null); }
					case -17:
						break;
					case 16:
						{ return tok(sym.LE, null); }
					case -18:
						break;
					case 17:
						{ return tok(sym.NEQ, null); }
					case -19:
						break;
					case 18:
						{ return tok(sym.GE, null); }
					case -20:
						break;
					case 19:
						{ return tok(sym.DO); }
					case -21:
						break;
					case 20:
						{ return tok(sym.IF); }
					case -22:
						break;
					case 21:
						{ return tok(sym.IN); }
					case -23:
						break;
					case 22:
						{ return tok(sym.TO); }
					case -24:
						break;
					case 23:
						{ return tok(sym.END); }
					case -25:
						break;
					case 24:
						{ return tok(sym.FOR); }
					case -26:
						break;
					case 25:
						{ return tok(sym.LET); }
					case -27:
						break;
					case 26:
						{ return tok(sym.NIL); }
					case -28:
						break;
					case 27:
						{ return tok(sym.VAR); }
					case -29:
						break;
					case 28:
						{ return tok(sym.ELSE); }
					case -30:
						break;
					case 29:
						{ return tok(sym.THEN); }
					case -31:
						break;
					case 30:
						{ return tok(sym.TYPE); }
					case -32:
						break;
					case 31:
						{ return tok(sym.ARRAY); }
					case -33:
						break;
					case 32:
						{ return tok(sym.BREAK); }
					case -34:
						break;
					case 33:
						{ return tok(sym.WHILE); }
					case -35:
						break;
					case 34:
						{ return tok(sym.FUNCTION); }
					case -36:
						break;
					case 36:
						{ }
					case -37:
						break;
					case 37:
						{
	if (commentDepth <= 0)
		yybegin(YYINITIAL);
	else
		commentDepth--;
}
					case -38:
						break;
					case 38:
						{
	commentDepth++;
	yybegin(COMMENT);
}
					case -39:
						break;
					case 39:
						{ return tok(sym.ID, yytext()); }
					case -40:
						break;
					case 40:
						{ err("Illegal character: " + yytext()); }
					case -41:
						break;
					case 41:
						{ }
					case -42:
						break;
					case 42:
						{ return tok(sym.ID, yytext()); }
					case -43:
						break;
					case 43:
						{ }
					case -44:
						break;
					case 44:
						{ return tok(sym.ID, yytext()); }
					case -45:
						break;
					case 45:
						{ return tok(sym.ID, yytext()); }
					case -46:
						break;
					case 46:
						{ return tok(sym.ID, yytext()); }
					case -47:
						break;
					case 47:
						{ return tok(sym.ID, yytext()); }
					case -48:
						break;
					case 48:
						{ return tok(sym.ID, yytext()); }
					case -49:
						break;
					case 49:
						{ return tok(sym.ID, yytext()); }
					case -50:
						break;
					case 50:
						{ return tok(sym.ID, yytext()); }
					case -51:
						break;
					case 51:
						{ return tok(sym.ID, yytext()); }
					case -52:
						break;
					case 52:
						{ return tok(sym.ID, yytext()); }
					case -53:
						break;
					case 53:
						{ return tok(sym.ID, yytext()); }
					case -54:
						break;
					case 54:
						{ return tok(sym.ID, yytext()); }
					case -55:
						break;
					case 55:
						{ return tok(sym.ID, yytext()); }
					case -56:
						break;
					case 56:
						{ return tok(sym.ID, yytext()); }
					case -57:
						break;
					case 57:
						{ return tok(sym.ID, yytext()); }
					case -58:
						break;
					case 58:
						{ return tok(sym.ID, yytext()); }
					case -59:
						break;
					case 59:
						{ return tok(sym.ID, yytext()); }
					case -60:
						break;
					case 60:
						{ return tok(sym.ID, yytext()); }
					case -61:
						break;
					case 61:
						{ return tok(sym.ID, yytext()); }
					case -62:
						break;
					case 62:
						{ return tok(sym.ID, yytext()); }
					case -63:
						break;
					case 63:
						{ return tok(sym.ID, yytext()); }
					case -64:
						break;
					case 64:
						{ return tok(sym.ID, yytext()); }
					case -65:
						break;
					case 65:
						{ return tok(sym.ID, yytext()); }
					case -66:
						break;
					case 66:
						{ return tok(sym.ID, yytext()); }
					case -67:
						break;
					case 67:
						{ return tok(sym.ID, yytext()); }
					case -68:
						break;
					case 68:
						{ return tok(sym.ID, yytext()); }
					case -69:
						break;
					case 69:
						{ return tok(sym.ID, yytext()); }
					case -70:
						break;
					case 70:
						{ return tok(sym.ID, yytext()); }
					case -71:
						break;
					case 71:
						{ return tok(sym.ID, yytext()); }
					case -72:
						break;
					case 72:
						{ return tok(sym.ID, yytext()); }
					case -73:
						break;
					case 73:
						{ return tok(sym.ID, yytext()); }
					case -74:
						break;
					case 74:
						{ return tok(sym.ID, yytext()); }
					case -75:
						break;
					case 75:
						{ return tok(sym.ID, yytext()); }
					case -76:
						break;
					case 76:
						{ return tok(sym.ID, yytext()); }
					case -77:
						break;
					case 77:
						{ return tok(sym.ID, yytext()); }
					case -78:
						break;
					case 78:
						{ return tok(sym.ID, yytext()); }
					case -79:
						break;
					case 79:
						{ return tok(sym.ID, yytext()); }
					case -80:
						break;
					case 80:
						{ return tok(sym.ID, yytext()); }
					case -81:
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
