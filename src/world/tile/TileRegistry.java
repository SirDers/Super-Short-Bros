package world.tile;

import java.util.HashMap;
import java.util.Map;

public final class TileRegistry {
    private static final Map<Integer, TileType> BY_ID = new HashMap<>();

    static {
        for (TileType type : TileType.values()) {
            BY_ID.put(type.getId(), type);
        }
    }

    private TileRegistry() {
    }

    public static TileType byId(int id) {
        return BY_ID.getOrDefault(id, TileType.AIR);
    }
}
