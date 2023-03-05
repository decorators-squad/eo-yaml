parser grammar YamlParser;

options {
    tokenVocab=YamlLexer;
}

yamlDocument
   : DOCUMENT_START? yaml (DOCUMENT_START yaml)*
   ;

yaml
  : sequence
  | mapping
  | scalar
  ;

mapping
  : scalar COLON scalar (scalar COLON scalar)*
  ;

sequence
  : DASH yaml (DASH yaml)*
  ;

scalar
  : literal
  ;

literal
	:	IntegerLiteral
	|	FloatingPointLiteral
	|	BooleanLiteral
	|	CharacterLiteral
	|	StringLiteral
	|	NullLiteral
	;
