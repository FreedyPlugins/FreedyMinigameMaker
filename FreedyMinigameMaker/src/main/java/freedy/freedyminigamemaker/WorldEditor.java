package freedy.freedyminigamemaker;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Bukkit;

public class WorldEditor {


    public void setBlockWE(String[] args) {
        com.sk89q.worldedit.LocalWorld world = BukkitUtil.getLocalWorld(Bukkit.getWorld(args[2]));
        com.sk89q.worldedit.Vector p1 = new com.sk89q.worldedit.Vector(Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
        com.sk89q.worldedit.Vector p2 = new com.sk89q.worldedit.Vector(Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
        CuboidRegion cube = new CuboidRegion(p1, p2);
        cube.setWorld(world);
        EditSession session = new EditSession(world, cube.getArea());
        try {
            if (args.length == 7) {
                session.setBlocks(cube, new BaseBlock(Integer.parseInt(args[6])));
            } else if (args.length == 8) {
                session.setBlocks(cube, new BaseBlock(Integer.parseInt(args[6]), Integer.parseInt(args[7])));
            }                                //session.setBlocks(cube, BLOCK HERE) <----- CAN ONLY PUT 1 BLOCK
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setBlocksWE(String[] args) {
        com.sk89q.worldedit.LocalWorld world = BukkitUtil.getLocalWorld(Bukkit.getWorld(args[2]));
        com.sk89q.worldedit.Vector p1 = new com.sk89q.worldedit.Vector(Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
        com.sk89q.worldedit.Vector p2 = new com.sk89q.worldedit.Vector(Integer.parseInt(args[6]), Integer.parseInt(args[7]), Integer.parseInt(args[8]));
        CuboidRegion cube = new CuboidRegion(p1, p2);
        cube.setWorld(world);
        EditSession session = new EditSession(world, cube.getArea());
        try {
            if (args.length == 10) {
                session.setBlocks(cube, new BaseBlock(Integer.parseInt(args[9])));
            } else if (args.length == 11) {
                session.setBlocks(cube, new BaseBlock(Integer.parseInt(args[9]), Integer.parseInt(args[10])));
            }

            //session.setBlocks(cube, BLOCK HERE) <----- CAN ONLY PUT 1 BLOCK
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
