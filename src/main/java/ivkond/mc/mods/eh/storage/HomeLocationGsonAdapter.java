package ivkond.mc.mods.eh.storage;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ivkond.mc.mods.eh.domain.HomeLocation;

import java.io.IOException;

/**
 * Explicit (de)serialization of {@link HomeLocation}. GSON bundled with MC 1.18.x
 * (2.8.x) cannot set record fields reflectively, so the JSON shape is spelled out.
 */
public final class HomeLocationGsonAdapter extends TypeAdapter<HomeLocation> {
    public static final HomeLocationGsonAdapter INSTANCE = new HomeLocationGsonAdapter();

    private HomeLocationGsonAdapter() {
    }

    @Override
    public void write(JsonWriter out, HomeLocation value) throws IOException {
        out.beginObject();
        out.name("dimension").value(value.dimension());
        out.name("x").value(value.x());
        out.name("y").value(value.y());
        out.name("z").value(value.z());
        out.name("rotX").value(value.rotX());
        out.name("rotY").value(value.rotY());
        out.endObject();
    }

    @Override
    public HomeLocation read(JsonReader in) throws IOException {
        String dimension = null;
        double x = 0, y = 0, z = 0;
        float rotX = 0, rotY = 0;
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "dimension" -> dimension = in.nextString();
                case "x" -> x = in.nextDouble();
                case "y" -> y = in.nextDouble();
                case "z" -> z = in.nextDouble();
                case "rotX" -> rotX = (float) in.nextDouble();
                case "rotY" -> rotY = (float) in.nextDouble();
                default -> in.skipValue();
            }
        }
        in.endObject();
        if (dimension == null) {
            throw new IOException("Malformed home location: missing dimension");
        }
        return new HomeLocation(dimension, x, y, z, rotX, rotY);
    }
}
