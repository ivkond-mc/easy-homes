package ivkond.mc.mods.eh.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ivkond.mc.mods.eh.domain.HomeLocation;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HomeLocationGsonAdapterTest {
    private static final HomeLocation LOCATION =
            new HomeLocation("minecraft:overworld", 1.5, -64.0, 260.25, 30.0f, -10.5f);

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(HomeLocation.class, HomeLocationGsonAdapter.INSTANCE)
            .create();

    @Test
    void writeProducesExplicitJsonShape() throws IOException {
        StringWriter string = new StringWriter();
        try (JsonWriter writer = new JsonWriter(string)) {
            HomeLocationGsonAdapter.INSTANCE.write(writer, LOCATION);
        }

        JsonObject json = JsonParser.parseString(string.toString()).getAsJsonObject();
        assertEquals("minecraft:overworld", json.get("dimension").getAsString());
        assertEquals(1.5, json.get("x").getAsDouble());
        assertEquals(-64.0, json.get("y").getAsDouble());
        assertEquals(260.25, json.get("z").getAsDouble());
        assertEquals(30.0, json.get("rotX").getAsDouble());
        assertEquals(-10.5, json.get("rotY").getAsDouble());
        assertEquals(6, json.size());
    }

    @Test
    void readRestoresEveryField() throws IOException {
        String json = "{\"dimension\":\"minecraft:the_nether\",\"x\":2.5,\"y\":64.0,\"z\":-3.0,\"rotX\":90.0,\"rotY\":-170.25}";

        HomeLocation restored = HomeLocationGsonAdapter.INSTANCE.read(reader(json));

        assertEquals(new HomeLocation("minecraft:the_nether", 2.5, 64.0, -3.0, 90.0f, -170.25f), restored);
    }

    @Test
    void roundTripThroughAdapterEqualsOriginal() throws IOException {
        StringWriter string = new StringWriter();
        try (JsonWriter writer = new JsonWriter(string)) {
            HomeLocationGsonAdapter.INSTANCE.write(writer, LOCATION);
        }
        HomeLocation restored = HomeLocationGsonAdapter.INSTANCE.read(reader(string.toString()));

        assertEquals(LOCATION, restored);
    }

    @Test
    void roundTripThroughGsonEqualsOriginal() {
        HomeLocation restored = gson.fromJson(gson.toJson(LOCATION, HomeLocation.class), HomeLocation.class);

        assertEquals(LOCATION, restored);
    }

    @Test
    void readSkipsUnknownFutureFields() throws IOException {
        String json = "{\"dimension\":\"minecraft:overworld\",\"x\":1.0,\"y\":2.0,\"z\":3.0,"
                + "\"rotX\":0.0,\"rotY\":0.0,\"futureField\":{\"nested\":[1,2]}}";

        HomeLocation restored = HomeLocationGsonAdapter.INSTANCE.read(reader(json));

        assertEquals(new HomeLocation("minecraft:overworld", 1.0, 2.0, 3.0, 0.0f, 0.0f), restored);
    }

    @Test
    void readWithoutDimensionFails() throws IOException {
        IOException e = assertThrows(IOException.class,
                () -> HomeLocationGsonAdapter.INSTANCE.read(reader("{\"x\":1.0}")));

        assertTrue(e.getMessage().contains("missing dimension"));
    }

    @Test
    void readEmptyObjectFails() throws IOException {
        assertThrows(IOException.class, () -> HomeLocationGsonAdapter.INSTANCE.read(reader("{}")));
    }

    private static JsonReader reader(String json) {
        return new JsonReader(new StringReader(json));
    }
}
