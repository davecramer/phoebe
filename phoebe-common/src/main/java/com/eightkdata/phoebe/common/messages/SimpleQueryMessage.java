package com.eightkdata.phoebe.common.messages;

import static com.eightkdata.phoebe.common.messages.StartupMessage.FIXED_CHARSET;

import com.eightkdata.phoebe.common.message.AbstractByteBufMessage;
import com.eightkdata.phoebe.common.message.AbstractMessage;
import com.eightkdata.phoebe.common.message.MessageType;
import com.eightkdata.phoebe.common.util.ByteSize;
import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import javax.annotation.Nonnull;

public class SimpleQueryMessage extends AbstractByteBufMessage {

  String query;
  /**
   * toString()-type method to output information about this message's payload. Default
   * implementation does not print any payload information, override to do it. Call the
   * ToStringHelper.add(String, xxx) methods to always include param name information.
   *
   * @param toStringHelper the ToStringHelper where to write the payload information
   */
  @Override
  public void fillInPayloadInformation(MoreObjects.ToStringHelper toStringHelper) {
    toStringHelper.add("query", query);

  }
  public SimpleQueryMessage(ByteBuf byteBuf, String query){
    super(byteBuf);
    this.query = query;
  }

  static SimpleQueryMessage encode(@Nonnull ByteBufAllocator byteBufAllocator, String query){

    return new SimpleQueryMessage(encodeToByteBuf(byteBufAllocator, FIXED_CHARSET, query), query);

  }
  @Nonnull
  @Override
  public MessageType getType() {
    return MessageType.Query;
  }

  @Override
  public int size() {
    // Oddly enough, this message supports both text and binary data (currently only text is supported).
    // Due to this support, the reported length should be of the real content,
    // excluding the null terminator byte used to mark the end of text data.

    return super.size() - ByteSize.BYTE;
  }
}
