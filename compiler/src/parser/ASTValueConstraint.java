/* Generated By:JJTree: Do not edit this line. ASTValueConstraint.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTValueConstraint extends SimpleNode {
  public ASTValueConstraint(int id) {
    super(id);
  }

  public ASTValueConstraint(AsnParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(AsnParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=72308ff9cb2f490e9c4e575e9cc9a61e (do not edit this line) */
