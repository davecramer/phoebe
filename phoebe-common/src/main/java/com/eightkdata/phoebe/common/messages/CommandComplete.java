package com.eightkdata.phoebe.common.messages;

import com.eightkdata.phoebe.common.message.AbstractByteBufMessage;
import com.eightkdata.phoebe.common.message.MessageType;
import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public class CommandComplete extends AbstractByteBufMessage {

  public CommandComplete(@Nonnull ByteBuf byteBuf) {
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

  }

  @Nonnull
  @Override
  public MessageType getType() {
    return MessageType.CommandComplete;
  }
}
