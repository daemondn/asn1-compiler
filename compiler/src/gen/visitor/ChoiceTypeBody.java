package gen.visitor;

import gen.*;
import gen.utils.*;
import parser.*;

public class ChoiceTypeBody extends DoNothingASTVisitor implements ContentProvider {

   private CodeBuilder builder = new CodeBuilder();
   private final GeneratorContext context;
   private int nextId = 0;

   protected class SetGetHasDeclaration extends DoNothingASTVisitor implements ContentProvider {

      private CodeBuilder builder = new CodeBuilder();

      @Override
      public Object visit(ASTElementType node, Object data) {
         // setter
         builder.append(2, "void set_").
                 append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append("(const ");
         if (!VisitorUtils.visitChildsAndAccept(builder, node, new SimpleTypeName(),
                 new DefinedCPPTypeName())) {
            builder.append(GenerationUtils.asCPPToken(
                    VisitorUtils.queueGeneratedCode(node, context)));
         }

         builder.append("::ValueType& v) { _").
                 append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append(" = v;");
         builder.append(" _id = ").
                 append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).append("_ID;").
                 append(" }");
         builder.newLine();

         // getter
         builder.append(2, "const ");
         if (!VisitorUtils.visitChildsAndAccept(builder, node, new SimpleTypeName(),
                 new DefinedCPPTypeName())) {
            builder.append(GenerationUtils.asCPPToken(
                    VisitorUtils.queueGeneratedCode(node, context)));
         }
         builder.append("::ValueType& get_").
                 append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append("() const { return _").
                 append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append("; }");
         builder.newLine();

         // has
         builder.append(2, "bool has_");
         builder.append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append("_Choosen() const { return _id == ").
                 append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append("_ID").
                 append("; }");
         builder.newLine();

         builder.newLine();
         return data;
      }

      public String getContent() {
         return builder.toString();
      }

      public boolean hasValuableContent() {
         return true;
      }
   }

   protected class ValueDeclaration extends DoNothingASTVisitor implements ContentProvider {

      private CodeBuilder builder = new CodeBuilder();

      @Override
      public Object visit(ASTElementType node, Object data) {
         // member
         builder.append(2, "");
         if (!VisitorUtils.visitChildsAndAccept(builder, node, new SimpleTypeName(),
                 new DefinedCPPTypeName())) {
            builder.append(GenerationUtils.asCPPToken(
                    VisitorUtils.queueGeneratedCode(node, context)));
         }
         builder.append("::ValueType _").
                 append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append(";");
         builder.newLine();

         return data;
      }

      public String getContent() {
         return builder.toString();
      }

      public boolean hasValuableContent() {
         return true;
      }
   }

   protected class IdentifierValueDeclaration extends DoNothingASTVisitor implements ContentProvider {

      private CodeBuilder builder = new CodeBuilder();

      @Override
      public Object visit(ASTElementType node, Object data) {
         // member
         builder.append(3, "");
         builder.append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append("_ID = ").append(String.valueOf(++nextId)).append(",");
         builder.newLine();

         return data;
      }

      public String getContent() {
         return builder.toString();
      }

      public boolean hasValuableContent() {
         return true;
      }
   }

   public ChoiceTypeBody(final GeneratorContext context) {
      this.context = context;
   }

   @Override
   public Object visit(ASTBuiltinType node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTChoiceType node, Object data) {
      // write sequence value definition
      builder.newLine();
      builder.append(1, "class ChoiceValue_Type").newLine();
      builder.append(1, "{").newLine();
      builder.append(1, "public:").newLine();
      builder.newLine();

      builder.append(2, "explicit ChoiceValue_Type() : _id(__VALUE_NOT_DEFINED__) {}").newLine();
      builder.newLine();

      // write setters/getters
      VisitorUtils.visitChildsAndAccept(builder, node, new SetGetHasDeclaration());

      builder.append(1, "private:").newLine();
      builder.newLine();

      builder.append(2, "enum ChoiceValue_identifier").newLine();
      builder.append(2, "{").newLine();

      VisitorUtils.visitChildsAndAccept(builder, node, new IdentifierValueDeclaration());

      builder.append(3, "__VALUE_NOT_DEFINED__ = -1").newLine();
      builder.append(2, "};").newLine();
      builder.newLine();

      // values
      VisitorUtils.visitChildsAndAccept(builder, node, new ValueDeclaration());

      builder.newLine();

      builder.append(2, "ChoiceValue_identifier _id;").newLine();

      builder.append(1, "};").newLine();

      // write sequence value typedef
      builder.newLine();
      builder.append(1, "typedef ChoiceValue_Type ValueType;").newLine();

      builder.newLine();
      builder.append(1, "void read(ASN1ValueReader& reader, ").append("ValueType").
              append("& value) const;").newLine();
      builder.append(1, "void write(ASN1ValueWriter& writer, const ").append("ValueType").
              append("& value) const;").newLine();
      builder.newLine();

      builder.append("private:").newLine();
      builder.newLine();

      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTElementType node, Object data) {
      builder.append(1, "");

      if (!VisitorUtils.visitChildsAndAccept(builder, node, new SimpleTypeName(),
              new DefinedCPPTypeName())) {
         builder.append("typedef ").append(GenerationUtils.asCPPToken(
                 VisitorUtils.queueGeneratedCode(node, context))).
                 append(" ").append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append("_Type;").newLine();
         builder.append(1, GenerationUtils.asCPPToken(node.getFirstToken().toString())).
                 append("_Type");
      } else {
         VisitorUtils.prependDefinedGeneratedNode(node, context);
      }

      builder.append(" _").append(GenerationUtils.asCPPToken(node.getFirstToken().toString())).
              append("_Type;");
      builder.newLine();

      return data;
   }

   public String getContent() {
      return builder.toString();
   }

   public boolean hasValuableContent() {
      return !builder.toString().isEmpty();
   }
}
