package com.eightkdata.phoebe.client.api;

import com.eightkdata.phoebe.client.SimpleQueryFlowHandler;
import com.eightkdata.phoebe.common.PostgresEncoding;
import com.eightkdata.phoebe.common.messages.ErrorResponse;

import java.nio.charset.Charset;

import javax.annotation.Nonnull;

public class SimpleQueryCommand implements SimpleQueryFlowHandler.Callback {

  private final String query;
  private final PostgresEncoding encoding;

  public SimpleQueryCommand(@Nonnull  String query,  @Nonnull PostgresEncoding encoding){
    this.query = query;
    this.encoding = encoding;
  }

  public String getQuery(){
    return query;
  }
  @Override
  public void onCompleted() {

  }

  @Override
  public void onErrorResponse(ErrorResponse errorResponse) {

  }
}
