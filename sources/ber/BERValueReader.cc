#include <ber/BERValueReader.hh>
#include <ber/BERBuffer.hh>

#include <type/VisibleStringType.hh>
#include <type/IntegerType.hh>
#include <type/BooleanType.hh>
#include <type/SequenceType.hh>

#include <common/Utils.hh>

namespace asn1
{

// Reads BOOLEAN value
void BERValueReader::readBoolean(Boolean& value, const BooleanType& type)
{
   if (_nestedReader)
      _nestedReader->readBoolean(value, type);
   else
   {
      TagType tag;
      PCType pc;
      CLType cl;
      int64_t length;
      _buffer.decodeIL(tag, pc, cl, length);

      _checkTagIsCorrect(pc, type);
      _checkTagTagging(tag, cl, BERBuffer::BOOLEAN_BERTYPE, type);

      if (length != 1)
         throw BERBufferException("BER " + type.toString() + " length must be equal to 1");

      BERBuffer::ValueType rawValue = _buffer.get();
      if (rawValue == 0xFF)
         value = true;
      else if (rawValue == 0)
         value = false;
      else
         throw BERBufferException("Unexpected content octets of BER " + type.toString());
   }
}

// Reads INTEGER value
void BERValueReader::readInteger(Integer& value, const IntegerType& type)
{
   if (_nestedReader)
      _nestedReader->readInteger(value, type);
   else
   {
      TagType tag;
      PCType pc;
      CLType cl;
      int64_t length;
      _buffer.setEnd(_buffer.decodeIL(tag, pc, cl, length));

      _checkTagIsCorrect(pc, type);
      _checkTagTagging(tag, cl, BERBuffer::INTEGER_BERTYPE, type);

      if (length == -1) // INTEGER must always have a definite length
         throw BERBufferException("Illegal BER " + type.toString() + " length");
      else if (length == 0)
      {
         value = 0LL;
         return;
      }

      bool isFirstByte = true;
      while (_buffer.current() < _buffer.end())
      {
         Integer b = _buffer.get();
         if (isFirstByte)
         {
            value = (b & 0x80) ? ~0LL : 0LL;
            isFirstByte = false;
         }

         value <<= 8;
         value |= static_cast<Integer>(b);
      }

      _buffer.clearEnd();

      // check received data
      type.checkType(value);
   }
}

// Reads OBJECT IDENTIFIER value
void BERValueReader::readObjectIdentifier(ObjectIdentifier& value, const ObjectIdentifierType& type)
{
}

// Reads NULL value
void BERValueReader::readNull()
{
   if (_nestedReader)
      _nestedReader->readNull();
   {
      TagType tag;
      PCType pc;
      CLType cl;
      int64_t length;
      _buffer.decodeIL(tag, pc, cl, length);

      if (tag != BERBuffer::NULL_BERTYPE)
         throw BERBufferException("BER NULL is expected");
      if (length != 0)
         throw BERBufferException("Length of BER NULL is expected to be equal to 0");
   }
}

// Reads OCTET STRING value
void BERValueReader::readOctetString(OctetString& value, const OctetStringType& type)
{
   if (_nestedReader)
      _nestedReader->readOctetString(value, type);
   else
   {
      TagType tag;
      PCType pc;
      CLType cl;
      _buffer.decodeIdentifierOctets(tag, pc, cl);

      _checkTagTagging(tag, cl, BERBuffer::OCTETSTRING_BERTYPE, type);

      _readOctetStringOctets(value, type);
   }
}

// Reads VISIBLE STRING value
void BERValueReader::readVisibleString(OctetString& value, const VisibleStringType& type)
{
   if (_nestedReader)
      _nestedReader->readVisibleString(value, type);
   else
   {
      TagType tag;
      PCType pc;
      CLType cl;
      _buffer.decodeIdentifierOctets(tag, pc, cl);

      _checkTagTagging(tag, cl, BERBuffer::VISIBLESTRING_BERTYPE, type);

      _readOctetStringOctets(value, type);
   }
}

// Reads SEQUENCE value
void BERValueReader::readSequenceBegin(const SequenceType& type)
{
   if (_nestedReader)
      _nestedReader->readSequenceBegin(type);
   else
   {
      TagType tag;
      PCType pc;
      CLType cl;
      int64_t length;
      _buffer.decodeIL(tag, pc, cl, length);

      // save position of the sequence end
      _sequenceEndPos = _buffer.current() + static_cast<BERBuffer::SizeType>(length);

      if (tag != BERBuffer::SEQUENCE_BERTYPE)
         throw BERBufferException("BER " + type.toString() + " is expected");
      if (pc != BERBuffer::CONSTRUCTED_OBJECTYPE)
         throw BERBufferException("BER " + type.toString() + " must be CONSTRUCTED");

      // create reader for nested operations
      _nestedReader = _prototype();
   }
}

bool BERValueReader::isSequenceEnd(const SequenceType& type)
{
   if (_nestedReader && _nestedReader->_nestedReader != NULL)
      return _nestedReader->isSequenceEnd(type);
   else
      return (_buffer.current() < _sequenceEndPos) ? false : true;
}

void BERValueReader::readSequenceEnd(const SequenceType& type)
{
   if (_nestedReader && _nestedReader->_nestedReader != NULL)
      _nestedReader->readSequenceEnd(type);
   else
   {
      delete _nestedReader;
      _nestedReader = NULL;

      if (_sequenceEndPos < _buffer.current())
         throw BERBufferException("More BER " + type.toString() + " items are expected");
   }
}

// Reads SEQUENCE OF value
void BERValueReader::readSequenceOfBegin(const SequenceType& type)
{
   readSequenceBegin(type);
}

bool BERValueReader::isSequenceOfEnd(const SequenceType& type)
{
   return isSequenceEnd(type);
}

void BERValueReader::readSequenceOfEnd(const SequenceType& type)
{
   readSequenceEnd(type);
}

// Reads SET value
void BERValueReader::readSetBegin()
{
}

void BERValueReader::readSetEnd()
{
}

// Reads SET OF value
void BERValueReader::readSetOfBegin()
{
}

void BERValueReader::readSetOfEnd()
{
}

// Reads EXPLICIT tag
void BERValueReader::readExplicitBegin(const Type& type)
{
   assert(type.hasTagNumber() && (type.hasExplicitTagging() || type.hasEmptyTagging()));

   if (_nestedReader)
      _nestedReader->readExplicitBegin(type);
   else
   {
      TagType tag;
      PCType pc;
      CLType cl;
      int64_t length;

      _buffer.decodeIL(tag, pc, cl, length);
      if (tag != type.tagNumber() || cl != type.tagClass())
         throw BERBufferException("BER " + type.toString() + " is expected");
      if (pc != BERBuffer::CONSTRUCTED_OBJECTYPE)
      {
         if (type.hasExplicitTagging() || type.hasEmptyTagging())
            throw BERBufferException("BER " + type.toString() + " must be CONSTRUCTED");
      }
   }
}

void BERValueReader::readExplicitEnd(const Type& type)
{
   assert(type.hasTagNumber() && (type.hasExplicitTagging() || type.hasEmptyTagging()));
}

// Returns prototype (new instance) of the reader
BERValueReader* BERValueReader::_prototype() const
{
   return new BERValueReader(_buffer);
}

// Reads and checks OCTET STRING value
void BERValueReader::_readOctetStringOctets(OctetString& value, const OctetStringType& type)
{
   // primitive decoding; TODO: support both primitive and constructed value
   BERBuffer::ContentType rawValue;
   _buffer.decodeContentOctets(rawValue);

   // assign data to temporary variable
   if (rawValue.size())
      value.assign(reinterpret_cast<OctetString::value_type*>(rawValue.data()), rawValue.size());

   // check received data
   type.checkType(value);
}

// Checks tag for correctness
void BERValueReader::_checkTagIsCorrect(PCType pc, const Type& type)
{
   if (type.hasTagNumber() && (type.hasExplicitTagging() || type.hasEmptyTagging()))
   {
      if (pc != BERBuffer::CONSTRUCTED_OBJECTYPE)
         throw BERBufferException("BER " + type.toString() + " must be CONSTRUCTED");
   }
   else
   {
      if (pc != BERBuffer::PRIMITIVE_OBJECTYPE)
         throw BERBufferException("BER " + type.toString() + " must be PRIMITIVE");
   }
}

// Checks tag for tagging (IMPLICIT, EXPLICIT, ...)
void BERValueReader::_checkTagTagging(TagType tag, CLType cl, TagType expectedTag, const Type& type)
{
   if (type.hasImplicitTagging())
   {
      if (type.hasTagNumber() && tag != type.tagNumber())
         throw BERBufferException("BER " + type.toString() + " is expected");
//      if (cl != BERBuffer::CONTEXT_CLASSTYPE)
//         throw BERBufferException("Implicitly defined tag must be context-specific");
   }
   else if (type.hasEmptyTagging() || type.hasExplicitTagging())
   {
      if (!type.hasTagNumber() && tag != expectedTag)
         throw BERBufferException("BER " + type.toString() + " is expected");
      else if (type.hasTagNumber() && tag != type.tagNumber())
         throw BERBufferException("BER " + type.toString() + " is expected");
   }
}

}