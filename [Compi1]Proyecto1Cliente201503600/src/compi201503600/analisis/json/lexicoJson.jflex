package compi201503600.analisis.json;

import java_cup.runtime.*;
import java.io.Reader;

%%

%{
    //Código de usuario
    String text = "";
%}

%cup
%class ScannerLexJson
%line
%column
%state YYINITIAL
%state CADENA
%ignorecase

%{

    /*  Generamos un java_cup.Symbol para guardar el tipo de token 
        encontrado */
    private Symbol Symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    /* Generamos un Symbol para el tipo de token encontrado 
       junto con su valor */
    private Symbol Symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

//expresiones

ENTERO  = [0-9]+   
DECIMAL = [0-9]+.[0-9]+
ID      = [A-Za-zñÑ][_0-9A-Za-zñÑ]*

SPACE   = [\ \r\t\f\t]
ENTER   = [ \ \n]

%%

<YYINITIAL> "{"             { return new Symbol(sym.LLAVEIZQ, yyline, yycolumn, yytext()); }
<YYINITIAL> "}"             { return new Symbol(sym.LLAVEDER, yyline, yycolumn, yytext()); }
<YYINITIAL> "["             { return new Symbol(sym.CORCHIZQ, yyline, yycolumn, yytext()); }
<YYINITIAL> "]"             { return new Symbol(sym.CORCHDER, yyline, yycolumn, yytext()); }
<YYINITIAL> ":"             { return new Symbol(sym.DPUNTOS, yyline, yycolumn, yytext()); }
<YYINITIAL> ","             { return new Symbol(sym.COMA, yyline, yycolumn, yytext()); }

<YYINITIAL> "score"         { return new Symbol(sym.SCORE, yyline, yycolumn, yytext()); }
<YYINITIAL> "clases"        { return new Symbol(sym.CLASES, yyline, yycolumn, yytext()); }
<YYINITIAL> "variables"     { return new Symbol(sym.VARIABLES, yyline, yycolumn, yytext()); }
<YYINITIAL> "metodos"       { return new Symbol(sym.METODOS, yyline, yycolumn, yytext()); }
<YYINITIAL> "comentarios"   { return new Symbol(sym.COMENTARIOS, yyline, yycolumn, yytext()); }
<YYINITIAL> "nombre"        { return new Symbol(sym.NOMBRE, yyline, yycolumn, yytext()); }
<YYINITIAL> "tipo"          { return new Symbol(sym.TIPO, yyline, yycolumn, yytext()); }
<YYINITIAL> "funcion"       { return new Symbol(sym.FUNCION, yyline, yycolumn, yytext()); }
<YYINITIAL> "clase"         { return new Symbol(sym.CLASE, yyline, yycolumn, yytext()); }
<YYINITIAL> "parametros"    { return new Symbol(sym.PARAMETROS, yyline, yycolumn, yytext()); }
<YYINITIAL> "texto"         { return new Symbol(sym.TEXTO, yyline, yycolumn, yytext()); }

<YYINITIAL> {ENTERO}       { return new Symbol(sym.ENTERO, yyline, yycolumn,yytext());}
<YYINITIAL> {DECIMAL}      { return new Symbol(sym.DECIMAL, yyline, yycolumn, yytext());}
<YYINITIAL> {ID}           { return new Symbol(sym.ID, yyline, yycolumn,yytext());}
<YYINITIAL> [\"]           { yybegin(CADENA); text = "\"";}

<YYINITIAL> {SPACE}        { /*Espacios en blanco, ignorados*/ }
<YYINITIAL> {ENTER}        { /*Saltos de linea, ignorados*/}
<YYINITIAL> .           { String errLex = "Error léxico : '"+yytext()+"' en la línea: "+(yyline+1)+" y columna: "+(yycolumn+1);
                            System.out.println(errLex);}

<CADENA> {
        [\"]    { 
                    String tmp = text + "\""; 
                    text = ""; 
                    yybegin(YYINITIAL);  
                    return new Symbol(sym.CADENA, yychar,yyline,tmp); 
                }
        [\n]    {
                    text = "";  
                    yybegin(YYINITIAL);
                    String errLex = "Error léxico : '"+yytext()+"' en la línea: "+(yyline+1)+" y columna: "+(yycolumn+1);
                    System.out.println(errLex);;
                }
        [^\"]   { text += yytext();}
}
