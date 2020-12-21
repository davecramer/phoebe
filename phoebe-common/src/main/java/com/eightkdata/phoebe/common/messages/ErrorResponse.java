/*
 * Copyright © 2015, 8Kdata Technology S.L.
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for any purpose, without fee, and without
 * a written agreement is hereby granted, provided that the above
 * copyright notice and this paragraph and the following two
 * paragraphs appear in all copies.
 *
 * In no event shall 8Kdata Technology S.L. be liable to any party
 * for direct, indirect, special, incidental, or consequential
 * damages, including lost profits, arising out of the use of this
 * software and its documentation, even if 8Kdata Technology S.L.
 * has been advised of the possibility of such damage.
 *
 * 8Kdata Technology S.L. specifically disclaims any warranties,
 * including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. the
 * software provided hereunder is on an “as is” basis, and
 * 8Kdata Technology S.L. has no obligations to provide
 * maintenance, support, updates, enhancements, or modifications.
 */


package com.eightkdata.phoebe.common.messages;

import static com.google.common.base.Preconditions.checkNotNull;

import com.eightkdata.phoebe.common.message.AbstractByteBufMessage;
import com.eightkdata.phoebe.common.message.MessageType;
import com.eightkdata.phoebe.common.util.ByteBufAllocatorUtil;
import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * The startup message that is sent to establish a new connection to a PostgreSQL server.
 * StartupMessages are always sent in SQL_ASCII encoding, as the database server may not have initialized yet the
 * {@code client_encoding}.
 *
 * @see <a href="http://www.postgresql.org/docs/9.4/interactive/protocol-message-formats.html">Message Formats</a>
 */
@Immutable
public class ErrorResponse extends AbstractByteBufMessage {
  private List<ErrorField> fields = new ArrayList<>();

    /**
     * toString()-type method to output information about this message's payload. Default
     * implementation does not print any payload information, override to do it. Call the
     * ToStringHelper.add(String, xxx) methods to always include param name information.
     *
     * @param toStringHelper the ToStringHelper where to write the payload information
     */
    @Override
    public void fillInPayloadInformation(MoreObjects.ToStringHelper toStringHelper) {
        getStatusIndicator();
        toStringHelper.add("fields", fields);
    }


    public enum StatusIndicator {
        /**
         * Severity the field contents are ERROR, FATAL, or PANIC (in an error message),
         * or WARNING, NOTICE, DEBUG, INFO, or LOG (in a notice message),
         * or a localized translation of one of these.
         */
        SEVERITY('S'),
        /**
         * SQLSTATE CODE
         */
        SQL_STATE('C'),
        /**
         *  Message: the primary human-readable error message. This should be accurate but terse (typically one line). Always present.
         *
         */
        MESSAGE('M');

        private final byte statusByte;

        StatusIndicator(char statusChar) {
            this.statusByte = (byte) statusChar;
        }

        public byte getStatusByte() {
            return statusByte;
        }

        public static ErrorResponse.StatusIndicator getByByte(byte b) {
            // A more elegant implementation would be a static Map with every instance mapping.
            // But we avoid that for simplicity in this case, avoid Map's overhead and avoid byte boxing/unboxing

            switch (b) {
                case 'S':   return SEVERITY;
                case 'C':   return SQL_STATE;
                case 'M':   return MESSAGE;
                default:    throw new IllegalArgumentException("Uknown StatusIndicator with byte '" + b + "'");
            }
        }
    }
    public ErrorResponse(@Nonnull ByteBuf byteBuf) {
        super(byteBuf);
    }

    @Override @Nonnull
    public MessageType getType() {
        return MessageType.ErrorResponse;
    }

    public void getStatusIndicator() {
        fields.addAll( ErrorField.decode(byteBuf));
    }

}
