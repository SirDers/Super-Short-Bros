package game;

public class ObjectFactory {
    public static PhysicsObject createObject(int type, int spawnX, int spawnY, int level) {
        switch (type) {
            case 0:
                return new Player(spawnX, spawnY, 0.75, 1.75, 1.0/128, 637.0/1920, 21.0/64, 7.0/16);
            case 1:
                return new Stella(spawnX, spawnY, 1, 1, level);
            case 2:
                return new Asteo(spawnX, spawnY, 0.99, 0.99, -3.0/64, 637.0/1920, 21.0/64, 7.0/16);
            case 3:
                return new Rasteo(spawnX, spawnY, 0.99, 0.99, -4.5/64, 637.0/1920, 21.0/64, 7.0/16);
            case 4:
                return new Masteo(spawnX, spawnY, 0.99, 0.99, -2.0/64, 637.0/1920, 21.0/64, 7.0/16);
            case 5:
                return new Soliseye(spawnX, spawnY, 0.99, 0.99, 0, Tiles.SIZE/8.0, 0, 0);
            default:
                throw new IllegalArgumentException("Unknown PhysicsObject type: " + type);
        }
    }
}