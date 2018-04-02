package compi201503600.analisis.report;

import java_cup.runtime.*;
import java.io.Reader;

%%

%{
    //Código de usuario
    String text = "";
%}

%cup
%class ScannerLexReport
%line
%column
%state YYINITIAL
%state CADENA
%state TEXTO
%ignorecase

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

//expresiones

ENTERO  = [0-9]+   
DECIMAL = [0-9]+.[0-9]+
ID      = [A-Za-zñÑ][_0-9A-Za-zñÑ]*

FINLINEA = \r|\n|\r\n
CARACTERES = [^\r\n]

COMENTARIO = {LINECOMMENT} | {MULTILINECOMMENT}
LINECOMMENT = "->" {CARACTERES}* {FINLINEA}?
MULTILINECOMMENT = "</" [^/] ~"/>" | "</" "/"+ "/>"

SPACE   = [\ \r\t\f\t]
ENTER   = [ \ \n]

%%
<YYINITIAL> "="            { return new Symbol(sym.OPIGUAL, yyline, yycolumn, yytext());}
<YYINITIAL> "+="           { return new Symbol(sym.OPASIGSUMA, yyline, yycolumn, yytext());}
<YYINITIAL> "-="           { return new Symbol(sym.OPASIGRESTA, yyline, yycolumn, yytext());}
<YYINITIAL> "*="           { return new Symbol(sym.OPASIGMULT, yyline, yycolumn, yytext());}
<YYINITIAL> "++"           { return new Symbol(sym.OPINCREMENT, yyline, yycolumn, yytext());}
<YYINITIAL> "--"           { return new Symbol(sym.OPDECREMENT, yyline, yycolumn, yytext());}
<YYINITIAL> "("            { return new Symbol(sym.PARA, yyline, yycolumn, yytext());}
<YYINITIAL> ")"            { return new Symbol(sym.PARC, yyline, yycolumn, yytext());}
<YYINITIAL> ";"            { return new Symbol(sym.PCOMA, yyline, yycolumn, yytext());}
<YYINITIAL> ","            { return new Symbol(sym.COMA, yyline, yycolumn, yytext());}
<YYINITIAL> "."            { return new Symbol(sym.PUNTO, yyline, yycolumn, yytext());}
<YYINITIAL> "["            { return new Symbol(sym.CORA, yyline, yycolumn, yytext());}
<YYINITIAL> "]"            { return new Symbol(sym.CORC, yyline, yycolumn, yytext());}
<YYINITIAL> "+"            { return new Symbol(sym.OPSUMA, yyline, yycolumn, yytext());}
<YYINITIAL> "-"            { return new Symbol(sym.OPRESTA, yyline, yycolumn, yytext());}
<YYINITIAL> "*"            { return new Symbol(sym.OPMULT, yyline, yycolumn, yytext());}
<YYINITIAL> "/"            { return new Symbol(sym.OPDIV, yyline, yycolumn, yytext());}
<YYINITIAL> "%"            { return new Symbol(sym.OPMODULO, yyline, yycolumn, yytext());}
<YYINITIAL> "<"            { return new Symbol(sym.OPMEN, yyline, yycolumn, yytext());}
<YYINITIAL> ">"            { return new Symbol(sym.OPMAY, yyline, yycolumn, yytext());}
<YYINITIAL> "<="            { return new Symbol(sym.OPMENIGUAL, yyline, yycolumn, yytext());}
<YYINITIAL> ">="            { return new Symbol(sym.OPMAYIGUAL, yyline, yycolumn, yytext());}
<YYINITIAL> "=="            { return new Symbol(sym.OPCOMPARADOR, yyline, yycolumn, yytext());}
<YYINITIAL> "!="            { return new Symbol(sym.OPDISTINTO, yyline, yycolumn, yytext());}
<YYINITIAL> "&&"            { return new Symbol(sym.OPAND, yyline, yycolumn, yytext());}
<YYINITIAL> "||"            { return new Symbol(sym.OPOR, yyline, yycolumn, yytext());}
<YYINITIAL> "!"            { return new Symbol(sym.OPNOT, yyline, yycolumn, yytext());}

<YYINITIAL> "RESULT"       { return new Symbol(sym.RESULT, yyline, yycolumn, yytext());}
<YYINITIAL> "Score"        { return new Symbol(sym.SCORE, yyline, yycolumn, yytext());}
<YYINITIAL> "variables"    { return new Symbol(sym.VARIABLES, yyline, yycolumn, yytext());}
<YYINITIAL> "nombre"       { return new Symbol(sym.NOMBRE, yyline, yycolumn, yytext());}
<YYINITIAL> "cantidad"     { return new Symbol(sym.CANTIDAD, yyline, yycolumn, yytext());}
<YYINITIAL> "metodos"      { return new Symbol(sym.METODOS, yyline, yycolumn, yytext());}
<YYINITIAL> "clases"       { return new Symbol(sym.CLASES, yyline, yycolumn, yytext());}
<YYINITIAL> "Entero"       { return new Symbol(sym.TINT, yyline, yycolumn, yytext());}
<YYINITIAL> "Decimal"      { return new Symbol(sym.TDOUBLE, yyline, yycolumn, yytext());}
<YYINITIAL> "Caracter"     { return new Symbol(sym.TCHAR, yyline, yycolumn, yytext());}
<YYINITIAL> "Texto"        { return new Symbol(sym.TSTRING, yyline, yycolumn, yytext());}
<YYINITIAL> "Booleano"     { return new Symbol(sym.TBOOL, yyline, yycolumn, yytext());}
<YYINITIAL> "PRINT"        { return new Symbol(sym.PRINT, yyline, yycolumn, yytext());}

<YYINITIAL> "html"      { return new Symbol(sym.HTML, yyline, yycolumn, yytext());}
<YYINITIAL> "head"      { return new Symbol(sym.HEAD, yyline, yycolumn, yytext());}
<YYINITIAL> "body"      { return new Symbol(sym.BODY, yyline, yycolumn, yytext());}
<YYINITIAL> "title"     { return new Symbol(sym.TITLE, yyline, yycolumn, yytext());}
<YYINITIAL> "h1"        { return new Symbol(sym.H1, yyline, yycolumn, yytext());}
<YYINITIAL> "h2"        { return new Symbol(sym.H2, yyline, yycolumn, yytext());}
<YYINITIAL> "h3"        { return new Symbol(sym.H3, yyline, yycolumn, yytext());}
<YYINITIAL> "h4"        { return new Symbol(sym.H4, yyline, yycolumn, yytext());}
<YYINITIAL> "h5"        { return new Symbol(sym.H5, yyline, yycolumn, yytext());}
<YYINITIAL> "h6"        { return new Symbol(sym.H6, yyline, yycolumn, yytext());}
<YYINITIAL> "table"     { return new Symbol(sym.TABLE, yyline, yycolumn, yytext());}
<YYINITIAL> "th"        { return new Symbol(sym.TH, yyline, yycolumn, yytext());}
<YYINITIAL> "td"        { return new Symbol(sym.TD, yyline, yycolumn, yytext());}
<YYINITIAL> "tr"        { return new Symbol(sym.TR, yyline, yycolumn, yytext());}
<YYINITIAL> "div"       { return new Symbol(sym.DIV, yyline, yycolumn, yytext());}
<YYINITIAL> "p"         { return new Symbol(sym.P, yyline, yycolumn, yytext());}
<YYINITIAL> "br"        { return new Symbol(sym.BR, yyline, yycolumn, yytext());}
<YYINITIAL> "hr"        { return new Symbol(sym.HR, yyline, yycolumn, yytext());}
<YYINITIAL> "color"     { return new Symbol(sym.COLOR, yyline, yycolumn, yytext());}
<YYINITIAL> "textcolor" { return new Symbol(sym.TEXTCOLOR, yyline, yycolumn, yytext());}
<YYINITIAL> "align"     { return new Symbol(sym.ALIGN, yyline, yycolumn, yytext());}
<YYINITIAL> "font"      { return new Symbol(sym.FONT, yyline, yycolumn, yytext());}

<YYINITIAL> {ENTERO}       { return new Symbol(sym.ENTERO, yyline, yycolumn,yytext());}
<YYINITIAL> {DECIMAL}      { return new Symbol(sym.DECIMAL, yyline, yycolumn, yytext());}
<YYINITIAL> {ID}           { return new Symbol(sym.ID, yyline, yycolumn,yytext());}
<YYINITIAL> {COMENTARIO}   { /* Comentarios ignorados */}
<YYINITIAL> [\"]           { yybegin(CADENA); text = "\""; }
<YYINITIAL> "$$"       { return new Symbol(sym.DOLLAR, yyline, yycolumn, yytext()); }

<YYINITIAL> {SPACE}        { /*Espacios en blanco, ignorados*/ }
<YYINITIAL> {ENTER}        { /*Saltos de linea, ignorados*/}
<YYINITIAL> .           { return new Symbol(sym.TEXTOHTML, yyline, yycolumn, yytext());}

<CADENA> {
        [\"]    { 
                    String tmp = text + "\""; 
                    text = ""; 
                    yybegin(YYINITIAL);  
                    return new Symbol(sym.CADENA, yychar,yyline,tmp); 
                }
        [\n]    {
                    String tmp = text; 
                    text = "";  
                    yybegin(YYINITIAL);
                    return new Symbol(sym.TEXTOHTML, yychar, yyline, tmp);
                }
        [^\"]   { text += yytext(); }
}
