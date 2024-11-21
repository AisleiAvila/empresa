package util;

import com.dasad.empresa.util.LocalDateDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class LocalDateDeserializerTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    private LocalDateDeserializer localDateDeserializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        localDateDeserializer = new LocalDateDeserializer();
    }

    @Test
    void testDeserialize() throws IOException {
        String dateStr = "25/12/2020";
        when(jsonParser.getText()).thenReturn(dateStr);

        LocalDate result = localDateDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(LocalDate.of(2020, 12, 25), result);
    }
}