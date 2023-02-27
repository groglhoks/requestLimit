package ecom.market.limit.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ecom.market.limit.controllers.ItemsController;

@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = { 
        "ecom.market.limit.requestCount = 5",
        "ecom.market.limit.minuteInterval = 1"
    }
    )
public class ControllerTest {

    @Autowired
	private ItemsController itemsController;

    @Value(value="${local.server.port}")
	private int port;

    @Value(value="${ecom.market.limit.requestCount}")
    private int requestCount;

    @Autowired
	private TestRestTemplate restTemplate;

    @Test
	public void contextLoads() throws Exception {
		assertThat(itemsController).isNotNull();
	}

    @Test
	public void outOfLimitBadGetway() throws Exception {

        String url = "http://localhost:" + port + "/getItem";

        for (int i = 0; i < requestCount; i++) {
            assertThat(this.restTemplate.getForObject(url, String.class)).isEqualTo("{}");    
        }

        // next request should be out of limit 
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class); 
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);

	} 
}
