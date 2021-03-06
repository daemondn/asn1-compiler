package gen.visitor;

import gen.*;
import gen.utils.*;
import parser.*;

public class ChoiceTypeReadWriteDefinition extends DoNothingASTVisitor implements ContentProvider,
        ConstantsForGeneration {

   private CodeBuilder builder = new CodeBuilder();
   private final GeneratorContext context;
   private boolean isFirstWriteIf = true;
   private boolean isFirstAssertOr = true;
   private boolean isFirstReadIf = true;
   private boolean isFirstComma = true;

   public ChoiceTypeReadWriteDefinition(final GeneratorContext context) {
      this.context = context;
   }

   protected class TypeList extends DoNothingASTVisitor implements ContentProvider {

      private CodeBuilder builder = new CodeBuilder();

      @Override
      public Object visit(ASTElementType node, Object data) {
         if (isFirstComma) {
            isFirstComma = false;
         } else {
            builder.append(" + \", \"");
         }

         builder.append(" + ").append("_").
                 append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append("_Type.toString()");

         return data;
      }

      public String getContent() {
         return builder.toString();
      }

      public boolean hasValuableContent() {
         return true;
      }
   }

   protected class ReadDefinition extends DoNothingASTVisitor implements ContentProvider {

      private CodeBuilder builder = new CodeBuilder();

      @Override
      public Object visit(ASTElementType node, Object data) {
         if (isFirstReadIf) {
            builder.append(1, "");
            isFirstReadIf = false;
         } else {
            builder.append(1, "else ");
         }

         builder.append("if (choosenType == &_").
                 append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append("_Type)").newLine();

         VisitorUtils.visitNodeAndAccept(builder, node,
                 new SetOrSequenceReadWriteDefinition.ReadDefinition());
         return data;
      }

      public String getContent() {
         return builder.toString();
      }

      public boolean hasValuableContent() {
         return true;
      }
   }

   protected class AssertsForWriteDefinition extends DoNothingASTVisitor
           implements ContentProvider {

      private CodeBuilder builder = new CodeBuilder();

      public AssertsForWriteDefinition() {
         builder.append(1, "assert(");
      }

      @Override
      public Object visit(ASTElementType node, Object data) {
         if (isFirstAssertOr) {
            builder.append("");
            isFirstAssertOr = false;
         } else {
            builder.append(" || ");
         }

         builder.append("value.has_").
                 append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append("_Choosen()");

         return data;
      }

      public String getContent() {
         return builder.append(");").newLine().toString();
      }

      public boolean hasValuableContent() {
         return true;
      }
   }

   protected class WriteDefinition extends DoNothingASTVisitor implements ContentProvider {

      private CodeBuilder builder = new CodeBuilder();

      @Override
      public Object visit(ASTElementType node, Object data) {
         if (isFirstWriteIf) {
            builder.append(1, "");
            isFirstWriteIf = false;
         } else {
            builder.append(1, "else ");
         }

         builder.append("if (value.has_").
                 append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append("_Choosen())").newLine();

         // write code for type reading
         builder.append(2, "_").append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append("_Type").append(".write(writer, value.get_").
                 append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append("());").newLine();

         return data;
      }

      public String getContent() {
         return builder.toString();
      }

      public boolean hasValuableContent() {
         return true;
      }
   }

   @Override
   public Object visit(ASTBuiltinType node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTTaggedType node, Object data) {
      if (!VisitorUtils.visitChildsAndAccept(null, node, new SimpleTypeName(),
              new DefinedCPPTypeName())) {
         final CodeBuilder uniqueName = new CodeBuilder();
         VisitorUtils.visitChildsAndAccept(uniqueName, node, new UniqueNameProducer());
         if (context.hasExternalized(uniqueName.toString())) {
            // TODO: hack, need to understand (I don't remember) why declaration generator generates
            // also methods definitions. See queueGeneratedCode method.
            return null;
         }

         context.setTypeName(uniqueName.toString());
      }

      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTChoiceType node, Object data) {
      builder.newLine();
      builder.append("void ").append(GenerationUtils.asCPPToken(context.getTypeName())).
              append("::read(ASN1ValueReader& reader, ").append("ValueType").
              append("& value) const").newLine();
      builder.append("{").newLine();
      builder.append(1, asn1NameSpace).append("Type* choosenType = NULL;").newLine();
      builder.append(1, "reader.readChoice(*this, &choosenType);").newLine();
      builder.newLine();

      VisitorUtils.visitChildsAndAccept(builder, node, new ReadDefinition());

      builder.append(1, "else").newLine();
      builder.append(1, "{").newLine();
      builder.append(2, "throw ASN1Exception(\"Expected \" + toString() + \" must be one of: \"").
              newLine();
      builder.append(3, "");
      VisitorUtils.visitChildsAndAccept(builder, node, new TypeList());
      builder.append(");").newLine();
      builder.append(1, "}").newLine();

      builder.append("}").newLine();
      builder.newLine();

      builder.append("void ").append(GenerationUtils.asCPPToken(context.getTypeName())).
              append("::write(ASN1ValueWriter& writer, const ").append("ValueType").
              append("& value) const").newLine();
      builder.append("{").newLine();

      VisitorUtils.visitChildsAndAccept(builder, node, new AssertsForWriteDefinition());
      builder.newLine();
      VisitorUtils.visitChildsAndAccept(builder, node, new WriteDefinition());

      builder.append("}").newLine();

      builder.newLine();
      return node.childrenAccept(this, data);
   }

   public String getContent() {
      return builder.toString();
   }

   public boolean hasValuableContent() {
      return true;
   }
}
