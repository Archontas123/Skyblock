package org.mythofy.skyblock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.UUID;

public class Island {
    private final UUID ownerUUID;
    private UUID owner;
    private Location center;
    private int level;
    private int borderSize;

    private int size;
    private World world;
    private Location homeLocation;
    private boolean isPublic;


    public Island(UUID ownerUUID, Location homeLocation, int level, World world, int borderSize) {
        this.ownerUUID = ownerUUID;
        this.homeLocation = homeLocation;
        this.level = level;
        this.world = world;
        this.borderSize = borderSize;
    }

    public UUID getOwner() {
        return owner;
    }

    public Location getCenter() {
        return center;
    }

    public int getSize() {
        return size;
    }

    public World getWorld() {
        return world;
    }
    public Location getHomeLocation() {
        return homeLocation;
    }

    public void setHomeLocation(Location homeLocation) {
        this.homeLocation = homeLocation;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Location[] getBorderCorners() {
        Location[] corners = new Location[4];
        int halfSize = size / 2;
        corners[0] = center.clone().add(halfSize, 0, halfSize);
        corners[1] = center.clone().add(halfSize, 0, -halfSize);
        corners[2] = center.clone().add(-halfSize, 0, -halfSize);
        corners[3] = center.clone().add(-halfSize, 0, halfSize);
        return corners;
    }
    public int getLevel() {
        return level;
    }
    public void showBorder(Island island) {
        Location[] corners = island.getBorderCorners();
        int y = island.getWorld().getHighestBlockYAt(island.getCenter());
        for (Location corner : corners) {
            for (int x = Math.min(corners[0].getBlockX(), corners[2].getBlockX()); x <= Math.max(corners[0].getBlockX(), corners[2].getBlockX()); x++) {
                island.getWorld().getBlockAt(x, y, corner.getBlockZ()).setType(Material.GLOWSTONE);
            }
            for (int z = Math.min(corners[0].getBlockZ(), corners[2].getBlockZ()); z <= Math.max(corners[0].getBlockZ(), corners[2].getBlockZ()); z++) {
                island.getWorld().getBlockAt(corner.getBlockX(), y, z).setType(Material.GLOWSTONE);
            }
        }
    }
    public int getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
    }

}

