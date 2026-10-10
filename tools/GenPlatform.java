import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Generates the GameTest structure template data/easy_homes/structure/platform.nbt:
 * a WxD stone floor with air above, so mock players have ground to stand on.
 *
 * Usage:
 *   java GenPlatform.java dump <path-to-empty.nbt>   - print NBT tree of a template
 *   java GenPlatform.java gen <out.nbt> <w> <h> <d>  - write platform template
 */
public class GenPlatform {
    public static void main(String[] args) throws IOException {
        if ("dump".equals(args[0])) {
            try (var in = new DataInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(args[1]))))) {
                dump(in.readByte(), in.readUTF(), in, 0);
            }
        } else if ("gen".equals(args[0])) {
            int w = Integer.parseInt(args[2]), h = Integer.parseInt(args[3]), d = Integer.parseInt(args[4]);
            try (var out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(Path.of(args[1]).toFile())))) {
                writePlatform(out, w, h, d);
            }
            System.out.println("Written " + args[1] + " (" + w + "x" + h + "x" + d + ")");
        }
    }

    // ---------- writer ----------

    static void writePlatform(DataOutputStream out, int w, int h, int d) throws IOException {
        out.writeByte(10); // TAG_Compound
        out.writeUTF("");  // root name
        // size: list of int [w, h, d]
        out.writeByte(9); out.writeUTF("size"); out.writeByte(3); out.writeInt(3);
        out.writeInt(w); out.writeInt(h); out.writeInt(d);
        // entities: empty list
        out.writeByte(9); out.writeUTF("entities"); out.writeByte(10); out.writeInt(0);
        // blocks: list of compounds, one per floor block (list elements carry no tag/name prefix)
        out.writeByte(9); out.writeUTF("blocks"); out.writeByte(10); out.writeInt(w * d);
        for (int x = 0; x < w; x++) {
            for (int z = 0; z < d; z++) {
                // pos: list of int [x, 0, z]
                out.writeByte(9); out.writeUTF("pos"); out.writeByte(3); out.writeInt(3);
                out.writeInt(x); out.writeInt(0); out.writeInt(z);
                out.writeByte(3); out.writeUTF("state"); out.writeInt(0); // palette index 0 = stone
                out.writeByte(0); // TAG_End of compound
            }
        }
        // palette: single entry minecraft:stone
        out.writeByte(9); out.writeUTF("palette"); out.writeByte(10); out.writeInt(1);
        out.writeByte(8); out.writeUTF("Name"); out.writeUTF("minecraft:stone");
        out.writeByte(0);
        // DataVersion: 1.21 (3953) — newer versions upgrade automatically
        out.writeByte(3); out.writeUTF("DataVersion"); out.writeInt(3953);
        out.writeByte(0); // TAG_End of root
    }

    // ---------- dumper ----------

    static void dump(byte type, String name, DataInputStream in, int depth) throws IOException {
        System.out.println("  ".repeat(depth) + "'" + name + "' type=" + typeName(type));
        switch (type) {
            case 10 -> { // compound
                while (true) {
                    byte t = in.readByte();
                    if (t == 0) break;
                    dump(t, in.readUTF(), in, depth + 1);
                }
            }
            case 9 -> { // list
                byte elem = in.readByte();
                int len = in.readInt();
                System.out.println("  ".repeat(depth + 1) + "list of " + typeName(elem) + " x" + len);
                for (int i = 0; i < len; i++) dump(elem, "#" + i, in, depth + 1);
            }
            case 11 -> { // int array
                int len = in.readInt();
                List<Integer> vals = new ArrayList<>();
                for (int i = 0; i < len; i++) vals.add(in.readInt());
                System.out.println("  ".repeat(depth + 1) + vals);
            }
            case 8 -> System.out.println("  ".repeat(depth + 1) + "\"" + in.readUTF() + "\"");
            case 3 -> System.out.println("  ".repeat(depth + 1) + in.readInt());
            case 1 -> System.out.println("  ".repeat(depth + 1) + in.readByte());
            case 2 -> System.out.println("  ".repeat(depth + 1) + in.readShort());
            case 4 -> System.out.println("  ".repeat(depth + 1) + in.readLong());
            case 5 -> System.out.println("  ".repeat(depth + 1) + in.readFloat());
            case 6 -> System.out.println("  ".repeat(depth + 1) + in.readDouble());
            default -> throw new IllegalStateException("unsupported tag " + type);
        }
    }

    static String typeName(byte t) {
        Map<Byte, String> m = new LinkedHashMap<>();
        m.put((byte) 1, "byte"); m.put((byte) 2, "short"); m.put((byte) 3, "int"); m.put((byte) 4, "long");
        m.put((byte) 5, "float"); m.put((byte) 6, "double"); m.put((byte) 8, "string");
        m.put((byte) 9, "list"); m.put((byte) 10, "compound"); m.put((byte) 11, "int_array");
        return m.getOrDefault(t, "?" + t);
    }
}
