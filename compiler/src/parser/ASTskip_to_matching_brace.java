/* Generated By:JJTree: Do not edit this line. ASTskip_to_matching_brace.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTskip_to_matching_brace extends SimpleNode {
  public ASTskip_to_matching_brace(int id) {
    super(id);
  }

  public ASTskip_to_matching_brace(AsnParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(AsnParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=57322fbac5e95008649e7189a4a1a514 (do not edit this line) */
