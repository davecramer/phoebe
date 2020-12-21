package com.eightkdata.phoebe.common.messages;

import static com.eightkdata.phoebe.common.messages.ErrorField.FIXED_CHARSET;
import static com.eightkdata.phoebe.common.util.DecodingUtil.getCString;

import com.eightkdata.phoebe.common.FieldDescription;
import com.eightkdata.phoebe.common.message.AbstractByteBufMessage;
import com.eightkdata.phoebe.common.message.MessageType;
import com.eightkdata.phoebe.common.type.TypeFormat;
import com.eightkdata.phoebe.common.util.DecodingUtil;
import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public class RowDescription extends AbstractByteBufMessage {

  int numFields;
  List <FieldDescription> fieldDescriptions;
  public RowDescription(@Nonnull ByteBuf byteBuf) {
    super(byteBuf);
  }

  /**
   * toString()-type method to output information about this message's payload. Default
   * implementation does not print any payload information, override to do it. Call the
   * ToStringHelper.add(String, xxx) methods to always include param name information.
   *
   * @param toStringHelper the ToStringHelper where to write the payload information
   */
  @Override
  public void fillInPayloadInformation(MoreObjects.ToStringHelper toStringHelper) {

    numFields = byteBuf.readShort();

    fieldDescriptions = new ArrayList<>(numFields);
    for ( int i=0; i< numFields; i++){
      CharSequence charSequence = getCString(byteBuf, byteBuf.readerIndex(), FIXED_CHARSET);
      byteBuf.skipBytes(charSequence.length()+1);
      String name = charSequence.toString();
      int tableOid    = byteBuf.readInt();
      short attributeId = byteBuf.readShort();
      int typeId      = byteBuf.readInt();
      short typeLen     = byteBuf.readShort();
      int typeMod     = byteBuf.readInt();
      TypeFormat format      = byteBuf.readShort()==0?TypeFormat.TEXT:TypeFormat.BINARY;
      fieldDescriptions.add(new FieldDescription(name, tableOid, attributeId, typeId, typeLen, typeMod, format));
    }
    toStringHelper.add("fields", fieldDescriptions);
  }


  @Nonnull
  @Override
  public MessageType getType() {
    return MessageType.RowDescription;
  }
}
