package accounts;

import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import static org.junit.Assert.assertEquals;

public class TransferControllerTest {

    public static class TestSparkApplication implements SparkApplication {
        @Override
        public void init() {
            new TransferController();
        }
    }

    @ClassRule
    public static SparkServer<TestSparkApplication> testServer = new SparkServer<>(TransferControllerTest.TestSparkApplication.class, 4567);

    @Before
    public void setBalances() throws Exception {
        PostMethod balance1 = testServer.post("/account", "{\"id\":1,\"balance\":100}", false);
        PostMethod balance2 = testServer.post("/account", "{\"id\":2,\"balance\":100}", false);

        testServer.execute(balance1);
        testServer.execute(balance2);
    }

    @Test
    public void testTransfer() throws Exception {
        PostMethod transfer12 = testServer.post("/transfer", "{\"sourceId\":1,\"targetId\":2,\"amount\":50}", false);
        PostMethod transfer21 = testServer.post("/transfer", "{\"sourceId\":2,\"targetId\":1,\"amount\":30}", false);

        testServer.execute(transfer12);
        testServer.execute(transfer21);

        GetMethod balance1 = testServer.get("/balance/1", false);
        GetMethod balance2 = testServer.get("/balance/2", false);

        HttpResponse httpResponse = testServer.execute(balance1);
        HttpResponse httpResponse2 = testServer.execute(balance2);

        assertEquals(200, httpResponse.code());
        assertEquals(200, httpResponse2.code());

        assertEquals("80", new String(httpResponse.body()));
        assertEquals("120", new String(httpResponse2.body()));
    }

}
