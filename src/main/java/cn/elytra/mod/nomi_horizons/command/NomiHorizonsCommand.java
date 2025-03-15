package cn.elytra.mod.nomi_horizons.command;

import cn.elytra.mod.nomi_horizons.NomiHorizons;
import cn.elytra.mod.nomi_horizons.config.NomiHorizonsConfigV2;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.server.command.CommandTreeBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NomiHorizonsCommand extends CommandTreeBase {

    private final ICommand helpCommand;

    public NomiHorizonsCommand() {
        addSubcommand(this.helpCommand = new HelpCommand());
        addSubcommand(new ReloadCommand());
    }

    @Nullable
    @Override
    public ICommand getSubCommand(@NotNull String command) {
        // always return help sub-command instead of null if not found.
        var c = super.getSubCommand(command);
        return c != null ? c : this.helpCommand;
    }

    @Override
    public @NotNull String getName() {
        return "nomi-horizons";
    }

    @Override
    public @NotNull String getUsage(@NotNull ICommandSender sender) {
        return "command.nomi_horizons.usage";
    }

    private static class HelpCommand extends CommandBase {
        @Override
        public int getRequiredPermissionLevel() {
            return 0;
        }

        @Override
        public @NotNull String getName() {
            return "help";
        }

        @Override
        public @NotNull String getUsage(@NotNull ICommandSender sender) {
            return "command.nomi_horizons.usage";
        }

        @Override
        public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String @NotNull [] args) {
            for(var i = 0; i <= 1; i++) {
                sender.sendMessage(new TextComponentTranslation("command.nomi_horizons.help." + i));
            }
        }
    }

    private static class ReloadCommand extends CommandBase {

        @Override
        public @NotNull String getName() {
            return "reload";
        }

        @Override
        public @NotNull String getUsage(@NotNull ICommandSender sender) {
            return "command.nomi_horizons.usage";
        }

        @Override
        public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String @NotNull [] args) throws CommandException {
            try {
                NomiHorizons.LOG.info("Reloading Nomi Horizons Configurations");
                try {
                    NomiHorizonsConfigV2.load();
                    sender.sendMessage(new TextComponentTranslation("command.nomi_horizons.reload.success"));
                } catch(Exception e) {
                    throw new CommandException("command.nomi_horizons.reload.error", "failed to forcibly reload the existing config instance");
                }
            } catch(Exception e) {
                throw new CommandException("command.nomi_horizons.reload.error", e.getMessage());
            }
        }

    }
}
