/*
 * Copyright (c) 2015, 8Kdata Technology S.L.
 *
 * Permission to use, copy, modify, and distribute this software and its documentation for any purpose,
 * without fee, and without a written agreement is hereby granted, provided that the above copyright notice and this
 * paragraph and the following two paragraphs appear in all copies.
 *
 * IN NO EVENT SHALL 8Kdata BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
 * INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF 8Kdata HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * 8Kdata SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS,
 * AND 8Kdata HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 */


package com.eightkdata.phoebe.common.message;

import static com.google.common.base.Preconditions.checkNotNull;

import com.eightkdata.phoebe.common.util.ByteBufAllocatorUtil;
import com.eightkdata.phoebe.common.util.EncodingUtil;
import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public abstract class AbstractMessage implements Message {

    protected static ByteBuf encodeToByteBuf(@Nonnull ByteBufAllocator byteBufAllocator,
        @Nonnull Charset charset,
        String data){
        // Validate input arguments
        checkNotNull(data, "data");
        checkNotNull(charset, "charset");
        ByteBuf byteBuf = ByteBufAllocatorUtil.allocStringByteBuf(
            byteBufAllocator, EncodingUtil.lengthCString(data, charset) + 1
        );

        EncodingUtil.writeCString(byteBuf, data, charset);
        return byteBuf;
    }
    @Override
    public String toString() {
        MoreObjects.ToStringHelper toStringHelper = MoreObjects.toStringHelper(getType().name());
        fillInPayloadInformation(toStringHelper);

        return toStringHelper.toString();
    }

    /**
     * toString()-type method to output information about this message's payload.
     * Default implementation does not print any payload information, override to do it.
     * Call the ToStringHelper.add(String, xxx) methods to always include param name information.
     *
     * @param toStringHelper the ToStringHelper where to write the payload information
     */
    public abstract void fillInPayloadInformation(MoreObjects.ToStringHelper toStringHelper);

}
