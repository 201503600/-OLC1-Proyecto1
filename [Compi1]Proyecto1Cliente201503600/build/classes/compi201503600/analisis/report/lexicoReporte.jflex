package compi201503600.analisis.report;

import java_cup.runtime.*;
import java.io.Reader;

%%

%{
    //Código de usuario
    String text = "";
%}
%public
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
<YYINITIAL> "="            { return symbol(sym.OPIGUAL);}
<YYINITIAL> "+="           { return symbol(sym.OPASIGSUMA);}
<YYINITIAL> "-="           { return symbol(sym.OPASIGRESTA);}
<YYINITIAL> "*="           { return symbol(sym.OPASIGMULT);}
<YYINITIAL> "++"           { return symbol(sym.OPINCREMENT);}
<YYINITIAL> "--"           { return symbol(sym.OPDECREMENT);}
<YYINITIAL> "("            { return symbol(sym.PARA);}
<YYINITIAL> ")"            { return symbol(sym.PARC);}
<YYINITIAL> ";"            { return symbol(sym.PCOMA);}
<YYINITIAL> ","            { return symbol(sym.COMA);}
<YYINITIAL> "."            { return symbol(sym.PUNTO);}
<YYINITIAL> "["            { return symbol(sym.CORA);}
<YYINITIAL> "]"            { return symbol(sym.CORC);}
<YYINITIAL> "+"            { return symbol(sym.OPSUMA);}
<YYINITIAL> "-"            { return symbol(sym.OPRESTA);}
<YYINITIAL> "*"            { return symbol(sym.OPMULT);}
<YYINITIAL> "/"            { return symbol(sym.OPDIV);}
<YYINITIAL> "%"            { return symbol(sym.OPMODULO);}
<YYINITIAL> "<"            { return symbol(sym.OPMEN);}
<YYINITIAL> ">"            { return symbol(sym.OPMAY);}
<YYINITIAL> "<="            { return symbol(sym.OPMENIGUAL);}
<YYINITIAL> ">="            { return symbol(sym.OPMAYIGUAL);}
<YYINITIAL> "=="            { return symbol(sym.OPCOMPARADOR);}
<YYINITIAL> "!="            { return symbol(sym.OPDISTINTO);}
<YYINITIAL> "&&"            { return symbol(sym.OPAND);}
<YYINITIAL> "||"            { return symbol(sym.OPOR);}
<YYINITIAL> "!"            { return symbol(sym.OPNOT);}

<YYINITIAL> "RESULT"       { return symbol(sym.RESULT);}
<YYINITIAL> "Score"        { return symbol(sym.SCORE);}
<YYINITIAL> "variables"    { return symbol(sym.VARIABLES);}
<YYINITIAL> "nombre"       { return symbol(sym.NOMBRE);}
<YYINITIAL> "tipo"         { return symbol(sym.TIPO); }
<YYINITIAL> "funcion"      { return symbol(sym.FUNCION); }
<YYINITIAL> "clase"        { return symbol(sym.CLASE); }
<YYINITIAL> "parametro"    { return symbol(sym.PARAMETRO); }
<YYINITIAL> "cantidad"     { return symbol(sym.CANTIDAD);}
<YYINITIAL> "metodos"      { return symbol(sym.METODOS);}
<YYINITIAL> "clases"       { return symbol(sym.CLASES);}
<YYINITIAL> "Entero"       { return symbol(sym.TINT);}
<YYINITIAL> "Decimal"      { return symbol(sym.TDOUBLE);}
<YYINITIAL> "Caracter"     { return symbol(sym.TCHAR);}
<YYINITIAL> "Texto"        { return symbol(sym.TSTRING);}
<YYINITIAL> "Booleano"     { return symbol(sym.TBOOL);}
<YYINITIAL> "PRINT"        { return symbol(sym.PRINT);}

<YYINITIAL> "html"      { return symbol(sym.HTML);}
<YYINITIAL> "head"      { return symbol(sym.HEAD);}
<YYINITIAL> "body"      { return symbol(sym.BODY);}
<YYINITIAL> "title"     { return symbol(sym.TITLE);}
<YYINITIAL> "h1"        { return symbol(sym.H1);}
<YYINITIAL> "h2"        { return symbol(sym.H2);}
<YYINITIAL> "h3"        { return symbol(sym.H3);}
<YYINITIAL> "h4"        { return symbol(sym.H4);}
<YYINITIAL> "h5"        { return symbol(sym.H5);}
<YYINITIAL> "h6"        { return symbol(sym.H6);}
<YYINITIAL> "table"     { return symbol(sym.TABLE);}
<YYINITIAL> "th"        { return symbol(sym.TH);}
<YYINITIAL> "td"        { return symbol(sym.TD);}
<YYINITIAL> "tr"        { return symbol(sym.TR);}
<YYINITIAL> "div"       { return symbol(sym.DIV);}
<YYINITIAL> "p"         { return symbol(sym.P);}
<YYINITIAL> "br"        { return symbol(sym.BR);}
<YYINITIAL> "hr"        { return symbol(sym.HR);}
<YYINITIAL> "color"     { return symbol(sym.COLOR);}
<YYINITIAL> "textcolor" { return symbol(sym.TEXTCOLOR);}
<YYINITIAL> "align"     { return symbol(sym.ALIGN);}
<YYINITIAL> "font"      { return symbol(sym.FONT);}

<YYINITIAL> {ENTERO}       { return symbol(sym.ENTERO,yytext());}
<YYINITIAL> {DECIMAL}      { return symbol(sym.DECIMAL, yytext());}
<YYINITIAL> {ID}           { return symbol(sym.ID,yytext());}
<YYINITIAL> {COMENTARIO}   { /* Comentarios ignorados */}
<YYINITIAL> [\"]           { yybegin(CADENA); text = "\""; }
<YYINITIAL> "$$"       { return symbol(sym.DOLLAR); }

<YYINITIAL> {SPACE}        { /*Espacios en blanco, ignorados*/ }
<YYINITIAL> {ENTER}        { /*Saltos de linea, ignorados*/}
<YYINITIAL> .           { return symbol(sym.TEXTOHTML, yytext());}

<CADENA> {
        [\"]    { 
                    String tmp = text + "\""; 
                    text = ""; 
                    yybegin(YYINITIAL);  
                    return symbol(sym.CADENA, tmp); 
                }
        [\n]    {
                    String tmp = text; 
                    text = "";  
                    yybegin(YYINITIAL);
                    return symbol(sym.TEXTOHTML, tmp);
                }
        [^\"]   { text += yytext(); }
}
