parser grammar HCleanParser;

options { tokenVocab=HCleanLexer; }

/* Boolean Nodes */
booleanExpression
    :   booleanTerm AND booleanExpression # booleanAndExpression
    |   booleanTerm XOR booleanExpression # booleanXorExpression
    |   booleanTerm OR  booleanExpression # booleanOrExpression
    |   NOT booleanExpression             # booleanNotExpression
    |   booleanTerm                       # booleanTermExpression
    ;
booleanTerm
    :   integerComparison
    |   stringComparison    
    |   booleanLiteral
    |   isDirectory
    |   isFile
    |   isSymlink
    |   LPAREN booleanExpression RPAREN
    ;
booleanLiteral  :   BOOLEANLITERAL;
isDirectory     :   ISDIRECTORY;
isFile          :   ISFILE;
isSymlink       :   ISSYMLINK;

/* Integer Nodes */
integerComparison
    :   integerExpression LT  integerExpression  # integerLessThanComparison
    |   integerExpression LTE integerExpression  # integerLessThanOrEqualComparison
    |   integerExpression EQ  integerExpression  # integerEqualComparison
    |   integerExpression NE  integerExpression  # integerNonEqualComparison
    |   integerExpression GTE integerExpression  # integerGreaterThanOrEqualComparison
    |   integerExpression GT  integerExpression  # integerGreaterThanComparison
    ;
integerExpression
    :   integerTerm ADD integerExpression  # integerAdditionExpression
    |   integerTerm SUB integerExpression  # integerSubtractionExpression
    |   integerTerm MUL integerExpression  # integerMultiplicationExpression
    |   integerTerm DIV integerExpression  # integerDivisionExpression
    |   integerTerm POW integerExpression  # integerPowersExpression
    |   integerTerm MOD integerExpression  # integerModuloExpression
    |   integerTerm                        # integerTermExpression
    ;
integerTerm
    :   accessTime
    |   modifyTime
    |   currentTime
    |   runTime
    |   pathConversion
    |   randomInteger
    |   timeLiteral
    |   timestampLiteral
    |   integerLiteral
    |   LPAREN integerExpression RPAREN
    ;
accessTime       :   ACCESSTIME;
currentTime      :   CURRENTTIME;
integerLiteral   :   INTLITERAL;
modifyTime       :   MODIFYTIME;
pathConversion   :   PATH LCBRACK STRLITERAL COMMA integerExpression RCBRACK;
randomInteger    :   RANDOM;
runTime          :   RUNTIME;
timeLiteral      :   TIME LCBRACK STRLITERAL RCBRACK;
dateLiteral      :   DATE LCBRACK STRLITERAL RCBRACK;
timestampLiteral :   TIMESTAMP LCBRACK STRLITERAL RCBRACK;

/* String Nodes */
stringComparison
    :   stringExpression EQ stringExpression  # stringEquality
    |   stringExpression NE stringExpression  # stringNonEquality
    |   stringExpression CN stringExpression  # stringContains
    |   stringExpression CM stringExpression  # stringMatches
    ;
stringExpression
    :   stringTerm ADD stringExpression      # stringConcatenationExpression
    |   stringTerm                           # stringTermExpression
    ;
stringTerm
    :   absolutePath
    |   filename    
    |   owningGroup
    |   owningUser    
    |   relativePath    
    |   stringLiteral
    |   LPAREN stringExpression RPAREN
    ;
absolutePath    :   ABSOLUTEPATH;
filename        :   FILENAME;
owningGroup     :   GROUP;
owningUser      :   USER;
relativePath    :   RELATIVEPATH;
stringLiteral   :   STRLITERAL;
