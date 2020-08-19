# microJavaCompiler

This was a project done as part of compilers course at the University of Belgrade, faculty of Electrical engineering.

## Description

MicroJava compiler has 4 basic functionalities: lexical analysis, syntax analysis, semantic analysis, and bytecode generation.

**Lexical analyzer** should recognize language lexemes and retrieve tokens, which will be processed in syntax analysis.

**Syntax analyzer** checks if the extracted tokens from source code can form sentences that are gramatically correct.

**Semantic analyzer** is formed based on abstract syntax tree that was created as a result of syntax analysis. Semantic analysis is done by implementing methods for visiting nodes of abstract syntax tree.

**Bytecode generator** compiles programs that are syntaxly and semantically correct into an executable form for a microJava virtual machine. Code generation is implemented in a similar way as semantic analysis, by implementing methods that visit nodes.


## Configurations
The project is implemented in Java, using modules Cup and JFlex.

**LexerGenerator**:  `-d src\rs\ac\bg\etf\pp1 spec\mjlexer.flex`

**ParserGenerator**: `-destdir rs\ac\bg\etf\pp1 -dump_states -parser MJParser -ast rs.ac.bg.etf.pp1.ast -buildtree ..\spec\mjparser.cup`

**Compile MJ Source**: `test\program.mj test\program.obj`

**Run**: `-debug test\program.obj`

**Disasm**:  `test\program.obj`


## Test examples

**primer1.mj** shows almost every error that semantic analysis will find

**primer2.mj** shows almost every correct substitution that the written grammar will recognize

