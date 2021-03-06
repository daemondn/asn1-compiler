/* Generated By:JJTree: Do not edit this line. ASTTaggedType.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public class ASTTaggedType extends SimpleNode {

   /**
    * Tagging type of the type.
    */
   public static final int IMPLICIT = 0;
   public static final int EXPLICIT = 1;
   public static final int NO_TAGGING = 2;
   /**
    * Tag class of the type.
    */
   public static final int CONTEXT = 0;
   public static final int UNIVERSAL = 1;
   public static final int APPLICATION = 2;
   public static final int PRIVATE = 3;
   /* type tagging type */
   private int tm = NO_TAGGING;

   public ASTTaggedType(int id) {
      super(id);
   }

   public ASTTaggedType(AsnParser p, int id) {
      super(p, id);
   }

   public void setTaggingMethod(int tm) {
      this.tm = tm;
   }

   public boolean isImplicitTagging() {
      return tm == IMPLICIT;
   }

   public boolean isExplicitTagging() {
      return tm == EXPLICIT;
   }

   public boolean isNoTagging() {
      return tm == NO_TAGGING;
   }

   /** Accept the visitor. **/
   @Override
   public Object jjtAccept(AsnParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }
}
/* JavaCC - OriginalChecksum=03517d402d807129336575f3b5a8feaf (do not edit this line) */
