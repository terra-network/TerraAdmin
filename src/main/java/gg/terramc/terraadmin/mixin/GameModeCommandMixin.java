package gg.terramc.terraadmin.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.GameModeCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(priority = 5, value = GameModeCommand.class)
public abstract class GameModeCommandMixin {
    /**
     * @author braden
     * @reason Because I can
     */
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("vanillagamemode")
                .requires(commandSourceStack -> commandSourceStack.hasPermissionLevel(2));

        dispatcher.register(literalArgumentBuilder);
    }


}