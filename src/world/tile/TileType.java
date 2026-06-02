package world.tile;

public enum TileType {
    AIR(0, false, false),
    PLAIN_BROWN(1, true, false),
    PLAIN_PURPLE(2, true, false),
    GRASS_1(3, true, false),
    GRASS_2(4, true, false),
    GRASS_3(5, true, false),
    GRASS_4(6, true, false),
    GRASS_5(7, true, false),
    GRASS_6(8, true, false),
    SPAWN(9, false, false),
    STELLA(10, false, false),
    ASTEO(11, false, false),
    ERASE_OBJECTS(12, false, false),
    RASTEO(13, false, false),
    MASTEO(14, false, false),
    SOLISEYE(15, false, false),
    FUTURE_1(16, true, false),
    FUTURE_2(17, true, false),
    FUTURE_3(18, true, false),
    FUTURE_4(19, true, false),
    FUTURE_5(20, true, false),
    FUTURE_6(21, true, false),
    FUTURE_7(22, true, false),
    FUTURE_8(23, true, false),
    FUTURE_9(24, true, false),
    SPIKE_1(25, true, true),
    SPIKE_2(26, true, true),
    SPIKE_3(27, true, true),
    SPIKE_4(28, true, true),
    GRASS2_1(29, true, false),
    GRASS2_2(30, true, false),
    GRASS2_3(31, true, false),
    GRASS2_4(32, true, false),
    GRASS2_5(33, true, false),
    GRASS2_6(34, true, false),
    GRASS2_7(35, true, false),
    GRASS2_8(36, true, false),
    GRASS2_9(37, true, false),
    GRASS2_MG(38, false, false),
    OBJECT_EDIT(39, false, false);

    private final int id;
    private final boolean solid;
    private final boolean spike;

    TileType(int id, boolean solid, boolean spike) {
        this.id = id;
        this.solid = solid;
        this.spike = spike;
    }

    public int getId() {
        return id;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isSpike() {
        return spike;
    }
}
