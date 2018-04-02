
/* --------------------------Codigo de Usuario----------------------- */
package compi201503600.analisis.java;

import java_cup.runtime.*;
import java.io.Reader;
      
%% //inicio de opciones
   
/* ------ Seccion de opciones y declaraciones de JFlex -------------- */  
   
/* 
    Cambiamos el nombre de la clase del analizador a Lexer
*/
%class ScannerLexJava
%public
/*
    Activar el contador de lineas, variable yyline
    Activar el contador de columna, variable yycolumn
*/
%line
%column
    
/* 
   Activamos la compatibilidad con Java CUP para analizadores
   sintacticos(parser)
*/
%cup
%state CADENA
%ignorecase
/*
    Declaraciones

    El codigo entre %{  y %} sera copiado integramente en el 
    analizador generado.
*/
%{
    String strliteral = "";

    /*  Generamos un java_cup.Symbol para guardar el tipo de token 
        encontrado */
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    /* Generamos un Symbol para el tipo de token encontrado 
       junto con su valor */
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}
   

/*
    Macro declaraciones
  
    Declaramos expresiones regulares que despues usaremos en las
    reglas lexicas.
*/
   

//expresiones

FINLINEA = \r|\n|\r\n
CARACTERES = [^\r\n]

COMENTARIO = {LINECOMMENT} | {MULTILINECOMMENT}
LINECOMMENT = "//" {CARACTERES}* {FINLINEA}?
MULTILINECOMMENT = "/*" [^*] ~"*/" | "/*" "*"+ "*/"

ENTERO  = [0-9]+   
DECIMAL = [0-9]+.[0-9]+
ID      = [A-Za-zñÑ][_0-9A-Za-zñÑ]*

SPACE   = [ \ \r\t\f\t]
ENTER   = [\ \n]

%%
/* -------------------- Seccion de reglas lexicas ------------------ */

   
<YYINITIAL> {
   
    "("                { return symbol(sym.PARENIZQ); }
    ")"                { return symbol(sym.PARENDER); }
    "["                { return symbol(sym.CORCHIZQ); }
    "]"                { return symbol(sym.CORCHDER); }
    "{"                { return symbol(sym.LLAVEIZQ); }
    "}"                { return symbol(sym.LLAVEDER); }
    ";"                { return symbol(sym.PCOMA); }
    ":"                { return symbol(sym.DPUNTOS); }
    ","                { return symbol(sym.COMA); }
    "."                { return symbol(sym.PUNTO); }
    "="                { return symbol(sym.IGUAL); }
    "+="               { return symbol(sym.MASIGUAL); }
    "-="               { return symbol(sym.MENIGUAL); }
    "*="               { return symbol(sym.MULTIGUAL); }
    "+"                { return symbol(sym.OPSUMA); }
    "-"                { return symbol(sym.OPRESTA); }
    "*"                { return symbol(sym.OPMULT); }
    "/"                { return symbol(sym.OPDIV); }
    "%"                { return symbol(sym.OPMODULO); }
    "++"               { return symbol(sym.OPINCREMENT); }
    "--"               { return symbol(sym.OPDECREMENT); }
    "<"                { return symbol(sym.OPMEN); }
    ">"                { return symbol(sym.OPMAY); }
    "<="               { return symbol(sym.OPMENIGUAL); }
    ">="               { return symbol(sym.OPMAYIGUAL); }
    "=="               { return symbol(sym.OPIGUAL); }
    "!="               { return symbol(sym.OPDISTINTO); }
    "&&"               { return symbol(sym.OPAND); }
    "||"               { return symbol(sym.OPOR); }
    "!"                { return symbol(sym.OPNOT); }

    "package"          { return symbol(sym.PACKAGE); }
    "import"           { return symbol(sym.IMPORTA); }
    "private"          { return symbol(sym.PRIVATE); }
    "public"           { return symbol(sym.PUBLIC); }
    "protected"        { return symbol(sym.PROTECTED); }
    "final"            { return symbol(sym.FINAL); }
    "class"            { return symbol(sym.CLASE); }
    "this"             { return symbol(sym.THIS); }
    "new"              { return symbol(sym.NEW); }
    "null"             { return symbol(sym.NULL); }
    "void"             { return symbol(sym.TVOID); }
    "int"              { return symbol(sym.TINT); }
    "boolean"          { return symbol(sym.TBOOLEAN); }
    "string"           { return symbol(sym.TSTRING); }
    "char"             { return symbol(sym.TCHAR); }
    "double"           { return symbol(sym.TDOUBLE); }
    "object"           { return symbol(sym.TOBJECT); }
    "if"               { return symbol(sym.IF); }
    "else"             { return symbol(sym.ELSE); }
    "for"              { return symbol(sym.FOR); }
    "while"            { return symbol(sym.WHILE); }
    "do"               { return symbol(sym.DO); }
    "switch"           { return symbol(sym.SWITCH); }
    "case"             { return symbol(sym.CASE); }
    "default"          { return symbol(sym.DEFAULT); }
    "break"            { return symbol(sym.BREAK); }
    "return"           { return symbol(sym.RETURN); }
    "system"           { return symbol(sym.SYSTEM); }
    "out"              { return symbol(sym.OUT); }
    "print"|"println"  { return symbol(sym.PRINT); }
    "static"           { return symbol(sym.STATIC); }
    "main"             { return symbol(sym.MAIN); }
    "args"             { return symbol(sym.ARGS); }

    [\"]               { yybegin(CADENA);
                         strliteral = "\""; }
   
    {ENTERO}           { return symbol(sym.ENTERO, yytext());  }
    {DECIMAL}          { return symbol(sym.DECIMAL, yytext()); }
    {ID}               { return symbol(sym.ID, yytext()); }

    {COMENTARIO}       { return symbol(sym.COMENTARIO, yytext()); }
    {SPACE}            { /* ignora el espacio */ } 
    {ENTER}            { /* ignora el espacio */ } 
}

<CADENA> {
    [^\"]              { strliteral += yytext(); }
    [\"]               { yybegin(YYINITIAL);
                         strliteral += "\"";
                         return symbol(sym.STRING_LITERAL, strliteral); }
    [\n\r]             { yybegin(YYINITIAL);
                         throw new Error("Se esperaba fin de cadena"); }
}


/* Si el token contenido en la entrada no coincide con ninguna regla
    entonces se marca un token ilegal */
[^]                    { throw new Error("Caracter ilegal <"+yytext()+">"); }
