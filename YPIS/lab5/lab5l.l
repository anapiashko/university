%{

#include "lab5b.tab.h"
#include <stdio.h>

void err();
%}

%%
"("								{return(LEFT);}
")"								{return(RIGHT);}
","								{return(COMMA);}
";"								{return(SEMICOLON);}
[\'].[\']                 		{
							yylval.sval = malloc(strlen(yytext));
							strncpy(yylval.sval, yytext, strlen(yytext));
							yylval.sval[strlen(yytext)] = '\0';
									return(SYMBOL);
								}
[_a-zA-Z][a-zA-Z0-9_]*          {
							yylval.sval = malloc(strlen(yytext));
							strncpy(yylval.sval, yytext, strlen(yytext));
							yylval.sval[strlen(yytext)] = '\0';
									return(FUNC);
								}
[\"][a-zA-Z0-9_]+[\"]   		{
							yylval.sval = malloc(strlen(yytext));
							strncpy(yylval.sval, yytext, strlen(yytext));
							yylval.sval[strlen(yytext)] = '\0';
									return(STR);
								}
[ \n\t]+						{}
.					   			{err(); exit(0);}

%%

void err(){
	printf("Wrong input: %s", yytext);
}

yywrap () {
  return (0);
}
