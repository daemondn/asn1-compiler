/* Generated By:JJTree: Do not edit this line. ASTSignedNumber.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public class ASTSignedNumber extends SimpleNode {

   private int n = 0;

   public ASTSignedNumber(int id) {
      super(id);
   }

   public ASTSignedNumber(AsnParser p, int id) {
      super(p, id);
   }

   public void setNumber(int n) {
      this.n = n;
   }

   public int getNumber() {
      return n;
   }

   /** Accept the visitor. **/
   public Object jjtAccept(AsnParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }
}
/* JavaCC - OriginalChecksum=d8326cebcbb6dd29a7b6d538f73d1d78 (do not edit this line) */
