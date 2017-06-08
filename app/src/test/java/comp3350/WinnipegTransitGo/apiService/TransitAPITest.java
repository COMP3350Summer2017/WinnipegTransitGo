package comp3350.WinnipegTransitGo.apiService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransitAPITest {

    @Test
    public void getAPI() throws Exception {
        TransitAPIProvider api = TransitAPI.getAPI("");
        assertNotNull("Returned API provider cannot be null", api);
    }

}