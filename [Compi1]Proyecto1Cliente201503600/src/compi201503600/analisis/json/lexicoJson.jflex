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

    /*  Generamos un java_cup.symbol para guardar el tipo de token 
        encontrado */
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    /* Generamos un symbol para el tipo de token encontrado 
       junto con su valor */
    private Symbol symbol(int type, Object value) {
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

<YYINITIAL> "{"             { return symbol(sym.LLAVEIZQ); }
<YYINITIAL> "}"             { return symbol(sym.LLAVEDER); }
<YYINITIAL> "["             { return symbol(sym.CORCHIZQ); }
<YYINITIAL> "]"             { return symbol(sym.CORCHDER); }
<YYINITIAL> ":"             { return symbol(sym.DPUNTOS); }
<YYINITIAL> ","             { return symbol(sym.COMA); }

<YYINITIAL> "score"         { return symbol(sym.SCORE); }
<YYINITIAL> "clases"        { return symbol(sym.CLASES); }
<YYINITIAL> "variables"     { return symbol(sym.VARIABLES); }
<YYINITIAL> "metodos"       { return symbol(sym.METODOS); }
<YYINITIAL> "comentarios"   { return symbol(sym.COMENTARIOS); }
<YYINITIAL> "nombre"        { return symbol(sym.NOMBRE); }
<YYINITIAL> "tipo"          { return symbol(sym.TIPO); }
<YYINITIAL> "funcion"       { return symbol(sym.FUNCION); }
<YYINITIAL> "clase"         { return symbol(sym.CLASE); }
<YYINITIAL> "parametros"    { return symbol(sym.PARAMETROS); }
<YYINITIAL> "texto"         { return symbol(sym.TEXTO); }

<YYINITIAL> {ENTERO}       { return symbol(sym.ENTERO,yytext());}
<YYINITIAL> {DECIMAL}      { return symbol(sym.DECIMAL, yytext());}
<YYINITIAL> {ID}           { return symbol(sym.ID,yytext());}
<YYINITIAL> [\"]           { yybegin(CADENA); text = "";}

<YYINITIAL> {SPACE}        { /*Espacios en blanco, ignorados*/ }
<YYINITIAL> {ENTER}        { /*Saltos de linea, ignorados*/}
<YYINITIAL> .           { String errLex = "Error léxico : '"+yytext()+"' en la línea: "+(yyline+1)+" y columna: "+(yycolumn+1);
                            System.out.println(errLex);}

<CADENA> {
        [\"]    { 
                    String tmp = text; 
                    text = ""; 
                    yybegin(YYINITIAL);  
                    return symbol(sym.CADENA,tmp); 
                }
        [\n]    {
                    text = "";  
                    yybegin(YYINITIAL);
                    String errLex = "Error léxico : '"+yytext()+"' en la línea: "+(yyline+1)+" y columna: "+(yycolumn+1);
                    System.out.println(errLex);;
                }
        [^\"]   { text += yytext();}
}
