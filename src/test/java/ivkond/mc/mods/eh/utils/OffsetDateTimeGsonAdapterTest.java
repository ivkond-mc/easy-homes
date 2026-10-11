package ivkond.mc.mods.eh.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OffsetDateTimeGsonAdapterTest {
    private static final OffsetDateTime TIME =
            OffsetDateTime.of(2026, 10, 11, 12, 34, 56, 123_456_789, ZoneOffset.ofHoursMinutes(5, 30));

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(OffsetDateTime.class, OffsetDateTimeGsonAdapter.INSTANCE)
            .create();

    @Test
    void serializeProducesIsoOffsetString() {
        JsonElement json = OffsetDateTimeGsonAdapter.INSTANCE.serialize(TIME, OffsetDateTime.class, null);

        JsonPrimitive primitive = assertInstanceOf(JsonPrimitive.class, json);
        assertTrue(primitive.isString());
        assertEquals(TIME.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), primitive.getAsString());
    }

    @Test
    void deserializeParsesIsoOffsetString() {
        String iso = TIME.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        OffsetDateTime parsed = OffsetDateTimeGsonAdapter.INSTANCE
                .deserialize(new JsonPrimitive(iso), OffsetDateTime.class, null);

        assertEquals(TIME, parsed);
    }

    @Test
    void roundTripPreservesInstantAndOffset() {
        for (ZoneOffset offset : new ZoneOffset[]{
                ZoneOffset.UTC, ZoneOffset.ofHours(14), ZoneOffset.ofHoursMinutes(-5, -30)
        }) {
            OffsetDateTime original = OffsetDateTime.of(2024, 2, 29, 23, 59, 59, 0, offset);
            JsonElement json = OffsetDateTimeGsonAdapter.INSTANCE.serialize(original, OffsetDateTime.class, null);
            OffsetDateTime restored = OffsetDateTimeGsonAdapter.INSTANCE
                    .deserialize(json, OffsetDateTime.class, null);

            assertEquals(original, restored);
        }
    }

    @Test
    void roundTripThroughGsonHolder() {
        Holder holder = new Holder();
        holder.time = TIME;

        Holder restored = gson.fromJson(gson.toJson(holder), Holder.class);

        assertEquals(TIME, restored.time);
    }

    @Test
    void nullFieldIsOmittedByDefaultGsonAndReadsBackAsNull() {
        Holder holder = new Holder();

        String json = gson.toJson(holder);
        assertFalse(json.contains("time"));

        Holder restored = gson.fromJson("{}", Holder.class);
        assertNull(restored.time);
    }

    @Test
    void malformedValueFailsDeserialization() {
        // The adapter parses verbatim, so a bad value surfaces as a parse
        // failure (Gson does not always wrap adapter exceptions on read)
        assertThrows(DateTimeParseException.class, () -> OffsetDateTimeGsonAdapter.INSTANCE
                .deserialize(new JsonPrimitive("not a date"), OffsetDateTime.class, null));
    }

    @SuppressWarnings("unused")
    private static final class Holder {
        OffsetDateTime time;
    }
}
