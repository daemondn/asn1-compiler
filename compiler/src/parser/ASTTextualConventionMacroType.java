/* Generated By:JJTree: Do not edit this line. ASTTextualConventionMacroType.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTTextualConventionMacroType extends SimpleNode {
  public ASTTextualConventionMacroType(int id) {
    super(id);
  }

  public ASTTextualConventionMacroType(AsnParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(AsnParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ab758ae119cb2e5c7b2320c207866aef (do not edit this line) */
