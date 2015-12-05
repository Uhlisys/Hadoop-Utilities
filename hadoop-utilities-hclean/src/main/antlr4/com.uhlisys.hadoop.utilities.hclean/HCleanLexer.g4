lexer grammar HCleanLexer;

WS             :   [ \t\n\r]+ -> skip ;

LPAREN         :   '('     ;
RPAREN         :   ')'     ;
LCBRACK        :   '{'     ;
RCBRACK        :   '}'     ;
LSBRACK        :   '['     ;
RSBRACK        :   ']'     ;

AND            :   'AND'   ;
OR             :   'OR'    ;
XOR            :   'XOR'   ;

NOT            :   '!'     ;
ADD            :   '+'     ;
SUB            :   '-'     ;
MUL            :   '*'     ;
DIV            :   '/'     ;
POW            :   '**'    ;
MOD            :   '%'     ;

LT             :   '<'     ;
LTE            :   '<='    ;
EQ             :   '=='    ;
NE             :   '!='    ;
GTE            :   '>='    ;
GT             :   '>'     ;
CN             :   '<>'    ;
CM             :   '=~'    ;
COMMA          :   ','     ;

ACCESSTIME     :   'atime' ;
MODIFYTIME     :   'mtime' ;
ABSOLUTEPATH   :   'apath' ;
RELATIVEPATH   :   'rpath' ;
FILENAME       :   'filename' ;
REGEX          :   'regex' ;
CURRENTTIME    :   'ctime' ;
RUNTIME        :   'rtime' ;
RANDOM         :   'random' ;
DATE           :   'date' ;
PATH           :   'path' ;
TIME           :   'time' ;
TIMESTAMP      :   'timestamp' ;
GROUP          :   'group' ;
USER           :   'user' ;
ISDIRECTORY    :   'isDirectory' ;
ISFILE         :   'isFile' ;
ISSYMLINK      :   'isSymlink' ;
BOOLEANLITERAL :   'true' | 'false' ;
INTLITERAL     :   (ADD|SUB)?('0'..'9')+ ;
STRLITERAL     :   '\'' ( ESC_SEQ | ~('\\'|'\'') )* '\'' ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;

fragment
HEX_DIGIT
    : ('0'..'9'|'a'..'f'|'A'..'F')
    ;