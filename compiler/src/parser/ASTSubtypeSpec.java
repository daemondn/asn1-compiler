/* Generated By:JJTree: Do not edit this line. ASTSubtypeSpec.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTSubtypeSpec extends SimpleNode {
  public ASTSubtypeSpec(int id) {
    super(id);
  }

  public ASTSubtypeSpec(AsnParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(AsnParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=8920e5b15ae17aa830a48100ee1ef7b2 (do not edit this line) */
