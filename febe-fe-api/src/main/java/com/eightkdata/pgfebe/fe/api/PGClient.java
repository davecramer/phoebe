// Copyright © 2015, 8Kdata Technologies, S.L.

package com.eightkdata.pgfebe.fe.api;

import com.eightkdata.pgfebe.common.exception.FeBeException;
import com.eightkdata.pgfebe.fe.decoder.BeMessageDecoder;
import com.eightkdata.pgfebe.fe.decoder.BeMessageProcessor;
import com.eightkdata.pgfebe.fe.encoder.FeMessageEncoder;
import com.eightkdata.pgfebe.fe.encoder.FeMessageProcessor;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * A client represents a server configuration and manages any shared resources for the server.
 */
@NotThreadSafe
public class PGClient {
    private static final long DEFAULT_CONNECT_TIMEOUT = 30;
    private static final TimeUnit DEFAULT_CONNECT_TIMEOUT_TIMEUNIT = TimeUnit.SECONDS;

    private final String host;
    private final int port;

    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    public PGClient(@Nonnull String host, int port) {
        checkArgument(!isNullOrEmpty(host), "host cannot be null or empty");
        checkArgument(port > 0, "port cannot be less than 1");
        this.host = host;
        this.port = port;
    }

    /**
     * Attempts to create a new session within a specified time limit.
     *
     * @return the session, or {@code null} if the time limit is reached.
     * @throws FeBeException if the session could not be created.
     */
    public @Nullable PGSession connect(long timeout, TimeUnit unit) throws FeBeException, InterruptedException {
        checkArgument(timeout > 0, "timeout cannot be less than 1");
        checkNotNull(unit, "timeUnit");

        final AtomicReference<Channel> channelRef = new AtomicReference<Channel>();

        new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ClientChannelHandlerInitializer())
                .connect(host, port)
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        channelRef.set(future.channel());
                    }
                })
                .await(timeout, unit);

        Channel channel = channelRef.get();
        return channel != null ? new PGSession(channel) : null;
    }

    /**
     * Attempts to create a new session using the default time limit of 30 seconds.
     *
     * @return the session, or {@code null} if the time limit is reached.
     * @throws FeBeException if the session could not be created.
     */
    public PGSession connect() throws FeBeException, InterruptedException {
        return connect(DEFAULT_CONNECT_TIMEOUT, DEFAULT_CONNECT_TIMEOUT_TIMEUNIT);
    }


    private static class ClientChannelHandlerInitializer extends ChannelInitializer<Channel> {
        @Override
        protected void initChannel(Channel channel) throws Exception {
            channel.pipeline()
                    .addLast("PGInboundMessageDecoder", new BeMessageDecoder())
                    .addLast("PGInboundMessageProcessor", new BeMessageProcessor())

                    .addLast("PGOutboundMessageEncoder", new FeMessageEncoder())
                    .addLast("PGOutboundMessageProcessor", new FeMessageProcessor());
        }
    }

}