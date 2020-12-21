/*
 * Copyright © 2015, 8Kdata Technologies, S.L.
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

package com.eightkdata.phoebe.client.api;

import org.junit.Test;

import static com.eightkdata.phoebe.common.PostgresEncoding.UTF8;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Unit tests for {@link PGSession}.
 *
 * All of the tests here assume trust based authentication, so make sure
 * that the DB instance you're running against is set up for that. See
 * {@link AuthenticationTest} for test that excercise the various
 * authentication protocols.
 */
public class PGSessionTest extends AbstractTest {


  @Override
  public void setUp() throws Exception {
    super.setUp();
  }

  @Test
  public void testClient() throws Throwable {
    client = new PGClient("localhost",5432);
    waiter.assertNotNull(session = client.connect());
    waiter.resume();
    session.start(new StartupCommand("test", "test","test", UTF8));
    waiter.await(300);
    waiter.resume();
    client.disconnect();
  }

    @Test
    public void testStart() throws Throwable {
        String username = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        String database = props.getProperty("db.name");
        session.start(new StartupCommand(username, password, database, UTF8) {
            public void onCompleted() {
                waiter.assertTrue(true);
                waiter.resume();
            }
        });
        waiter.await(5, SECONDS );
    }

    @Test
    public void testEmptyQuery() throws Throwable {
        testStart();
        session.query(new SimpleQueryCommand("select 1", UTF8) {
            @Override
            public void onCompleted() {
                waiter.assertTrue(true);
                waiter.resume();
            }
        });
        waiter.await(10, SECONDS);
    }


//    @Test
//    @Category({ExternalDatabase.class})
//    public void testSimpleQuery() throws Throwable {
//        testStart();
//        session.query(new SimpleQueryCommand("select current_timestamp;") {
//            @Override
//            public void onCompleted(ReadyForQuery message) {
//                waiter.assertTrue(message.getStatus() == ReadyForQuery.Status.IDLE);
//                waiter.resume();
//            }
//        });
//        waiter.await(5, SECONDS);
//    }

}
