/* Generated By:JJTree: Do not edit this line. ASTMacroReference.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTMacroReference extends SimpleNode {
  public ASTMacroReference(int id) {
    super(id);
  }

  public ASTMacroReference(AsnParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(AsnParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=2f7f5fba622beed6e192bd552f7e23c9 (do not edit this line) */
