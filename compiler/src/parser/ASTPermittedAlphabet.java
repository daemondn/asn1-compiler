/* Generated By:JJTree: Do not edit this line. ASTPermittedAlphabet.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTPermittedAlphabet extends SimpleNode {
  public ASTPermittedAlphabet(int id) {
    super(id);
  }

  public ASTPermittedAlphabet(AsnParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(AsnParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=2027dbc5c18c299a6092d2331568d836 (do not edit this line) */
