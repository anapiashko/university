%{

#include <stdio.h>

void yyerror (char const *);
  
%}
%union
{
	char *sval;
}

%token LEFT
%token RIGHT
%token <sval> FUNC
%token <sval> SYMBOL
%token <sval> STR
%token SEMICOLON
%token COMMA

%start functions
%%

functions:				| functions function
					;
function:          		FUNC LEFT {printf("\nProcedure name: %s\n", $1);}
					variables
					RIGHT
					SEMICOLON
 					;
variables:				/* empty */
					| vars identifier
					;
vars:          			/* empty */
					| vars variable
					;
variable:        			identifier COMMA
					;
identifier:				SYMBOL {printf("Single character : %s\n", $1);}
					| STR {printf("Constant string : %s\n", $1);}
					;
%%

void yyerror (char const *s)
{
	printf("Error: %s\n", s);
}


main (void) {
	printf("Source code: ");
    	yyparse();
    return 0;
}
