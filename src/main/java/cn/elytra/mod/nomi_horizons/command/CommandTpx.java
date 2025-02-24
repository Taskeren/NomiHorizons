package cn.elytra.mod.nomi_horizons.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.NotNull;

public class CommandTpx extends CommandBase {

    public CommandTpx() {
    }

    @Override
    @NotNull
    public String getName() {
        return "tpx";
    }

    @Override
    @NotNull
    public String getUsage(@NotNull ICommandSender sender) {
        return "command.tpx.usage";
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String @NotNull [] args) throws CommandException {
        EntityPlayerMP current = getCommandSenderAsPlayer(sender);

        switch(args.length) {
            case 1:
                var target = getPlayer(server, sender, args[0]);
                transferPlayerToAnother(current, target);
                sender.sendMessage(new TextComponentTranslation("command.tpx.success", target.getDisplayName()));
                return;
            case 2:
                var target1 = getPlayer(server, sender, args[0]);
                var target2 = getPlayer(server, sender, args[1]);
                transferPlayerToAnother(target1, target2);
                sender.sendMessage(new TextComponentTranslation("command.tpx.success.other", target1.getDisplayName(), target2.getDisplayName()));
                return;
            default:
                sender.sendMessage(new TextComponentTranslation("command.tpx.usage"));
                return;
        }
    }

    private static void transferPlayerToAnother(EntityPlayerMP toTeleport, EntityPlayerMP teleportTo) {
        var teleport = new TeleportExecutor(teleportTo.posX, teleportTo.posY, teleportTo.posZ, teleportTo.dimension);
        teleport.teleport(toTeleport);
    }

    // https://github.com/FTBTeam/FTB-Library-Legacy/blob/1.12/src/main/java/com/feed_the_beast/ftblib/lib/math/TeleporterDimPos.java
    private static class TeleportExecutor implements ITeleporter {

        public double posX, posY, posZ;
        public int dimId;

        public TeleportExecutor(double posX, double posY, double posZ, int dimId) {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.dimId = dimId;
        }

        @Override
        public void placeEntity(World world, Entity entity, float yaw) {
            entity.motionX = entity.motionY = entity.motionZ = 0.0D;
            entity.fallDistance = 0F;

            if(entity instanceof EntityPlayerMP mp && mp.connection != null) {
                mp.connection.setPlayerLocation(posX, posY, posZ, yaw, entity.rotationPitch);
            } else {
                entity.setLocationAndAngles(posX, posY, posZ, yaw, entity.rotationPitch);
            }
        }

        public Entity teleport(Entity entity) {
            if(entity == null || entity.world.isRemote) {
                return entity;
            }

            if(dimId != entity.dimension) {
                return entity.changeDimension(dimId, this);
            }

            placeEntity(entity.world, entity, entity.rotationPitch);
            return entity;
        }
    }
}
